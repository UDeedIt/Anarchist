package pro.udeedit.devtools.anarchist.demo.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pro.udeedit.devtools.anarchist.Anarchist
import pro.udeedit.devtools.anarchist.AnarchistStatus

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnarchistDemoTheme {
                // Provides a coroutine scope tied to this Composable's lifecycle
                val scope = rememberCoroutineScope()

                // Track the full result object to handle both status and settings button
                var permissionStatus by remember { mutableStateOf(AnarchistStatus.DENIED) }

                // State to track the permission status in the UI
                var statusText by remember { mutableStateOf("Checking permissions...") }

                // Trigger the check once the Activity is ready
                LaunchedEffect(Unit) {
                    /**
                     * For API 33+, check Notifications. For older, assume Allowed for this permission.
                     */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val result = Anarchist.checkAndRequestPermissions(
                            activity = this@MainActivity,
                            permissions = listOf(Manifest.permission.POST_NOTIFICATIONS),
                            requestCode = 1001,
                            checkStatusOnly = true
                        )

                        permissionStatus = result.finalStatus
                        statusText = "Notification Status: ${result.finalStatus}"

                    } else {
                        permissionStatus = AnarchistStatus.ALLOWED
                        statusText = "Notifications auto-allowed (Pre-Tiramisu)"
                    }
                }

                PermissionStatusScreen(
                    statusText,
                    currentStatus = permissionStatus,
                    onRequestPermission = {
                        scope.launch {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val result = Anarchist.checkAndRequestPermissions(
                                    activity = this@MainActivity,
                                    permissions = listOf(Manifest.permission.POST_NOTIFICATIONS),
                                    requestCode = 1001,
                                    checkStatusOnly = false // Now we WANT the dialog to show
                                )

                                // Update the status state
                                permissionStatus = result.finalStatus

                                // Update UI after the user interacts with the system dialog
                                statusText = "Notification Status: ${result.finalStatus}"
                            }
                        }
                    },
                    onOpenSettings = {
                        // Utilizing the Anarchist utility to open system settings
                        Anarchist.openSettings(this)
                    }
                )
            }
        }
    }
}

/**
 * Reusable UI component for the demo.
 */
@Composable
fun PermissionStatusScreen(
    status: String,
    currentStatus: AnarchistStatus,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Anarchist Library Demo 🏴‍☠️",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = status,
                style = MaterialTheme.typography.bodyLarge,
                color = if (currentStatus == AnarchistStatus.ALLOWED)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(32.dp))

            /**
             * The "Anarchist" Logic:
             * If the status is PERMANENTLY DENIED, we must send the user to settings.
             * Otherwise, we show the standard request button.
             */
            if (currentStatus == AnarchistStatus.DENIED_PERMANENTLY) {
                Button(
                    onClick = onOpenSettings,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Open System Settings")
                }
            } else if (currentStatus == AnarchistStatus.DENIED) {
                Button(onClick = onRequestPermission) {
                    Text("Request Permission")
                }
            }
        }
    }
}


/**
 * Basic Theme for the Demo.
 */
@Composable
fun AnarchistDemoTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}


// --- PREVIEWS ---

@Preview(showBackground = true, name = "Notification Status - Denied")
@Composable
fun DemoPreviewDenied() {
    AnarchistDemoTheme {
        PermissionStatusScreen(
            status = "Notification Status: DENIED_PERMANENTLY",
            currentStatus = AnarchistStatus.DENIED_PERMANENTLY,
            onRequestPermission = {},
            onOpenSettings = {}
        )
    }
}

@Preview(showBackground = true, name = "Notification Status - Allowed")
@Composable
fun DemoPreviewAllowed() {
    AnarchistDemoTheme {
        PermissionStatusScreen(
            "Notification Status: ALLOWED",
            currentStatus = AnarchistStatus.ALLOWED,
            onRequestPermission = { /* Do nothing in preview */ },
            onOpenSettings = {}
        )
    }
}