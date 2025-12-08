package com.applicassion.ChatbotTVCompose.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.applicassion.ChatbotTVCompose.LocalVocalAssistantViewModel
import com.applicassion.ChatbotTVCompose.ui.navigation.LocalAppsViewModel
import com.applicassion.ChatbotTVCompose.ui.ConversationRole
import com.applicassion.ChatbotTVCompose.ui.VocalAssistantUIState
import com.applicassion.ChatbotTVCompose.ui.theme.ChatBotBubble
import com.applicassion.ChatbotTVCompose.ui.theme.ChatBotBubbleBorder
import com.applicassion.ChatbotTVCompose.ui.theme.TextPrimary
import com.applicassion.ChatbotTVCompose.ui.theme.UserBubble
import com.applicassion.ChatbotTVCompose.ui.theme.UserBubbleBorder
import com.applicassion.ChatbotTVCompose.ui.widgets.AppsRow
import com.applicassion.ChatbotTVCompose.ui.widgets.AppsRowPlaceholder
import com.applicassion.ChatbotTVCompose.ui.widgets.HeaderWidget
import com.applicassion.ChatbotTVCompose.ui.widgets.TvCircularProgressIndicator

private val ChatBubbleShape = RoundedCornerShape(16.dp)
private val ChatBubbleNoScale = CardDefaults.scale(focusedScale = 1f)
private val ChatBotBorder = BorderStroke(3.dp, ChatBotBubbleBorder)
private val UserBorder = BorderStroke(3.dp, UserBubbleBorder)

@Composable
fun HomeScreen(
    onNavigateToAllApps: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vocalAssistantViewModel = LocalVocalAssistantViewModel.current
    val appsViewModel = LocalAppsViewModel.current
    val uiState by vocalAssistantViewModel.vocalAssistantUIState.observeAsState(
        VocalAssistantUIState()
    )
    val conversation = vocalAssistantViewModel.conversation
    
    // Apps state
    val installedApps by appsViewModel.appList.collectAsState()
    val isLoadingApps by appsViewModel.isLoading.collectAsState()
    
    // Memoize the limited apps list to avoid creating new list on every recomposition
    val displayedApps by remember { derivedStateOf { installedApps.take(8) } }

    Column(
        modifier = modifier
            .padding(
                start = 32.dp,
                end = 32.dp,
                top = 16.dp
            )
            .fillMaxSize()
    ) {
        // Header with mic button
        HeaderWidget(onAppsClick = onNavigateToAllApps)

        Spacer(modifier = Modifier.height(24.dp))

        // Apps Row - show placeholder while loading
        if (isLoadingApps) {
            AppsRowPlaceholder()
            Spacer(modifier = Modifier.height(24.dp))
        } else if (displayedApps.isNotEmpty()) {
            AppsRow(
                title = "Your Apps",
                apps = displayedApps,
                onAppClick = appsViewModel::launchApp,
                onSeeAllClick = onNavigateToAllApps
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Show error message if present
        uiState.error?.let { error ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = androidx.tv.material3.SurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Chat Section Title
        Text(
            text = "Assistant",
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        // Chat messages
        LazyColumn(
            modifier = Modifier
                .weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(conversation.size) { index ->
                val (role, text) = conversation[index]
                ChatBubble(
                    text = text,
                    role = role
                )
            }
            
            // Show loading indicator when waiting for bot response
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        TvCircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubble(
    text: String,
    role: ConversationRole,
    modifier: Modifier = Modifier
) {
    val isBot = role == ConversationRole.CHAT_BOT
    val containerColor = if (isBot) ChatBotBubble else UserBubble
    val borderStroke = if (isBot) ChatBotBorder else UserBorder
    val alignment = if (isBot) Alignment.CenterStart else Alignment.CenterEnd
    
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
            scale = ChatBubbleNoScale,
            border = CardDefaults.border(
                focusedBorder = Border(
                    border = borderStroke,
                    shape = ChatBubbleShape
                )
            ),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
