package com.app.tapit

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import java.util.Locale
import kotlin.random.Random
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private lateinit var wordTextView: TextView
    private lateinit var mainLayout: View
    private lateinit var backgroundImageView: ImageView

    private val words = arrayOf(
        "Apple", "Banana", "Cat", "Dog", "Elephant",
        "Frog", "Glass", "Horse", "Ice Cream", "Juice",
        "Kangaroo", "Lion", "Monkey", "Nest", "Orange",
        "Penguin", "Queen", "Rabbit", "Sun", "Tiger",
        "Umbrella", "Violin", "Watermelon", "Xylophone",
        "Yak", "Zebra"
    )

    private val backgroundColors = arrayOf(
        "#FFB3BA", "#FFDFBA", "#FFFFBA", "#BAFFC9", "#BAE1FF",
        "#E6E6FA", "#FFF0F5", "#F0FFF0", "#E0FFFF", "#FFE4E1"
    )

    // Tracks the last N word indices shown; a word can only repeat after it leaves this queue
    private val recentWords = ArrayDeque<Int>()
    private val recentWordsLimit = 5
    private var lastColorIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordTextView = findViewById(R.id.wordTextView)
        mainLayout = findViewById(R.id.main_layout)
        backgroundImageView = findViewById(R.id.backgroundImageView)
        val repeatButton = findViewById<Button>(R.id.repeatButton)

        // Capture original margins from XML
        val originalTextMargin = (wordTextView.layoutParams as android.view.ViewGroup.MarginLayoutParams).topMargin
        val originalButtonMargin = (repeatButton.layoutParams as android.view.ViewGroup.MarginLayoutParams).bottomMargin

        textToSpeech = TextToSpeech(this, this)

        // Initial eye-friendly background color
        val initialColorIndex = Random.nextInt(backgroundColors.size)
        lastColorIndex = initialColorIndex
        mainLayout.setBackgroundColor(Color.parseColor(backgroundColors[initialColorIndex]))

        // Handle Edge-to-Edge insets by adding to the original XML margins
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            
            // Apply original margin + status bar height + extra buffer for camera cutouts
            val textParams = wordTextView.layoutParams as android.view.ViewGroup.MarginLayoutParams
            textParams.topMargin = originalTextMargin + systemBars.top + 20
            wordTextView.layoutParams = textParams

            // Apply original margin + navigation bar height
            val buttonParams = repeatButton.layoutParams as android.view.ViewGroup.MarginLayoutParams
            buttonParams.bottomMargin = originalButtonMargin + systemBars.bottom
            repeatButton.layoutParams = buttonParams

            insets
        }

        mainLayout.setOnClickListener {
            generateAndSpeakWord()
        }


        repeatButton.setOnClickListener {
            val currentWord = wordTextView.text.toString()
            if (currentWord.isNotEmpty()) {
                textToSpeech?.speak(currentWord, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    private fun generateAndSpeakWord() {
        textToSpeech?.let { tts ->
            // Pick a word whose index is not in the recent-words queue
            var newWordIndex: Int
            do {
                newWordIndex = Random.nextInt(words.size)
            } while (recentWords.contains(newWordIndex))
            // Add to queue and evict oldest if over the limit
            recentWords.addLast(newWordIndex)
            if (recentWords.size > recentWordsLimit) recentWords.removeFirst()
            val wordToSpeak = words[newWordIndex]

            // Pick a background color different from the last one
            var newColorIndex: Int
            do {
                newColorIndex = Random.nextInt(backgroundColors.size)
            } while (newColorIndex == lastColorIndex)
            lastColorIndex = newColorIndex

            // Update UI
            wordTextView.text = wordToSpeak
            mainLayout.setBackgroundColor(Color.parseColor(backgroundColors[newColorIndex]))
            // Attempt to load an image from assets (e.g., "apple.webp")
            val imageFileName = "${wordToSpeak.lowercase().replace(" ", "_")}.webp"
            backgroundImageView.load("file:///android_asset/images/$imageFileName") {
                crossfade(true)
            }

            // Speak the word
            tts.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech?.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language not supported, fallback to default
                textToSpeech?.setLanguage(Locale.getDefault())
            }
        }
    }

    override fun onDestroy() {
        textToSpeech?.apply {
            stop()
            shutdown()
        }
        super.onDestroy()
    }
}
