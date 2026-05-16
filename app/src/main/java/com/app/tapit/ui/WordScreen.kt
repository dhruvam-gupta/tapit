package com.app.tapit.ui

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.tapit.data.Category
import java.util.Locale
import kotlin.random.Random

private val backgroundColors = listOf(
    Color(0xFFFFB3BA), Color(0xFFFFDFBA), Color(0xFFFFFFBA),
    Color(0xFFBAFFC9), Color(0xFFBAE1FF), Color(0xFFE6E6FA),
    Color(0xFFFFF0F5), Color(0xFFF0FFF0), Color(0xFFE0FFFF),
    Color(0xFFFFE4E1)
)

@Composable
fun WordScreen(
    category: Category,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    // TextToSpeech setup
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember {
        var engine: TextToSpeech? = null
        engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = engine?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    engine?.setLanguage(Locale.getDefault())
                }
                ttsReady = true
            }
        }
        engine
    }

    DisposableEffect(Unit) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // Word state
    var currentWord by remember { mutableStateOf("") }
    var currentImagePath by remember { mutableStateOf("") }
    var currentColorIndex by remember { mutableIntStateOf(Random.nextInt(backgroundColors.size)) }
    val recentWords = remember { ArrayDeque<Int>() }
    val recentWordsLimit = 5
    var lastColorIndex by remember { mutableIntStateOf(-1) }
    var hasStarted by remember { mutableStateOf(false) }

    fun generateAndSpeakWord() {
        if (!ttsReady) return

        val words = category.words
        // Pick a word not in recent queue
        var newWordIndex: Int
        do {
            newWordIndex = Random.nextInt(words.size)
        } while (recentWords.contains(newWordIndex) && recentWords.size < words.size)

        recentWords.addLast(newWordIndex)
        if (recentWords.size > recentWordsLimit) recentWords.removeFirst()

        // Pick a different background color
        var newColorIndex: Int
        do {
            newColorIndex = Random.nextInt(backgroundColors.size)
        } while (newColorIndex == lastColorIndex)
        lastColorIndex = newColorIndex

        val word = words[newWordIndex]
        currentWord = word
        currentImagePath = category.imagePathForWord(word)
        currentColorIndex = newColorIndex
        hasStarted = true

        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColors[currentColorIndex])
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                generateAndSpeakWord()
            }
    ) {
        // Background image (full screen)
        AnimatedVisibility(
            visible = hasStarted && currentImagePath.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AsyncImage(
                model = currentImagePath,
                contentDescription = currentWord,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Back button (top-left)
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 44.dp)
                .size(44.dp)
                .clip(CircleShape),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White.copy(alpha = 0.7f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF1E293B)
            )
        }

        // Word text (top center)
        Text(
            text = if (hasStarted) currentWord else "Tap the screen!",
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 48.dp, start = 64.dp, end = 64.dp)
                .fillMaxWidth(),
            style = androidx.compose.ui.text.TextStyle(
                shadow = androidx.compose.ui.graphics.Shadow(
                    color = Color.Black,
                    offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )

        // "Speak Again" button (bottom center)
        Button(
            onClick = {
                if (currentWord.isNotEmpty()) {
                    tts?.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.45f)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
        ) {
            Text(
                text = "Speak Again",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                letterSpacing = 0.5.sp
            )
        }
    }
}
