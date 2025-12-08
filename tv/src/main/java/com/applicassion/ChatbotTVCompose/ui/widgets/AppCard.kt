package com.applicassion.ChatbotTVCompose.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.Text
import com.applicassion.ChatbotTVCompose.domain.model.AppModel
import com.applicassion.ChatbotTVCompose.ui.theme.AccentPurple
import com.applicassion.ChatbotTVCompose.ui.theme.BackgroundCard
import com.google.accompanist.drawablepainter.rememberDrawablePainter

// Pre-computed constants
private val CardShape = RoundedCornerShape(12.dp)
private val CardWidth = 120.dp
private val IconSize = 80.dp
private val IconPadding = 12.dp
private val LabelWidth = 100.dp
private val FocusedBorderStroke = BorderStroke(2.dp, AccentPurple)
private val FocusedGlowColor = AccentPurple.copy(alpha = 0.5f)

@Composable
fun AppCard(
    app: AppModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconPainter = rememberDrawablePainter(drawable = app.icon)

    Column(
        modifier = modifier.width(CardWidth),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = onClick,
            shape = CardDefaults.shape(CardShape),
            colors = CardDefaults.colors(
                containerColor = BackgroundCard,
                focusedContainerColor = BackgroundCard
            ),
            scale = CardDefaults.scale(scale = 1f, focusedScale = 1.1f),
            border = CardDefaults.border(
                focusedBorder = Border(
                    border = FocusedBorderStroke,
                    shape = CardShape
                )
            ),
            glow = CardDefaults.glow(
                focusedGlow = Glow(
                    elevationColor = FocusedGlowColor,
                    elevation = 8.dp
                )
            )
        ) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier
                    .size(IconSize)
                    .padding(IconPadding)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = app.label,
            fontSize = 14.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(LabelWidth)
        )
    }
}
