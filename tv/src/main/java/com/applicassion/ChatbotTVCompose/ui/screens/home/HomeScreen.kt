package com.applicassion.ChatbotTVCompose.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.applicassion.ChatbotTVCompose.LocalVocalAssistantViewModel
import com.applicassion.ChatbotTVCompose.ui.VocalAssistantUIState
import com.applicassion.ChatbotTVCompose.ui.widgets.HeaderWidget
import com.applicassion.ChatbotTVCompose.ui.widgets.ChatBubble

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val vocalAssistantViewModel = LocalVocalAssistantViewModel.current
    val uiState by vocalAssistantViewModel.vocalAssistantUIState.observeAsState(
        VocalAssistantUIState()
    )
    val conversation = vocalAssistantViewModel.conversation

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        HeaderWidget()

        // Show error message if present
        uiState.error?.let { error ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
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
        }
    }
}