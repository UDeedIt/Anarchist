package pro.udeedit.devtools.anarchist.demo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

/**
 * A reactive card component that displays the status and management options
 * for a single [PermissionFeature].
 *
 * @param feature The permission data to display.
 * @param onRequest Callback to trigger the permission request logic.
 * @param onOpenSettings Callback to open the system settings.
 */
@Composable
fun PermissionCard(
    feature: PermissionFeature,
    onRequest: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header with Icon and Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(feature.icon, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = feature.title, style = MaterialTheme.typography.titleMedium)
                }

                // Info Button (Triggers the D2D Information Dialog)
                IconButton(onClick = { /* TODO: Show Info Dialog */ }) {
                    Icon(Icons.Default.Info, contentDescription = "Technical Info")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = feature.description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Action Button based on AnarchistStatus
            Box(modifier = Modifier.align(Alignment.End)) {
                when (feature.currentStatus) {
                    AnarchistStatus.ALLOWED -> {
                        Button(
                            onClick = { /* TODO: Run feature.actionIfAllowed */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Test Feature")
                        }
                    }
                    AnarchistStatus.DENIED_PERMANENTLY -> {
                        Button(
                            onClick = onOpenSettings,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("Open Settings")
                        }
                    }
                    else -> {
                        Button(onClick = onRequest) {
                            Text("Request")
                        }
                    }
                }
            }
        }
    }
}
