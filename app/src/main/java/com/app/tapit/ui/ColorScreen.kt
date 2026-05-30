package com.app.tapit.ui

import android.speech.tts.TextToSpeech
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.app.tapit.constants.AppConstants
import com.app.tapit.data.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Maps each color word in the Colors category to its actual [Color] value.
 */
private val colorNameToColor = mapOf(
    "Blue" to Color(0xFF2196F3),
    "Khaki" to Color(0xFFC3B091),
    "Orange" to Color(0xFFFF9800),
    "Pink" to Color(0xFFE91E90),
    "Red" to Color(0xFFF44336),
    "Gray" to Color(0xFFC0C0C0),
    "Purple" to Color(0xFF800080),
    "White" to Color(0xFFFFFFFF),
    "Yellow" to Color(0xFFFFEB3B),
    "Green" to Color(0xFF45BF55),
    "Black" to Color(0xFF000000),
    "Brown" to Color(0xFF964B00)
)

@Composable
fun ColorScreen(
    category: Category,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val dims = AppConstants.WordScreen
    val colorDims = AppConstants.ColorScreen
    val textSize = AppConstants.TextSize

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

    // Word / color state
    var currentWord by remember { mutableStateOf("") }
    var currentColor by remember { mutableStateOf(Color.White) }
    var hasStarted by remember { mutableStateOf(false) }
    var sequentialIndex by remember { mutableIntStateOf(0) }

    // Spelling state
    var isSpelling by remember { mutableStateOf(false) }
    var spellingDone by remember { mutableStateOf(false) }
    var activeLetterIndex by remember { mutableIntStateOf(-1) }
    val coroutineScope = rememberCoroutineScope()

    fun generateAndSpeakColor() {
        if (!ttsReady) return

        val words = category.words
        val word = words[sequentialIndex]
        sequentialIndex = (sequentialIndex + 1) % words.size

        currentWord = word
        currentColor = colorNameToColor[word] ?: Color.Gray
        hasStarted = true

        // Reset spelling state for the new word
        isSpelling = false
        spellingDone = false
        activeLetterIndex = -1

        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // Animate the card fill color for a smooth transition
    val animatedCardColor by animateColorAsState(
        targetValue = if (hasStarted) currentColor else Color.White,
        animationSpec = tween(durationMillis = 400),
        label = "cardColor"
    )

    val cardShape = RoundedCornerShape(colorDims.CARD_CORNER_RADIUS)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                generateAndSpeakColor()
            }
    ) {
        // Center content: Card, Name, and Spelling Row in a Column to ensure correct margins
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Centered card (photo frame)
            Box(
                modifier = Modifier
                    .size(colorDims.CARD_SIZE)
                    .clip(cardShape)
                    .background(animatedCardColor)
                    .border(
                        width = colorDims.CARD_BORDER_WIDTH,
                        color = Color.Black,
                        shape = cardShape
                    )
            )

            Spacer(modifier = Modifier.height(colorDims.COLOR_NAME_TOP_PADDING))

            // Color name text
            Text(
                text = if (hasStarted) currentWord else "Tap the screen!",
                fontSize = textSize.WORD_DISPLAY,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Spelling row — shown below the color name when spelling is active or done
            if (hasStarted && (isSpelling || spellingDone)) {
                val spelling = AppConstants.Spelling
                Spacer(modifier = Modifier.height(spelling.ROW_TOP_PADDING))
                SpellingRow(
                    letters = SpellingHelper.getLetters(currentWord),
                    activeLetterIndex = activeLetterIndex,
                    isSpellingDone = spellingDone,
                    isDarkBackground = false
                )
            }
        }

        // Back button (top-left) — dark icon on white background
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
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF1E293B),
                modifier = Modifier.size(dims.BACK_ICON_SIZE)
            )
        }

        // Bottom buttons row
        val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = dims.BUTTON_ROW_MAX_WIDTH)
                .padding(bottom = navBarPadding + dims.SPEAK_BUTTON_BOTTOM_EXTRA_PADDING)
                .fillMaxWidth(dims.BUTTON_ROW_MAX_WIDTH_RATIO),
            horizontalArrangement = Arrangement.spacedBy(AppConstants.Spelling.BUTTON_GAP)
        ) {
            // "Speak Again" button
            Button(
                onClick = {
                    if (currentWord.isNotEmpty()) {
                        tts?.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(dims.SPEAK_BUTTON_CORNER_RADIUS),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F5F9)
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
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(dims.SPEAK_BUTTON_CORNER_RADIUS),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F5F9)
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
