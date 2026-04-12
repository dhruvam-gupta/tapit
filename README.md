# Tap and Learn

Tap and Learn is a simple, interactive, and kid-friendly Android application designed to help toddlers and children learn new words. 

## How it Works
When a user opens the app and taps anywhere on the screen, the application:
1. Picks a random word from a predefined vocabulary list (e.g., animals, fruits, objects).
2. Displays the word clearly on the screen.
3. Reads the word aloud using the device's built-in TextToSpeech (TTS) engine.
4. Changes the background to a random, eye-friendly pastel color for visual stimulation.

## Features
- **Interactive Learning:** Simple tap-to-interact functionality that is easy for young children to use.
- **Audio Feedback:** Utilizes Android's TextToSpeech to pronounce the displayed words, assisting in auditory learning.
- **Visuals:** Randomly rotating pastel background colors (`#FFB3BA`, `#FFDFBA`, etc.) that are soft on the eyes.
- **Vocabulary:** A predefined set of words tailored for kids (Apple, Banana, Cat, Dog, Elephant, etc.).

## Tech Stack
- **Platform:** Android (minSdk 24, targetSdk 34)
- **Language:** Java
- **UI Frameworks:** AppCompat, Material Components, ConstraintLayout
- **Key Android APIs:** `android.speech.tts.TextToSpeech`
