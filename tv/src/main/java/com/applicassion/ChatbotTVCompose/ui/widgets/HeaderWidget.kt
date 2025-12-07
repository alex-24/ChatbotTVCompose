package com.applicassion.ChatbotTVCompose.ui.widgets

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import com.applicassion.ChatbotTVCompose.LocalVocalAssistantViewModel

@Composable
fun HeaderWidget(modifier: Modifier = Modifier) {
    val vocalAssistantViewModel = LocalVocalAssistantViewModel.current
    val context = LocalContext.current
    val audioRecordingPermission = android.Manifest.permission.RECORD_AUDIO

    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.2f else 1f, label = "scale")

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            vocalAssistantViewModel.onMicClickWithPermission()
        }
    }

    val onMicClick: () -> Unit = {
        val granted = ContextCompat.checkSelfPermission(
            context,
            audioRecordingPermission
        ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            vocalAssistantViewModel.onMicClickWithPermission()
        } else {
            permissionLauncher.launch(audioRecordingPermission)
        }
    }

    Row(
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.Android,
            contentDescription = "App logo"
        )
        Spacer(modifier = Modifier.weight(1f))
        Surface(
            modifier = Modifier
                .scale(scale)
                .then(
                    if (isFocused) {
                        Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(8.dp)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .focusable()
                .onKeyEvent { keyEvent ->
                    if (keyEvent.type == KeyEventType.KeyUp &&
                        (keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter)
                    ) {
                        onMicClick()
                        true
                    } else {
                        false
                    }
                },
            shape = CircleShape
        ) {
            when (vocalAssistantViewModel.vocalAssistantUIState.value?.isRecording) {
                true -> {
                    TvCircularProgressIndicator()
                }
                else -> {
                    Icon(
                        modifier = Modifier.padding(8.dp),
                        imageVector = Icons.Rounded.Mic,
                        contentDescription = "Start voice recording"
                    )
                }
            }
        }
    }
}
