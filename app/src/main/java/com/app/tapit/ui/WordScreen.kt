package com.app.tapit.ui

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.tapit.constants.AppConstants
import com.app.tapit.data.Category
import com.app.tapit.data.CategoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    val dims = AppConstants.WordScreen
    val textSize = AppConstants.TextSize
    val anim = AppConstants.Animation

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
    var lastColorIndex by remember { mutableIntStateOf(-1) }
    var hasStarted by remember { mutableStateOf(false) }
    var sequentialIndex by remember { mutableIntStateOf(0) }

    // Spelling state
    var isSpelling by remember { mutableStateOf(false) }
    var spellingDone by remember { mutableStateOf(false) }
    var activeLetterIndex by remember { mutableIntStateOf(-1) }
    val coroutineScope = rememberCoroutineScope()

    fun generateAndSpeakWord() {
        if (!ttsReady) return

        val words = category.words
        val isSequential = CategoryData.sequentialCategories.contains(category.key)

        val newWordIndex: Int
        if (isSequential) {
            newWordIndex = sequentialIndex
            sequentialIndex = (sequentialIndex + 1) % words.size
        } else {
            // Pick a word not in recent queue
            var randomWordIndex: Int
            do {
                randomWordIndex = Random.nextInt(words.size)
            } while (recentWords.contains(randomWordIndex) && recentWords.size < words.size)

            recentWords.addLast(randomWordIndex)
            if (recentWords.size > anim.RECENT_WORDS_LIMIT) recentWords.removeFirst()
            newWordIndex = randomWordIndex
        }

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

        // Reset spelling state for the new word
        isSpelling = false
        spellingDone = false
        activeLetterIndex = -1

        tts?.speak(getSpokenText(word), TextToSpeech.QUEUE_FLUSH, null, null)
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

        // Back button (top-left) — white arrow with drop shadow, no background
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = dims.BACK_BUTTON_START_PADDING,
                    top = statusBarPadding + dims.BACK_BUTTON_TOP_EXTRA_PADDING
                )
                .size(dims.BACK_BUTTON_SIZE),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Transparent
            )
        ) {
            Box {
                // Shadow layer — dark icon offset behind the main icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black.copy(alpha = dims.BACK_SHADOW_ALPHA),
                    modifier = Modifier
                        .size(dims.BACK_ICON_SIZE)
                        .offset(
                            x = dims.BACK_SHADOW_OFFSET,
                            y = dims.BACK_SHADOW_OFFSET
                        )
                )
                // Main white icon
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(dims.BACK_ICON_SIZE)
                )
            }
        }

        // Word text (top center)
        // Word text and Spelling row (top center)
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    top = statusBarPadding + dims.WORD_TEXT_TOP_EXTRA_PADDING,
                    start = dims.WORD_TEXT_HORIZONTAL_PADDING,
                    end = dims.WORD_TEXT_HORIZONTAL_PADDING
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (hasStarted) currentWord else "Tap the screen!",
                fontSize = textSize.WORD_DISPLAY,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = androidx.compose.ui.text.TextStyle(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black,
                        offset = androidx.compose.ui.geometry.Offset(
                            dims.WORD_SHADOW_OFFSET,
                            dims.WORD_SHADOW_OFFSET
                        ),
                        blurRadius = dims.WORD_SHADOW_BLUR
                    )
                )
            )

            // Spelling row — shown below the word text when spelling is active or done
            if (hasStarted && (isSpelling || spellingDone)) {
                Spacer(modifier = Modifier.height(8.dp))
                SpellingRow(
                    letters = SpellingHelper.getLetters(currentWord),
                    activeLetterIndex = activeLetterIndex,
                    isSpellingDone = spellingDone,
                    isDarkBackground = true
                )
            }
        }

        // Bottom buttons row — respects navigation bar insets
        val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = navBarPadding + dims.SPEAK_BUTTON_BOTTOM_EXTRA_PADDING),
            horizontalArrangement = Arrangement.spacedBy(AppConstants.Spelling.BUTTON_GAP)
        ) {
            // "Speak Again" button
            Button(
                onClick = {
                    if (currentWord.isNotEmpty()) {
                        tts?.speak(getSpokenText(currentWord), TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                modifier = Modifier.width(dims.SPEAK_BUTTON_WIDTH),
                shape = RoundedCornerShape(dims.SPEAK_BUTTON_CORNER_RADIUS),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = dims.SPEAK_BUTTON_ALPHA)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = dims.SPEAK_BUTTON_ELEVATION)
            ) {
                Text(
                    text = "Speak Again",
                    fontSize = textSize.SPEAK_BUTTON,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(
                        horizontal = dims.SPEAK_BUTTON_CONTENT_HORIZONTAL_PADDING,
                        vertical = dims.SPEAK_BUTTON_CONTENT_VERTICAL_PADDING
                    ),
                    letterSpacing = textSize.SPEAK_BUTTON_LETTER_SPACING
                )
            }

            // "Spell It" button
            Button(
                onClick = {
                    if (currentWord.isNotEmpty() && tts != null) {
                        isSpelling = true
                        spellingDone = false
                        activeLetterIndex = -1
                        SpellingHelper.spellWord(
                            tts = tts,
                            word = currentWord,
                            onLetterStart = { index ->
                                coroutineScope.launch(Dispatchers.Main) {
                                    activeLetterIndex = index
                                }
                            },
                            onSpellingComplete = {
                                coroutineScope.launch(Dispatchers.Main) {
                                    isSpelling = false
                                    spellingDone = true
                                }
                            }
                        )
                    }
                },
                modifier = Modifier.width(dims.SPEAK_BUTTON_WIDTH),
                shape = RoundedCornerShape(dims.SPEAK_BUTTON_CORNER_RADIUS),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White.copy(alpha = dims.SPEAK_BUTTON_ALPHA)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = dims.SPEAK_BUTTON_ELEVATION)
            ) {
                Text(
                    text = "Spell It",
                    fontSize = textSize.SPEAK_BUTTON,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(
                        horizontal = AppConstants.Spelling.SPELL_BUTTON_HORIZONTAL_PADDING,
                        vertical = AppConstants.Spelling.SPELL_BUTTON_VERTICAL_PADDING
                    ),
                    letterSpacing = textSize.SPEAK_BUTTON_LETTER_SPACING
                )
            }
        }
    }
}

private fun getSpokenText(word: String): String {
    return word.replace("Ximenia", "Zymenia", ignoreCase = true)
}
