package pro.udeedit.devtools.anarchist.demo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.data.actions.PermissionActionExecutor
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature
import pro.udeedit.devtools.anarchist.demo.ui.AnarchistDemoTheme
import pro.udeedit.devtools.anarchist.demo.ui.theme.SuccessGreen

/**
 * A reactive card component that displays the status and management options
 * for a single [PermissionFeature].
 *
 * This component acts as the primary interactive element of the Anarchist Dashboard,
 * dynamically adapting its visual state and available actions based on the
 * current system permission status.
 *
 * @param feature The permission data and current status to display.
 * @param onRequest Callback to trigger the unified system permission request.
 * @param onOpenSettings Callback to direct the user to the application system settings.
 * @param onRevoke Callback to reset the internal request history for testing purposes.
 */
@Composable
fun PermissionCard(
    feature: PermissionFeature,
    onRequest: () -> Unit,
    onOpenSettings: () -> Unit,
    onRevoke: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    // State to manage the visibility of the D2D Supporting Information Dialog
    var showInfoDialog by remember { mutableStateOf(false) }

    // --- D2D SUPPORTING INFORMATION DIALOG ---
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(feature.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text(feature.title)
                }
            },
            text = {
                Column {
                    Text("Technical Metadata", style = MaterialTheme.typography.titleSmall)
                    Text("API Range: ${feature.apiRange}", style = MaterialTheme.typography.bodySmall)

                    Spacer(Modifier.height(16.dp))

                    Text("Manifest Requirements", style = MaterialTheme.typography.titleSmall)
                    // Iterating through required manifest tags for developer reference
                    feature.manifestTags.forEach { tag ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Text("Supporting Rationale", style = MaterialTheme.typography.titleSmall)
                    Text(feature.rationaleLong, style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) { Text("Close") }
            }
        )
    }

    // --- CARD UI ---
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            // Using a semi-transparent surface variant for a modern 'Cushy' feel
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- HEADER ROW ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Visual branding: Icon and Title
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = feature.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                // D2D Supporting Info Button
                IconButton(onClick = { showInfoDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Technical implementation details",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short functional description of the permission
            Text(
                text = feature.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 4.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- STATUS INFORMATION BLOCK ---
            Text(
                text = getStatusDescription(feature.currentStatus, feature.wasAskedBefore),
                // Increased font size for better technical readability
                style = MaterialTheme.typography.bodySmall,
                color = when (feature.currentStatus) {
                    // Success Green
                    AnarchistStatus.ALLOWED -> SuccessGreen
                    // Error Red
                    AnarchistStatus.DENIED_PERMANENTLY -> MaterialTheme.colorScheme.error
                    // Default Info Blue
                    else -> MaterialTheme.colorScheme.primary
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- ACTION CONTROLS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /**
                 * D2D Supporting Logic:
                 * We ONLY show "Clear History" if the library has a record of asking
                 * for this permission. On a fresh start, this button is hidden.
                 */
                if (feature.wasAskedBefore) {
                    TextButton(
                        onClick = onRevoke,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Clear History")
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }

                /**
                 * DYNAMIC STATE LOGIC:
                 * Swaps the primary action button based on the AnarchistStatus.
                 * This ensures the user is always presented with the correct next step.
                 */
                Box {
                    when (feature.currentStatus) {
                        // Case: Permission is already granted
                        AnarchistStatus.ALLOWED -> {
                            Button(
                                onClick = {
                                    // Dispatch action to the centralized executor
                                    PermissionActionExecutor.performAction(
                                        context,
                                        feature.id
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                            ) {
                                Text("Test Feature")
                            }
                        }

                        // Case: Permission blocked by 'Don't ask again'
                        AnarchistStatus.DENIED_PERMANENTLY -> {
                            Button(
                                onClick = onOpenSettings,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error // Red for blocked
                                )
                            ) {
                                Text("Open Settings")
                            }
                        }

                        // Case: Standard Denied state or first-time launch
                        else -> {
                            Button(
                                onClick = onRequest,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Request")
                            }
                        }
                    }
                }
            }
        }
    }
}


/**
 * Returns a technical description based on the current permission state.
 */
private fun getStatusDescription(status: AnarchistStatus, wasAsked: Boolean): String {
    return when {
        status == AnarchistStatus.ALLOWED -> "System Status: GRANTED. Feature is unlocked."
        status == AnarchistStatus.DENIED_PERMANENTLY -> "System Status: PERMANENTLY DENIED. User must manually enable in settings."
        wasAsked && status == AnarchistStatus.DENIED -> "System Status: DENIED (Rationale). System allows asking again with an explanation."
        else -> "System Status: UNKNOWN / NOT REQUESTED. Ready for first attempt."
    }
}


// --- PREVIEWS ---

@Preview(showBackground = true, name = "State 1: Initial (Not Requested)")
@Composable
fun PreviewCardInitial() {
    AnarchistDemoTheme {
        PermissionCard(
            feature = PermissionFeature(
                id = "PREVIEW_1",
                title = "Camera Access",
                manifestString = "",
                icon = Icons.Default.PhotoCamera,
                description = "Standard access to hardware camera sensors.",
                apiRange = "API 23+",
                manifestTags = emptyList(),
                rationaleLong = "",
                currentStatus = AnarchistStatus.DENIED,
                wasAskedBefore = false
            ),
            onRequest = {},
            onOpenSettings = {},
            onRevoke = {}
        )
    }
}

@Preview(showBackground = true, name = "State 2: Rationale (Denied Once)")
@Composable
fun PreviewCardRationale() {
    AnarchistDemoTheme {
        PermissionCard(
            feature = PermissionFeature(
                id = "PREVIEW_2",
                title = "Notifications",
                manifestString = "",
                icon = Icons.Default.Notifications,
                description = "Ability to show push notifications.",
                apiRange = "API 33+",
                manifestTags = emptyList(),
                rationaleLong = "",
                currentStatus = AnarchistStatus.DENIED,
                wasAskedBefore = true
            ),
            onRequest = {},
            onOpenSettings = {},
            onRevoke = {}
        )
    }
}

@Preview(showBackground = true, name = "State 3: Blocked (Permanently Denied)")
@Composable
fun PreviewCardBlocked() {
    AnarchistDemoTheme {
        PermissionCard(
            feature = PermissionFeature(
                id = "PREVIEW_3",
                title = "Location Access",
                manifestString = "",
                icon = Icons.Default.LocationOn,
                description = "Required for navigation features.",
                apiRange = "API 23+",
                manifestTags = emptyList(),
                rationaleLong = "",
                currentStatus = AnarchistStatus.DENIED_PERMANENTLY,
                wasAskedBefore = true
            ),
            onRequest = {},
            onOpenSettings = {},
            onRevoke = {}
        )
    }
}

@Preview(showBackground = true, name = "State 4: Allowed (Success)")
@Composable
fun PreviewCardAllowed() {
    AnarchistDemoTheme {
        PermissionCard(
            feature = PermissionFeature(
                id = "PREVIEW_4",
                title = "Contacts",
                manifestString = "",
                icon = Icons.Default.Info,
                description = "Access to device contacts.",
                apiRange = "API 23+",
                manifestTags = emptyList(),
                rationaleLong = "Granted.",
                currentStatus = AnarchistStatus.ALLOWED,
                wasAskedBefore = true
            ),
            onRequest = {},
            onOpenSettings = {},
            onRevoke = {}
        )
    }
}
