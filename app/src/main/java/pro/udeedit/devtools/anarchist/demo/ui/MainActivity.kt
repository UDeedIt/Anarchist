package pro.udeedit.devtools.anarchist.demo.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pro.udeedit.devtools.anarchist.Anarchist
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.ui.components.PermissionCard
import pro.udeedit.devtools.anarchist.demo.ui.viewmodels.DashboardViewModel

/**
 * Main Activity for the Anarchist Demo.
 *
 * This class serves as the host for the scalable permission dashboard,
 * demonstrating reactive permission management in a modern Compose environment.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Applying the custom theme wrapper for the demo app
            AnarchistDemoTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initializing the primary dashboard orchestration
                    AnarchistDashboard()
                }
            }
        }
    }
}

/**
 * The primary Dashboard UI that orchestrates the display and interaction
 * of all permission features defined in the registry.
 *
 * @param viewModel The state holder for the permission dashboard logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnarchistDashboard(
    viewModel: DashboardViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as Activity

    // Monitors the foreground/background state of the Activity
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Observers the reactive StateFlow containing the permission features
    val permissions by viewModel.permissionFeatures.collectAsState()

    /**
     * D2D SUPPORTING LOGIC: Lifecycle Synchronization
     * Attaches an observer to refresh permission statuses every time the user
     * returns to the app from the system settings or a permission dialog.
     */
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                // Silently refresh the list to catch any manual system changes
                viewModel.refreshStatuses(activity)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /**
     * INITIAL SYNCHRONIZATION:
     * Triggers a status check for all registered permissions on startup.
     */
    LaunchedEffect(Unit) {
        viewModel.refreshStatuses(activity)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Anarchist Dashboard 🏴‍☠️") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { padding ->

        /**
         * SCALABLE LIST:
         * Uses a LazyColumn to efficiently render the permission registry.
         */
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {

            // Map the permissions list into interactive cards
            items(permissions) { feature ->

                PermissionCard(
                    feature = feature,

                    // Logic for standard system request
                    onRequest = {
                        viewModel.requestPermission(activity, feature)
                    },

                    // Path for manual recovery in system settings
                    onOpenSettings = {
                        Anarchist.openSettings(context)
                    },

                    // Supporting utility for resetting the request history
                    onRevoke = {
                        viewModel.revokePermission(context, feature)
                    }
                )
            }
        }
    }
}


/**
 * Basic Theme wrapper for the Demo components.
 */
@Composable
fun AnarchistDemoTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}


// --- PREVIEWS ---

/**
 * Mock data used to verify the Dashboard layout in the IDE Preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, name = "Full Dashboard Preview")
@Composable
fun DashboardPreview() {
    AnarchistDemoTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(title = { Text("Anarchist Dashboard 🏴‍☠️") })
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                // Example 1: Standard Denied state
                PermissionCard(
                    feature = pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature(
                        id = "PREVIEW_1",
                        title = "Camera Access",
                        manifestString = "",
                        icon = Icons.Default.PhotoCamera,
                        description = "Standard access to hardware camera sensors.",
                        apiRange = "API 23+",
                        manifestTags = emptyList(),
                        rationaleLong = "",
                        currentStatus = AnarchistStatus.DENIED
                    ),
                    onRequest = {},
                    onOpenSettings = {},
                    onRevoke = {}
                )

                // Example 2: Success (Allowed) state
                PermissionCard(
                    feature = pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature(
                        id = "PREVIEW_2",
                        title = "Notifications",
                        manifestString = "",
                        icon = Icons.Default.Notifications,
                        description = "Ability to show push notifications.",
                        apiRange = "API 33+",
                        manifestTags = emptyList(),
                        rationaleLong = "",
                        currentStatus = AnarchistStatus.ALLOWED
                    ),
                    onRequest = {},
                    onOpenSettings = {},
                    onRevoke = {}
                )
            }
        }
    }
}
