package pro.udeedit.devtools.anarchist.demo.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pro.udeedit.devtools.anarchist.Anarchist
import pro.udeedit.devtools.anarchist.demo.R
import pro.udeedit.devtools.anarchist.demo.data.mocks.PreviewMocks
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature
import pro.udeedit.devtools.anarchist.demo.ui.components.PermissionCard
import pro.udeedit.devtools.anarchist.demo.ui.navigation.Screen
import pro.udeedit.devtools.anarchist.demo.ui.navigation.navItems
import pro.udeedit.devtools.anarchist.demo.ui.viewmodels.DashboardViewModel

/**
 * Encapsulates all user interaction callbacks for the Anarchist Dashboard.
 *
 * This container follows the 'Command' pattern to simplify Composable signatures
 * and improve code maintainability.
 */
data class DashboardActions(
    val onTabSelected: (Screen) -> Unit,
    val onRequest: (PermissionFeature) -> Unit,
    val onOpenSettings: (PermissionFeature) -> Unit,
    val onRevoke: (PermissionFeature) -> Unit,
    val onResetAll: () -> Unit,
)

/**
 * Main Activity for the Anarchist Demo.
 *
 * Serves as the primary entry point for demonstrating the Anarchist library
 * within a reactive, multi-tabbed dashboard environment.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Applying the project-standard theme wrapper
            AnarchistDemoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    // Initializing the primary dashboard orchestration
                    AnarchistDashboard()
                }
            }
        }
    }
}

/**
 * STATEFUL DASHBOARD SCREEN:
 *
 * This component acts as the high-level orchestrator. It manages the connection
 * to the [DashboardViewModel], manages the visibility of global dialogs, and
 * monitors the Activity lifecycle to ensure real-time status synchronization.
 */
@Composable
fun AnarchistDashboard(viewModel: DashboardViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as Activity

    // Obtains the LifecycleOwner to register the foreground status observer
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    // Observers for the reactive states managed by the ViewModel
    val permissions by viewModel.permissionFeatures.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()

    // State to control the visibility of the global Reset Confirmation Dialog
    var showResetDialog by remember { mutableStateOf(false) }

    /**
     * INITIAL SYNCHRONIZATION:
     * Triggers a status refresh when the dashboard first enters the composition.
     */
    LaunchedEffect(Unit) {
        viewModel.refreshStatuses(activity)
    }

    /**
     * Lifecycle Resumption Sync
     * Attaches an observer to refresh permission statuses every time the user
     * returns to the application from system screens (settings or dialogs).
     */
    DisposableEffect(lifecycleOwner) {
        val observer =
            androidx.lifecycle.LifecycleEventObserver { _, event ->
                if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                    // Perform a non-intrusive status scan
                    viewModel.refreshStatuses(activity)
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /**
     * RESET CONFIRMATION DIALOG:
     * Logic for the global data reset. This ensures the library's internal
     * persistence is cleared across all permission categories.
     */
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(text = stringResource(id = R.string.reset_dialog_title)) },
            text = { Text(text = stringResource(id = R.string.reset_dialog_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.resetAllHistory(context)
                    showResetDialog = false
                }) {
                    Text(
                        text = stringResource(id = R.string.reset_confirm),
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(text = stringResource(id = R.string.reset_cancel))
                }
            },
        )
    }

    // Supporting Logic: Creating the actions container to reduce parameter count
    val actions =
        remember(viewModel, activity, context) {
            DashboardActions(
                onTabSelected = { viewModel.selectTab(it) },
                onRequest = { viewModel.requestPermission(activity, it) },
                onOpenSettings = { feature ->
                    Anarchist.openSpecialSettings(context, feature.manifestString)
                },
                onRevoke = { viewModel.revokePermission(context, it) },
                onResetAll = { showResetDialog = true },
            )
        }

    // Delegation to the stateless content renderer
    DashboardContent(
        title = stringResource(id = R.string.dashboard_title),
        permissions = permissions,
        currentScreen = currentScreen,
        actions = actions,
    )
}

/**
 * STATELESS DASHBOARD CONTENT:
 *
 * A pure UI component responsible for rendering the dashboard layout.
 * By decoupling the UI from the ViewModel, this component becomes highly
 * testable and supports efficient IDE Previews with mock data.
 *
 * @param title The text displayed in the Top App Bar.
 * @param permissions The list of [PermissionFeature] objects to render.
 * @param currentScreen The currently active navigation tab.
 * @param actions The container for all user interaction callbacks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    title: String,
    permissions: List<PermissionFeature>,
    currentScreen: Screen,
    actions: DashboardActions,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = title) },
                actions = {
                    /**
                     * SUPPORTING UTILITY: Global Reset
                     * The 🏴‍☠️ icon triggers the clearing of all internal permission
                     * request history, allowing for a fresh start of the demo.
                     */
                    IconButton(
                        onClick = actions.onResetAll,
                        // Enables Appium to trigger a global reset
                        modifier = Modifier.testTag("btn_global_reset"),
                    ) {
                        Text(
                            text = "🏴‍☠️",
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        },
        bottomBar = {
            /**
             * CATEGORIZED NAVIGATION:
             * Displays the functional tabs (Standard, Special, Bundles).
             */
            NavigationBar {
                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(id = screen.contentDescriptionRes),
                            )
                        },
                        label = { Text(text = stringResource(id = screen.titleRes)) },
                        selected = currentScreen == screen,
                        onClick = { actions.onTabSelected(screen) },
                        // Enables Appium to select tabs by ID
                        modifier = Modifier.testTag("tab_${screen.route}"),
                    )
                }
            }
        },
    ) { padding ->

        /**
         * REACTIVE PERMISSION LIST:
         * Efficiently renders a list of cards based on the active category.
         */
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(permissions) { feature ->

                PermissionCard(
                    feature = feature,
                    onRequest = { actions.onRequest(feature) },
                    // Passing the feature down to the component
                    onOpenSettings = { actions.onOpenSettings(feature) },
                    onRevoke = { actions.onRevoke(feature) },
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

// --- DASHBOARD PREVIEWS ---

@Preview(showBackground = true, name = "Standard Tab Preview")
@Composable
fun PreviewStandardTab() {
    AnarchistDemoTheme {
        DashboardContent(
            title = "Anarchist Dashboard",
            permissions = PreviewMocks.mockStandardList,
            currentScreen = Screen.Standard,
            actions = DashboardActions({}, {}, {}, {}, {}),
        )
    }
}

@Preview(showBackground = true, name = "Special Tab Preview")
@Composable
fun PreviewSpecialTab() {
    AnarchistDemoTheme {
        DashboardContent(
            title = "Anarchist Dashboard",
            permissions = PreviewMocks.mockSpecialList,
            currentScreen = Screen.Special,
            actions = DashboardActions({}, {}, {}, {}, {}),
        )
    }
}

@Preview(showBackground = true, name = "Bundles Tab Preview")
@Composable
fun PreviewBundlesTab() {
    AnarchistDemoTheme {
        DashboardContent(
            title = "Anarchist Dashboard",
            permissions = PreviewMocks.mockBundlesList,
            currentScreen = Screen.Bundles,
            actions = DashboardActions({}, {}, {}, {}, {}),
        )
    }
}
