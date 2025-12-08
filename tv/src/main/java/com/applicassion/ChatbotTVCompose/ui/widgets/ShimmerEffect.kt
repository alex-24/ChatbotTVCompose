package com.applicassion.ChatbotTVCompose.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.applicassion.ChatbotTVCompose.ui.theme.BackgroundCard

// Pre-computed shapes to avoid recreation
private val IconShape = RoundedCornerShape(12.dp)
private val LabelShape = RoundedCornerShape(4.dp)
private val PlaceholderColor = BackgroundCard.copy(alpha = 0.6f)

@Composable
fun AppCardPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon placeholder
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(IconShape)
                .background(PlaceholderColor)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Label placeholder
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(16.dp)
                .clip(LabelShape)
                .background(PlaceholderColor)
        )
    }
}

@Composable
fun AppsRowPlaceholder(
    modifier: Modifier = Modifier,
    itemCount: Int = 8
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Title placeholder
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(24.dp)
                    .clip(LabelShape)
                    .background(PlaceholderColor)
            )
            
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(20.dp)
                    .clip(LabelShape)
                    .background(PlaceholderColor)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Apps row placeholder - use Row instead of LazyRow for static content
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(itemCount.coerceAtMost(6)) {
                AppCardPlaceholder()
            }
        }
    }
}
