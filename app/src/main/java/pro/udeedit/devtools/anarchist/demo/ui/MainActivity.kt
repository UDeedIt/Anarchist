package pro.udeedit.devtools.anarchist.demo

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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AnarchistDemoTheme {
                // Provides a coroutine scope tied to this Composable's lifecycle
                val scope = rememberCoroutineScope()

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

                        statusText = "Notification Status: ${result.finalStatus}"

                    } else {
                        statusText = "Notifications auto-allowed (Pre-Tiramisu)"
                    }
                }

                PermissionStatusScreen(
                    statusText,
                    onRequestPermission = {
                        scope.launch {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                val result = Anarchist.checkAndRequestPermissions(
                                    activity = this@MainActivity,
                                    permissions = listOf(Manifest.permission.POST_NOTIFICATIONS),
                                    requestCode = 1001,
                                    checkStatusOnly = false // Now we WANT the dialog to show
                                )

                                // Update UI after the user interacts with the system dialog
                                statusText = "Notification Status: ${result.finalStatus}"
                            }
                        }
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
    onRequestPermission: () -> Unit
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
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // The button to trigger the Anarchist logic
            Button(onClick = onRequestPermission) {
                Text("Request Permission")
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
            "Notification Status: DENIED",
            onRequestPermission = { /* Do nothing in preview */ }
        )
    }
}

@Preview(showBackground = true, name = "Notification Status - Allowed")
@Composable
fun DemoPreviewAllowed() {
    AnarchistDemoTheme {
        PermissionStatusScreen(
            "Notification Status: ALLOWED",
            onRequestPermission = { /* Do nothing in preview */ }
        )
    }
}