package com.app.tapit.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.app.tapit.constants.AppConstants
import com.app.tapit.constants.AppConstants.TempConstants.comingSoonCategoryKeys
import com.app.tapit.data.Category
import com.app.tapit.data.CategoryData

// Curated pastel palette for category cards
private val cardColors = listOf(
    Color(0xFFFFB3BA), // Soft pink
    Color(0xFFFFDFBA), // Soft peach
    Color(0xFFBBF7D0), // Soft mint
    Color(0xFFBAE1FF), // Soft blue
    Color(0xFFE6E6FA), // Lavender
    Color(0xFFFDE68A), // Soft yellow
    Color(0xFFC4B5FD), // Soft purple
    Color(0xFFFBCFE8), // Soft rose
    Color(0xFF99F6E4), // Soft teal
    Color(0xFFFED7AA), // Soft orange
    Color(0xFFA5F3FC), // Soft cyan
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onCategoryClick: (Category) -> Unit
) {
    val grid = AppConstants.CategoryGrid
    val textSize = AppConstants.TextSize

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tap and Learn",
                        fontSize = textSize.APP_TITLE,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1E293B)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(grid.COLUMN_COUNT),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                horizontal = grid.HORIZONTAL_PADDING,
                vertical = grid.VERTICAL_PADDING
            ),
            horizontalArrangement = Arrangement.spacedBy(grid.CELL_SPACING),
            verticalArrangement = Arrangement.spacedBy(grid.CELL_SPACING)
        ) {
            items(CategoryData.categories) { category ->
                val colorIndex = CategoryData.categories.indexOf(category) % cardColors.size
                val isComingSoon = category.key in comingSoonCategoryKeys
                CategoryCard(
                    category = category,
                    cardColor = cardColors[colorIndex],
                    isComingSoon = isComingSoon,
                    onClick = { if (!isComingSoon) onCategoryClick(category) }
                )
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    cardColor: Color,
    isComingSoon: Boolean = false,
    onClick: () -> Unit
) {
    val grid = AppConstants.CategoryGrid
    val textSize = AppConstants.TextSize
    val anim = AppConstants.Animation

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) anim.CARD_PRESS_SCALE else 1f,
        animationSpec = tween(durationMillis = anim.CARD_PRESS_DURATION_MS),
        label = "card_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(grid.CARD_ASPECT_RATIO)
            .scale(scale)
            .shadow(
                elevation = grid.CARD_ELEVATION,
                shape = RoundedCornerShape(grid.CARD_CORNER_RADIUS),
                ambientColor = cardColor.copy(alpha = grid.CARD_SHADOW_ALPHA),
                spotColor = cardColor.copy(alpha = grid.CARD_SHADOW_ALPHA)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                enabled = !isComingSoon
            ),
        shape = RoundedCornerShape(grid.CARD_CORNER_RADIUS),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(grid.CARD_INNER_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Category thumbnail (Emoji Only)
            Box(
                modifier = Modifier
                    .size(grid.THUMBNAIL_SIZE)
                    .clip(RoundedCornerShape(grid.THUMBNAIL_CORNER_RADIUS)),
                contentAlignment = Alignment.Center
            ) {
                val isMultiLine = category.emoji.contains("\n")
                Text(
                    text = category.emoji,
                    fontSize = if (isMultiLine) textSize.CATEGORY_EMOJI_MULTILINE else textSize.CATEGORY_EMOJI,
                    lineHeight = if (isMultiLine) textSize.CATEGORY_EMOJI_LINE_HEIGHT else TextUnit.Unspecified,
                    textAlign = TextAlign.Center
                )
            }

            // Category name
            Text(
                text = category.name,
                fontSize = textSize.CATEGORY_NAME,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = grid.CATEGORY_NAME_TOP_PADDING)
            )

            // Item count badge
            Text(
                text = "${category.words.size} items",
                fontSize = textSize.ITEM_COUNT,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = grid.ITEM_COUNT_TOP_PADDING)
            )

            // Coming Soon badge
            if (isComingSoon) {
                Text(
                        text = "COMING SOON",
                        fontSize = textSize.ITEM_COUNT,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF6B35),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = grid.ITEM_COUNT_TOP_PADDING)
                )
            }
        }
    }
}
