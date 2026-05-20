package com.app.tapit.constants

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized constants for layout dimensions, text sizes, animations, and other values.
 * All hardcoded layout values across the app should be defined here.
 */
object AppConstants {

    // ── Grid & Card (CategoryScreen) ──────────────────────────
    object CategoryGrid {
        val COLUMN_COUNT = 2
        val HORIZONTAL_PADDING = 16.dp
        val VERTICAL_PADDING = 8.dp
        val CELL_SPACING = 14.dp
        val CARD_ASPECT_RATIO = 0.85f
        val CARD_CORNER_RADIUS = 20.dp
        val CARD_ELEVATION = 8.dp
        val CARD_SHADOW_ALPHA = 0.4f
        val CARD_INNER_PADDING = 12.dp
        val THUMBNAIL_SIZE = 90.dp
        val THUMBNAIL_CORNER_RADIUS = 16.dp
        val CATEGORY_NAME_TOP_PADDING = 12.dp
        val ITEM_COUNT_TOP_PADDING = 4.dp
    }

    // ── Word Screen Layout ───────────────────────────────────
    object WordScreen {
        val BACK_BUTTON_START_PADDING = 8.dp
        val BACK_BUTTON_TOP_EXTRA_PADDING = 8.dp
        val BACK_BUTTON_SIZE = 48.dp
        val BACK_ICON_SIZE = 28.dp
        val BACK_SHADOW_OFFSET = 1.dp
        val BACK_SHADOW_ALPHA = 0.5f

        val WORD_TEXT_TOP_EXTRA_PADDING = 12.dp
        val WORD_TEXT_HORIZONTAL_PADDING = 64.dp
        val WORD_SHADOW_OFFSET = 2f
        val WORD_SHADOW_BLUR = 4f

        val SPEAK_BUTTON_BOTTOM_EXTRA_PADDING = 16.dp
        val SPEAK_BUTTON_CORNER_RADIUS = 28.dp
        val SPEAK_BUTTON_ELEVATION = 3.dp
        val SPEAK_BUTTON_CONTENT_HORIZONTAL_PADDING = 16.dp
        val SPEAK_BUTTON_CONTENT_VERTICAL_PADDING = 8.dp
        val SPEAK_BUTTON_ALPHA = 0.45f
    }

    // ── Text Sizes ───────────────────────────────────────────
    object TextSize {
        val APP_TITLE = 24.sp
        val CATEGORY_NAME = 17.sp
        val CATEGORY_EMOJI = 48.sp
        val CATEGORY_EMOJI_MULTILINE = 34.sp
        val CATEGORY_EMOJI_LINE_HEIGHT = 38.sp
        val ITEM_COUNT = 12.sp
        val WORD_DISPLAY = 35.sp
        val SPEAK_BUTTON = 18.sp
        val SPEAK_BUTTON_LETTER_SPACING = 0.5.sp
    }

    // ── Animations ───────────────────────────────────────────
    object Animation {
        val CARD_PRESS_SCALE = 0.94f
        val CARD_PRESS_DURATION_MS = 100
        const val RECENT_WORDS_LIMIT = 5
    }
}
