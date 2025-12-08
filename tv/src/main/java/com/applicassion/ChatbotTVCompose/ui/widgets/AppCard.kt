package com.applicassion.ChatbotTVCompose.ui.widgets

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
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.applicassion.ChatbotTVCompose.domain.model.AppModel
import com.applicassion.ChatbotTVCompose.ui.theme.AccentPurple
import com.applicassion.ChatbotTVCompose.ui.theme.BackgroundCard
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun AppCard(
    app: AppModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(12.dp)
    
    Column(
        modifier = modifier.width(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            onClick = onClick,
            shape = CardDefaults.shape(cardShape),
            colors = CardDefaults.colors(
                containerColor = BackgroundCard,
                focusedContainerColor = BackgroundCard
            ),
            scale = CardDefaults.scale(
                scale = 1f,
                focusedScale = 1.1f
            ),
            border = CardDefaults.border(
                focusedBorder = androidx.tv.material3.Border(
                    border = androidx.compose.foundation.BorderStroke(2.dp, AccentPurple),
                    shape = cardShape
                )
            ),
            glow = CardDefaults.glow(
                focusedGlow = androidx.tv.material3.Glow(
                    elevationColor = AccentPurple.copy(alpha = 0.5f),
                    elevation = 8.dp
                )
            )
        ) {
            Image(
                painter = rememberDrawablePainter(drawable = app.icon),
                contentDescription = app.label,
                modifier = Modifier
                    .size(80.dp)
                    .padding(12.dp)
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
            modifier = Modifier.width(100.dp)
        )
    }
}

