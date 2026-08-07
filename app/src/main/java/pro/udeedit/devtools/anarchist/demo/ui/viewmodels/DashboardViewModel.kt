package pro.udeedit.devtools.anarchist.demo.ui.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.SavedStateHandle
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
 * Supporting Logic: This architecture utilizes a 'Master List' pattern to ensure
 * Global Status Synchronization. By maintaining a single source of truth for all
 * permissions, the UI can reflect system changes across all tabs simultaneously.
 *
 * @param savedStateHandle System-provided handle to save and restore UI state.
 * @property currentScreen Reactive stream of the currently active navigation tab.
 * @property permissionFeatures Reactive stream of permissions filtered for the active tab.
 */
class DashboardViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    companion object {
        // Unique key for the navigation route in the saved state map
        private const val KEY_ACTIVE_TAB = "active_tab_route"
    }

    /**
     * MASTER LIST: The absolute source of truth for the application session.
     * Contains every permission feature from the registry with its current runtime status.
     */
    private val _allFeatures = MutableStateFlow<List<PermissionFeature>>(emptyList())


    // Tracks the currently selected navigation tab
    private val _currentScreen = MutableStateFlow<Screen>(
        savedStateHandle.get<String>(KEY_ACTIVE_TAB)?.let { savedRoute ->
            // Reconstruct the Screen object from the saved string route
            pro.udeedit.devtools.anarchist.demo.ui.navigation.navItems.find { it.route == savedRoute }
        } ?: Screen.Standard // Default if nothing was saved
    )

    /**
     * Publicly exposed state of the current navigation destination.
     */
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()


    // Internal reactive state container for the displayed permission list
    private val _permissionFeatures = MutableStateFlow<List<PermissionFeature>>(emptyList())

    /**
     * Publicly exposed state for Compose observation.
     */
    val permissionFeatures: StateFlow<List<PermissionFeature>> = _permissionFeatures.asStateFlow()


    init {
        /**
         * INITIALIZATION:
         * 1. Aggregates all categories from the PermissionRegistry into the master list.
         * 2. Populates the initial visible list for the default 'Standard' tab.
         */
        _allFeatures.value = PermissionRegistry.getStandardPermissions() +
                PermissionRegistry.getSpecialPermissions() +
                PermissionRegistry.getGroupedPermissions()

        updateVisibleList()
    }


    /**
     * Switches the dashboard content based on the selected navigation tab.
     *
     * @param screen The target navigation destination.
     */
    fun selectTab(screen: Screen) {
        _currentScreen.value = screen

        // 3. Save the route string so it survives process death
        savedStateHandle[KEY_ACTIVE_TAB] = screen.route

        // Triggers the filtering logic to update what the user sees
        updateVisibleList()
    }


    /**
     * Filters the Master List to determine which features should be rendered
     * in the current UI context.
     *
     * Supporting Logic: This replaces the old 'loadTab' function. Instead of
     * fetching static data from the Registry, it filters the Master List which
     * already contains the synchronized system statuses.
     */
    private fun updateVisibleList() {
        val currentRoute = _currentScreen.value.route

        _permissionFeatures.value = when (currentRoute) {
            Screen.Standard.route -> _allFeatures.value.filter { feature ->
                PermissionRegistry.getStandardPermissions().any { it.id == feature.id }
            }
            Screen.Special.route -> _allFeatures.value.filter { feature ->
                PermissionRegistry.getSpecialPermissions().any { it.id == feature.id }
            }
            else -> _allFeatures.value.filter { feature ->
                PermissionRegistry.getGroupedPermissions().any { it.id == feature.id }
            }
        }
    }


    /**
     * Synchronizes the status of EVERY permission in the project with the system state.
     *
     * Supporting Logic: By iterating through the Master List (_allFeatures) instead
     * of just the visible list, we ensure that if a user enables multiple permissions
     * while in the system settings, they are all updated regardless of the active tab.
     *
     * @param activity The host activity required for system status checks.
     */
    fun refreshStatuses(activity: Activity) {
        val updatedMasterList = _allFeatures.value.map { feature ->
            // Parse permissions (handles single strings or comma-separated bundles)
            val permissionsList = feature.manifestString.split(",").map { it.trim() }

            val result = Anarchist.checkAndRequestPermissions(
                activity = activity,
                permissions = permissionsList,
                requestCode = feature.id.hashCode().let { if (it < 0) -it else it } % 65536,
                checkStatusOnly = true
            )

            // Sync the 'wasAskedBefore' flag from the library persistence
            val asked = permissionsList.any { Anarchist.wasAskedBefore(activity, it) }

            feature.copy(
                currentStatus = result.finalStatus,
                wasAskedBefore = asked
            )
        }

        // Update the master list and immediately refresh the visible projection
        _allFeatures.value = updatedMasterList
        updateVisibleList()
    }


    /**
     * Triggers a system permission request or redirects to special settings.
     *
     * @param activity The host activity to handle the request result.
     * @param feature The specific [PermissionFeature] being interacted with.
     */
    fun requestPermission(activity: Activity, feature: PermissionFeature) {
        if (feature.isManualOnly) {
            Anarchist.openSpecialSettings(activity, feature.manifestString)
        } else {
            val permissionsList = feature.manifestString.split(",").map { it.trim() }
            val safeRequestCode = feature.id.hashCode().let { if (it < 0) -it else it } % 65536

            val result = Anarchist.checkAndRequestPermissions(
                activity = activity,
                permissions = permissionsList,
                requestCode = safeRequestCode,
                checkStatusOnly = false
            )

            // Update the master list directly
            updateFeatureInMasterList(feature.id, result.finalStatus, true)
        }
    }


    /**
     * Resets the internal request history for a specific feature.
     *
     * @param context Context required for preference modification.
     * @param feature The specific permission feature to reset.
     */
    fun revokePermission(context: Context, feature: PermissionFeature) {
        val permissionsList = feature.manifestString.split(",").map { it.trim() }

        // Reset the flag in the library's internal storage
        Anarchist.resetRequestHistory(context, permissionsList)

        // Force UI reset to Denied state in the master list
        updateFeatureInMasterList(feature.id, AnarchistStatus.DENIED, false)
    }


    /**
     * Internal helper to update a specific item in the Master List and sync the view.
     *
     * @param id The unique identifier of the feature to update.
     * @param status The new [AnarchistStatus] to apply.
     * @param wasAsked The new value for the 'Asked Before' flag.
     */
    private fun updateFeatureInMasterList(id: String, status: AnarchistStatus, wasAsked: Boolean) {
        _allFeatures.value = _allFeatures.value.map {
            if (it.id == id) {
                it.copy(currentStatus = status, wasAskedBefore = wasAsked)
            } else {
                it
            }
        }

        // Ensure the change is reflected in the current tab
        updateVisibleList()
    }


    /**
     * Resets the request history for every permission globally across all categories.
     *
     * @param context Context required to access internal library storage.
     */
    fun resetAllHistory(context: Context) {
        // Collect every permission string from the current master list
        val flatList = _allFeatures.value
            .flatMap { it.manifestString.split(",") }
            .map { it.trim() }
            .distinct()

        Anarchist.resetRequestHistory(context, flatList)

        // Reset the master list objects to their initial state
        _allFeatures.value = _allFeatures.value.map {
            it.copy(currentStatus = AnarchistStatus.DENIED, wasAskedBefore = false)
        }

        updateVisibleList()
    }
}
