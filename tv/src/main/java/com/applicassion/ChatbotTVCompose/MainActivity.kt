package com.applicassion.ChatbotTVCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.applicassion.ChatbotTVCompose.ui.VocalAssistantViewModel
import com.applicassion.ChatbotTVCompose.ui.navigation.NavigationComponent
import com.applicassion.ChatbotTVCompose.ui.screens.home.HomeScreen
import com.applicassion.ChatbotTVCompose.ui.theme.ChatbotTVComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ChatbotTVComposeApplicationUI(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                NavigationComponent()
            }
        }
    }
}

val LocalVocalAssistantViewModel = staticCompositionLocalOf<VocalAssistantViewModel> {
    error("AssistantViewModel not provided")
}

@Composable
fun ChatbotTVComposeApplicationUI(
    modifier: Modifier = Modifier,
    vocalAssistantViewModel: VocalAssistantViewModel = hiltViewModel(),
    content: @Composable () -> Unit,
) {
    ChatbotTVComposeTheme {
        Surface(
            modifier = modifier,
            shape = RectangleShape
        ) {
            CompositionLocalProvider(
                LocalVocalAssistantViewModel provides vocalAssistantViewModel
            ) {
                content.invoke()
            }
        }
    }
}

/*@Preview(device = Devices.TV_1080p)
@Composable
fun ChatbotTVComposeApplicationUIPreview() {
    ChatbotTVComposeTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RectangleShape
        ) {
            HomeScreen()
        }
    }
}*/