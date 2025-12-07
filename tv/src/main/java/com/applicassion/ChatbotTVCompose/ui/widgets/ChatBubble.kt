package com.applicassion.ChatbotTVCompose.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.applicassion.ChatbotTVCompose.ui.ConversationRole
import com.applicassion.ChatbotTVCompose.ui.theme.ChatBotBubble
import com.applicassion.ChatbotTVCompose.ui.theme.ChatBotBubbleBorder
import com.applicassion.ChatbotTVCompose.ui.theme.UserBubble
import com.applicassion.ChatbotTVCompose.ui.theme.UserBubbleBorder


private val ChatBubbleShape = RoundedCornerShape(16.dp)

@Composable
fun ChatBubble(
    text: String,
    role: ConversationRole,
    modifier: Modifier = Modifier
) {
    val (containerColor, borderColor, alignment) = when (role) {
        ConversationRole.CHAT_BOT -> Triple(ChatBotBubble, ChatBotBubbleBorder, Alignment.CenterStart)
        ConversationRole.USER -> Triple(UserBubble, UserBubbleBorder, Alignment.CenterEnd)
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Card(
            onClick = {},
            shape = CardDefaults.shape(ChatBubbleShape),
            colors = CardDefaults.colors(
                containerColor = containerColor,
                focusedContainerColor = containerColor
            ),
            scale = CardDefaults.scale(focusedScale = 1f),
            border = CardDefaults.border(
                focusedBorder = Border(
                    border = BorderStroke(3.dp, borderColor),
                    shape = ChatBubbleShape
                )
            ),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text(
                text = text,
                color = Color.Black,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}