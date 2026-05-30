package com.app.tapit.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.app.tapit.constants.AppConstants

/**
 * Displays the letters of a word as individual badges in a wrapping row.
 *
 * @param letters            List of uppercase letter strings (from [SpellingHelper.getLetters]).
 * @param activeLetterIndex  The index of the letter currently being spoken, or -1 if none.
 * @param isSpellingDone     True once spelling has completed — all letters stay highlighted.
 * @param isDarkBackground   If true, uses light/white tones; if false, uses dark tones.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpellingRow(
    letters: List<String>,
    activeLetterIndex: Int,
    isSpellingDone: Boolean,
    isDarkBackground: Boolean = true
) {
    val spelling = AppConstants.Spelling

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spelling.LETTER_SPACING),
        verticalArrangement = Arrangement.spacedBy(spelling.LETTER_SPACING),
        modifier = Modifier.padding(horizontal = spelling.ROW_HORIZONTAL_PADDING)
    ) {
        letters.forEachIndexed { index, letter ->
            val isActive = index == activeLetterIndex
            val isPast = isSpellingDone || index < activeLetterIndex

            LetterBadge(
                letter = letter,
                isActive = isActive,
                isPast = isPast,
                isDarkBackground = isDarkBackground
            )
        }
    }
}

@Composable
private fun LetterBadge(
    letter: String,
    isActive: Boolean,
    isPast: Boolean,
    isDarkBackground: Boolean
) {
    val spelling = AppConstants.Spelling

    // Scale bounces up when active
    val scale by animateFloatAsState(
        targetValue = if (isActive) spelling.ACTIVE_SCALE else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 300f),
        label = "letter_scale"
    )

    // Background color transitions
    val bgColor by animateColorAsState(
        targetValue = when {
            isActive -> if (isDarkBackground) Color.White else Color(0xFF1E293B)
            isPast -> if (isDarkBackground) Color.White.copy(alpha = 0.7f) else Color(0xFF1E293B).copy(alpha = 0.7f)
            else -> if (isDarkBackground) Color.White.copy(alpha = 0.2f) else Color(0xFFE2E8F0)
        },
        label = "letter_bg"
    )

    // Text color transitions
    val textColor by animateColorAsState(
        targetValue = when {
            isActive -> if (isDarkBackground) Color(0xFF1E293B) else Color.White
            isPast -> if (isDarkBackground) Color(0xFF1E293B).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)
            else -> if (isDarkBackground) Color.White.copy(alpha = 0.5f) else Color(0xFF94A3B8)
        },
        label = "letter_text"
    )

    Box(
        modifier = Modifier
            .size(spelling.BADGE_SIZE)
            .scale(scale)
            .background(
                color = bgColor,
                shape = RoundedCornerShape(spelling.BADGE_CORNER_RADIUS)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            fontSize = spelling.LETTER_FONT_SIZE,
            fontWeight = if (isActive || isPast) FontWeight.ExtraBold else FontWeight.Medium,
            color = textColor
        )
    }
}
