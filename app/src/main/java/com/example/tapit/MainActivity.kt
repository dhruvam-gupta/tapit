package com.example.tapit

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var textToSpeech: TextToSpeech? = null
    private lateinit var wordTextView: TextView
    private lateinit var mainLayout: View

    private val words = arrayOf(
        "Apple", "Banana", "Cat", "Dog", "Elephant",
        "Frog", "Giraffe", "Horse", "Ice Cream", "Juice",
        "Kangaroo", "Lion", "Monkey", "Nest", "Orange",
        "Penguin", "Queen", "Rabbit", "Sun", "Tiger",
        "Umbrella", "Violin", "Watermelon", "Xylophone",
        "Yak", "Zebra"
    )

    private val backgroundColors = arrayOf(
        "#FFB3BA", "#FFDFBA", "#FFFFBA", "#BAFFC9", "#BAE1FF",
        "#E6E6FA", "#FFF0F5", "#F0FFF0", "#E0FFFF", "#FFE4E1"
    )

    private var lastWordIndex = -1
    private var lastColorIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordTextView = findViewById(R.id.wordTextView)
        mainLayout = findViewById(R.id.main_layout)

        textToSpeech = TextToSpeech(this, this)

        // Initial eye-friendly background color
        val initialColorIndex = Random.nextInt(backgroundColors.size)
        lastColorIndex = initialColorIndex
        mainLayout.setBackgroundColor(Color.parseColor(backgroundColors[initialColorIndex]))

        mainLayout.setOnClickListener {
            generateAndSpeakWord()
        }
    }

    private fun generateAndSpeakWord() {
        textToSpeech?.let { tts ->
            // Pick a new random word different from the last one
            var newWordIndex: Int
            do {
                newWordIndex = Random.nextInt(words.size)
            } while (newWordIndex == lastWordIndex && words.size > 1)
            lastWordIndex = newWordIndex
            val wordToSpeak = words[newWordIndex]

            // Pick a new random background color different from the last one
            var newColorIndex: Int
            do {
                newColorIndex = Random.nextInt(backgroundColors.size)
            } while (newColorIndex == lastColorIndex && backgroundColors.size > 1)
            lastColorIndex = newColorIndex

            // Update UI
            wordTextView.text = wordToSpeak
            mainLayout.setBackgroundColor(Color.parseColor(backgroundColors[newColorIndex]))

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
