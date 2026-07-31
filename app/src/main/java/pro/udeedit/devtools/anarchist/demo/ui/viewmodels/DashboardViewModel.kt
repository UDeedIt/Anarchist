package pro.udeedit.devtools.anarchist.demo.ui.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pro.udeedit.devtools.anarchist.Anarchist
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature
import pro.udeedit.devtools.anarchist.demo.data.registry.PermissionRegistry
import pro.udeedit.devtools.anarchist.demo.ui.navigation.Screen

/**
 * ViewModel responsible for orchestrating the multi-tab Anarchist Dashboard.
 *
 * It manages the reactive state of permission features across Standard, Special,
 * and Bundled categories, ensuring real-time status synchronization with the Android system.
 */
class DashboardViewModel : ViewModel() {

    // Tracks the currently selected navigation tab
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Standard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Internal reactive state container for the displayed permission list
    private val _permissionFeatures = MutableStateFlow<List<PermissionFeature>>(emptyList())

    /**
     * Publicly exposed state for Compose observation.
     */
    val permissionFeatures: StateFlow<List<PermissionFeature>> = _permissionFeatures.asStateFlow()


    init {
        // Default initialization with standard permissions
        loadTab(Screen.Standard)
    }


    /**
     * Switches the dashboard content based on the selected navigation tab.
     *
     * @param screen The target destination (Standard, Special, or Bundles).
     */
    fun selectTab(screen: Screen) {
        _currentScreen.value = screen
        loadTab(screen)
    }


    /**
     * Loads the specific permission set from the registry based on the provided screen.
     */
    private fun loadTab(screen: Screen) {
        _permissionFeatures.value = when (screen) {
            Screen.Standard -> PermissionRegistry.getStandardPermissions()
            Screen.Special -> PermissionRegistry.getSpecialPermissions()
            Screen.Bundles -> PermissionRegistry.getGroupedPermissions()
        }
    }


    /**
     * Synchronizes the status of all visible permissions with the system state.
     *
     * Supporting Logic: This is triggered on lifecycle resumptions to ensure the UI
     * reflects manual changes made by the user in system settings.
     *
     * @param activity The host activity required for system status checks.
     */
    fun refreshStatuses(activity: Activity) {
        val updatedList = _permissionFeatures.value.map { feature ->
            // Parse permissions (handles single strings or comma-separated bundles)
            val permissionsList = feature.manifestString.split(",").map { it.trim() }

            val result = Anarchist.checkAndRequestPermissions(
                activity = activity,
                permissions = permissionsList,
                requestCode = feature.id.hashCode(),
                checkStatusOnly = true
            )

            // Check if at least one permission in the feature has been asked before
            val asked = permissionsList.any { Anarchist.wasAskedBefore(activity, it) }

            feature.copy(
                currentStatus = result.finalStatus,
                wasAskedBefore = asked
            )
        }
        _permissionFeatures.value = updatedList
    }


    /**
     * Triggers a system permission request or intent-based settings navigation.
     */
    fun requestPermission(activity: Activity, feature: PermissionFeature) {
        val permissionsList = feature.manifestString.split(",").map { it.trim() }

        val result = Anarchist.checkAndRequestPermissions(
            activity = activity,
            permissions = permissionsList,
            requestCode = feature.id.hashCode(),
            checkStatusOnly = false
        )

        updateFeatureInList(feature.id, result.finalStatus, true)
    }


    /**
     * Resets the library's internal request history for a specific feature.
     *
     * Supporting Utility: Clears the 'requestedBefore' flag for all permissions
     * associated with this feature to allow testing first-run scenarios.
     */
    fun revokePermission(context: Context, feature: PermissionFeature) {
        val permissionsList = feature.manifestString.split(",").map { it.trim() }

        Anarchist.resetRequestHistory(context, permissionsList)

        // Force UI reset to Denied state with no history
        updateFeatureInList(feature.id, AnarchistStatus.DENIED, false)
    }


    /**
     * Internal helper to perform a targeted update on a specific list item.
     */
    private fun updateFeatureInList(id: String, status: AnarchistStatus, wasAsked: Boolean) {
        _permissionFeatures.value = _permissionFeatures.value.map {
            if (it.id == id) {
                it.copy(currentStatus = status, wasAskedBefore = wasAsked)
            } else {
                it
            }
        }
    }

    /**
     * Resets the request history for every permission defined in the registry.
     *
     * D2D Supporting Utility: Clears the 'asked before' flag globally,
     * restoring the application to a fresh-install state for all categories.
     */
    fun resetAllHistory(context: Context) {
        // Collect all manifest strings from all registry categories
        val allPermissions = PermissionRegistry.getStandardPermissions().map { it.manifestString } +
                PermissionRegistry.getSpecialPermissions().map { it.manifestString } +
                PermissionRegistry.getGroupedPermissions().map { it.manifestString }

        // Split bundles and flatten into a single list of unique permissions
        val flatList = allPermissions.flatMap { it.split(",") }.map { it.trim() }.distinct()

        // Trigger the library reset
        Anarchist.resetRequestHistory(context, flatList)

        // Refresh the current UI state immediately
        // Note: activity check is omitted here as we only need context for reset
        loadTab(_currentScreen.value)
    }
}
