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

/**
 * ViewModel responsible for managing the state and orchestration of the Anarchist Dashboard.
 */
class DashboardViewModel : ViewModel() {

    // Internal reactive state container
    private val _permissionFeatures = MutableStateFlow<List<PermissionFeature>>(emptyList())

    /**
     * Publicly exposed state for Compose observation.
     */
    val permissionFeatures: StateFlow<List<PermissionFeature>> = _permissionFeatures.asStateFlow()


    init {
        // Populate the initial dashboard data
        loadPermissions()
    }


    /**
     * Initializes the dashboard list from the central PermissionRegistry.
     */
    private fun loadPermissions() {
        _permissionFeatures.value = PermissionRegistry.getStandardPermissions()
    }


    /**
     * Performs a non-intrusive status check for all registered permissions.
     *
     * D2D Logic: This is called on ON_RESUME to ensure the UI matches the
     * system state after a user returns from settings or dialogs.
     */
    fun refreshStatuses(activity: Activity) {
        val updatedList = _permissionFeatures.value.map { feature ->
            val result = Anarchist.checkAndRequestPermissions(
                activity = activity,
                permissions = listOf(feature.manifestString),
                requestCode = feature.id.hashCode(),
                checkStatusOnly = true
            )

            // Fetch the 'Asked' state from the library for this specific permission
            val asked = Anarchist.wasAskedBefore(activity, feature.manifestString)

            feature.copy(
                currentStatus = result.finalStatus,
                wasAskedBefore = asked // Update the UI flag
            )
        }
        _permissionFeatures.value = updatedList
    }



    /**
     * Triggers a standard system permission request for a specific feature.
     */
    fun requestPermission(activity: Activity, feature: PermissionFeature) {
        val result = Anarchist.checkAndRequestPermissions(
            activity = activity,
            permissions = listOf(feature.manifestString),
            requestCode = feature.id.hashCode(),
            checkStatusOnly = false // Triggers the system dialog
        )

        // Update the list immediately based on the user's action
        updateFeatureStatus(feature.id, result.finalStatus)
    }


    /**
     * Resets the internal request history for a specific permission.
     *
     * D2D Supporting Utility: This clears the 'asked before' flag in the
     * library's persistence layer, allowing the developer to see the
     * system dialog again as if it were a first-time install.
     *
     * @param context Context required for preference modification.
     * @param feature The specific permission feature to reset.
     */
    fun revokePermission(context: Context, feature: PermissionFeature) {
        // Use the library utility to clear the history
        Anarchist.resetRequestHistory(context, listOf(feature.manifestString))

        // Update the UI state list immediately
        _permissionFeatures.value = _permissionFeatures.value.map {
            if (it.id == feature.id) {
                it.copy(
                    currentStatus = AnarchistStatus.DENIED,
                    // CRUCIAL FIX: Set this to false so the button vanishes
                    wasAskedBefore = false
                )
            } else {
                it
            }
        }
    }


    /**
     * Internal helper to update a specific item in the state list.
     */
    private fun updateFeatureStatus(id: String, newStatus: AnarchistStatus) {
        _permissionFeatures.value = _permissionFeatures.value.map {
            if (it.id == id) it.copy(currentStatus = newStatus) else it
        }
    }
}
