package com.example.tapit;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private TextView wordTextView;
    private View mainLayout;
    
    private final String[] words = {
            "Apple", "Banana", "Cat", "Dog", "Elephant", 
            "Frog", "Giraffe", "Horse", "Ice Cream", "Juice",
            "Kangaroo", "Lion", "Monkey", "Nest", "Orange",
            "Penguin", "Queen", "Rabbit", "Sun", "Tiger",
            "Umbrella", "Violin", "Watermelon", "Xylophone",
            "Yak", "Zebra"
    };

    // Eye-friendly pastel colors for the background
    private final String[] backgroundColors = {
            "#FFB3BA", "#FFDFBA", "#FFFFBA", "#BAFFC9", "#BAE1FF",
            "#E6E6FA", "#FFF0F5", "#F0FFF0", "#E0FFFF", "#FFE4E1"
    };

    private Random random;
    private int lastWordIndex = -1;
    private int lastColorIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordTextView = findViewById(R.id.wordTextView);
        mainLayout = findViewById(R.id.main_layout);
        
        textToSpeech = new TextToSpeech(this, this);
        random = new Random();

        // Initial eye-friendly background color
        int initialColorIndex = random.nextInt(backgroundColors.length);
        lastColorIndex = initialColorIndex;
        mainLayout.setBackgroundColor(Color.parseColor(backgroundColors[initialColorIndex]));

        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndSpeakWord();
            }
        });
    }

    private void generateAndSpeakWord() {
        if (textToSpeech != null) {
            // Pick a new random word different from the last one
            int newWordIndex;
            do {
                newWordIndex = random.nextInt(words.length);
            } while (newWordIndex == lastWordIndex && words.length > 1);
            lastWordIndex = newWordIndex;
            String wordToSpeak = words[newWordIndex];

            // Pick a new random background color different from the last one
            int newColorIndex;
            do {
                newColorIndex = random.nextInt(backgroundColors.length);
            } while (newColorIndex == lastColorIndex && backgroundColors.length > 1);
            lastColorIndex = newColorIndex;

            // Update UI
            wordTextView.setText(wordToSpeak);
            mainLayout.setBackgroundColor(Color.parseColor(backgroundColors[newColorIndex]));

            // Speak the word
            textToSpeech.speak(wordToSpeak, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language not supported, fallback to default
                textToSpeech.setLanguage(Locale.getDefault());
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
