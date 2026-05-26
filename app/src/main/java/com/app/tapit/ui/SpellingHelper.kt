package com.app.tapit.ui

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.os.Bundle

/**
 * Utility that spells a word letter-by-letter via TTS.
 *
 * Only alphabetic characters are spelled out; spaces, hyphens, and other
 * punctuation are skipped so that "Twenty-one" becomes "T, W, E, N, T, Y, O, N, E".
 *
 * An [onLetterStart] callback fires on the **main thread** each time a new
 * letter begins, passing the index into the *filtered* letter list.
 * An [onSpellingComplete] callback fires when the last letter finishes.
 */
object SpellingHelper {

    private const val UTTERANCE_PREFIX = "spell_letter_"

    /**
     * Spells [word] letter-by-letter through [tts].
     *
     * @param tts             An initialised [TextToSpeech] engine.
     * @param word            The word to spell.
     * @param onLetterStart   Called with the 0-based index of each letter as it starts.
     * @param onSpellingComplete Called when the last letter finishes speaking.
     */
    fun spellWord(
        tts: TextToSpeech,
        word: String,
        onLetterStart: (index: Int) -> Unit,
        onSpellingComplete: () -> Unit
    ) {
        // Filter to only alphabetic characters
        val letters = word.filter { it.isLetter() }
        if (letters.isEmpty()) return

        // Set up the listener before queuing utterances
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                val idx = utteranceId?.removePrefix(UTTERANCE_PREFIX)?.toIntOrNull() ?: return
                onLetterStart(idx)
            }

            override fun onDone(utteranceId: String?) {
                val idx = utteranceId?.removePrefix(UTTERANCE_PREFIX)?.toIntOrNull() ?: return
                if (idx == letters.length - 1) {
                    onSpellingComplete()
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                // No-op: swallow TTS errors gracefully
            }
        })

        // Queue each letter. The first one flushes any ongoing speech,
        // subsequent ones are added to the queue.
        letters.forEachIndexed { index, letter ->
            val params = Bundle()
            tts.speak(
                letter.uppercase(),
                if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD,
                params,
                "$UTTERANCE_PREFIX$index"
            )
        }
    }

    /**
     * Returns only the alphabetic characters of [word] as a list of uppercase strings.
     * This is used by the UI to display the letter badges.
     */
    fun getLetters(word: String): List<String> {
        return word.filter { it.isLetter() }.map { it.uppercase() }
    }
}
