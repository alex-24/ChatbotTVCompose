package com.applicassion.ChatbotTVCompose.ui.widgets

import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.tv.material3.Icon
import androidx.tv.material3.Text
import com.applicassion.ChatbotTVCompose.LocalVocalAssistantViewModel
import com.applicassion.ChatbotTVCompose.ui.theme.AccentIndigo
import com.applicassion.ChatbotTVCompose.ui.theme.AccentPurple
import com.applicassion.ChatbotTVCompose.ui.theme.BackgroundDark
import com.applicassion.ChatbotTVCompose.ui.theme.BackgroundDarkHover
import com.applicassion.ChatbotTVCompose.ui.theme.RecordingRed
import com.applicassion.ChatbotTVCompose.ui.theme.TextMuted

@Composable
fun HeaderWidget(modifier: Modifier = Modifier) {
    val vocalAssistantViewModel = LocalVocalAssistantViewModel.current
    val context = LocalContext.current
    val audioRecordingPermission = android.Manifest.permission.RECORD_AUDIO

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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Logo & Name
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(AccentIndigo)
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "◆",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Nova",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "TV",
                fontSize = 28.sp,
                fontWeight = FontWeight.Light,
                color = AccentPurple
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Notifications
            HeaderIconButton(
                icon = Icons.Rounded.Notifications,
                contentDescription = "Notifications",
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }
            )

            // Apps
            HeaderIconButton(
                icon = Icons.Rounded.Apps,
                contentDescription = "Apps",
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }
            )

            // Settings
            HeaderIconButton(
                icon = Icons.Rounded.Settings,
                contentDescription = "Settings",
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_SETTINGS).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Mic Button (primary action)
            MicButton(
                isRecording = vocalAssistantViewModel.vocalAssistantUIState.value?.isRecording == true,
                onClick = onMicClick
            )
        }
    }
}

@Composable
private fun HeaderIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.15f else 1f, label = "scale")

    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(if (isFocused) BackgroundDarkHover else BackgroundDark)
            .then(
                if (isFocused) {
                    Modifier.border(
                        width = 2.dp,
                        color = AccentPurple,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyUp &&
                    (keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter)
                ) {
                    onClick()
                    true
                } else {
                    false
                }
            }
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isFocused) Color.White else TextMuted
        )
    }
}

@Composable
private fun MicButton(
    isRecording: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.15f else 1f, label = "scale")

    Box(
        modifier = modifier
            .scale(scale)
            .clip(CircleShape)
            .background(if (isRecording) RecordingRed else AccentIndigo)
            .then(
                if (isFocused) {
                    Modifier.border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                } else {
                    Modifier
                }
            )
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyUp &&
                    (keyEvent.key == Key.Enter || keyEvent.key == Key.DirectionCenter)
                ) {
                    onClick()
                    true
                } else {
                    false
                }
            }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isRecording) {
            TvCircularProgressIndicator()
        } else {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Rounded.Mic,
                contentDescription = "Start voice recording",
                tint = Color.White
            )
        }
    }
}
