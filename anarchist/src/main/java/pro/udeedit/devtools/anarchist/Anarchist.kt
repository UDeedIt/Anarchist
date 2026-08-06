package pro.udeedit.devtools.anarchist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import pro.udeedit.devtools.anarchist.internal.AnarchistPreference

/**
 * The primary entry point for the Anarchist Permissions Library.
 *
 * Provides a unified API to check and request permissions while
 * abstracting the complexity of rationale handling and permanent denials.
 */
object Anarchist {

    /**
     * Checks the status of multiple permissions and requests them if necessary.
     *
     * @param activity The host activity required for requesting permissions.
     * @param permissions The list of manifest permissions to verify.
     * @param requestCode Unique ID for this request.
     * @param checkStatusOnly If true, only checks state without showing system dialogs.
     * @return [AnarchistResult] containing the current state of all requested permissions.
     */
    fun checkAndRequestPermissions(
        activity: Activity,
        permissions: List<String>,
        requestCode: Int,
        checkStatusOnly: Boolean = false

    ): AnarchistResult {

        val prefs = AnarchistPreference(activity)
        val result = AnarchistResult()
        val statusMap = hashMapOf<String, AnarchistStatus>()

        permissions.forEach { permission ->
            if (isGranted(activity, permission)) {
                // Clear history if permission is now granted
                prefs.clearRequestedFlag(permission)
                statusMap[permission] = AnarchistStatus.ALLOWED

            } else {
                val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                val wasAskedBefore = prefs.isRequestedBefore(permission)

                statusMap[permission] = when {
                    // System says we should explain why we need it
                    shouldShowRationale -> AnarchistStatus.DENIED
                    // System won't show the dialog anymore
                    wasAskedBefore -> AnarchistStatus.DENIED_PERMANENTLY
                    // First time or standard denial
                    else -> AnarchistStatus.DENIED
                }
            }
        }

        result.details = statusMap

        // Logic to determine the final overall status of the group request
        result.finalStatus = when {
            statusMap.values.any { it == AnarchistStatus.DENIED_PERMANENTLY } -> AnarchistStatus.DENIED_PERMANENTLY
            statusMap.values.any { it == AnarchistStatus.DENIED } -> AnarchistStatus.DENIED
            else -> AnarchistStatus.ALLOWED
        }

        // Trigger system request if any are denied and not in check-only mode
        if (result.finalStatus == AnarchistStatus.DENIED && !checkStatusOnly) {
            val pendingList = statusMap.filter { it.value == AnarchistStatus.DENIED }.keys.toList()
            requestFromSystem(activity, pendingList, requestCode)
        }

        return result
    }

    /**
     * Utility to open the Application Details screen in system settings.
     *
     * This is intended to be used when the library returns [AnarchistStatus.DENIED_PERMANENTLY],
     * as the system will no longer show the standard permission dialog.
     *
     * @param context The context used to start the settings Activity.
     */
    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * Checks if a specific permission is currently granted by the system.
     *
     * Supporting Logic: Standard [ContextCompat.checkSelfPermission] only works
     * for dangerous permissions. This function is enhanced to route the check
     * to the appropriate system service based on the permission string
     * (e.g. AlarmManager for Exact Alarms).
     *
     * @param context The context used for the system check.
     * @param permission The manifest permission string to verify.
     * @return True if the permission is currently granted, false otherwise.
     */
    private fun isGranted(context: Context, permission: String): Boolean {
        return when (permission) {
            // Supporting Case: Exact Alarms (API 31+)
            "android.permission.SCHEDULE_EXACT_ALARM" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager
                    alarmManager.canScheduleExactAlarms()

                } else {
                    true // Implicitly allowed on older versions
                }
            }

            // Default Case: Standard dangerous permissions
            else -> {
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }


    /**
     * Triggers the standard Android system permission dialog for a list of permissions.
     *
     * This function also updates the internal persistence layer to mark
     * these permissions as "requested," which is vital for later detecting
     * the "Permanently Denied" state.
     *
     * @param activity The host activity that will receive the callback.
     * @param list The array of permissions to be requested from the user.
     * @param code The request code to identify this specific transaction.
     */
    private fun requestFromSystem(activity: Activity, list: List<String>, code: Int) {
        ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)

        // Track the request in our internal preferences
        val prefs = AnarchistPreference(activity)
        list.forEach { prefs.markAsRequested(it) }
    }

    /**
     * Resets the internal request history for a specified list of permissions.
     *
     * This function clears the "requestedBefore" flags from the
     * library's internal persistence layer. It is specifically designed to facilitate
     * thorough testing of the application's first-time permission request flows
     * without requiring a full application data wipe.
     *
     * Use cases for this function include:
     * 1. Implementing a "Reset Onboarding" or "Clear Permission Cache" feature.
     * 2. Automated UI testing where multiple "first ask" scenarios must be verified in sequence.
     * 3. Debugging rationale logic by simulating a fresh install state.
     *
     * @param context The context used to access the internal [AnarchistPreference] storage.
     * @param permissions The list of manifest permission strings to be reset in the history.
     */
    fun resetRequestHistory(context: Context, permissions: List<String>) {
        // Accesses the internal preference helper to modify the record
        val prefs = AnarchistPreference(context)

        // Iterates through the provided list and purges the 'asked before' flag for each
        permissions.forEach { permission ->
            prefs.clearRequestedFlag(permission)
        }
    }

    /**
     * Checks the internal preference layer to see if this permission
     * has been requested from the system before.
     *
     * @param context Context for shared preferences access.
     * @param permission The manifest permission string.
     * @return True if a record of a previous request exists.
     */
    fun wasAskedBefore(context: Context, permission: String): Boolean {
        return AnarchistPreference(context).isRequestedBefore(permission)
    }


    /**
     * Checks the system status of a 'Special' permission.
     *
     * D2D Supporting Logic: Unlike standard dangerous permissions, special
     * permissions (such as Exact Alarms or System Overlays) require unique
     * system service checks. This function provides a unified entry point
     * for these non-standard verifications.
     *
     * @param context The context used to access system services.
     * @param permission The manifest permission string to verify.
     * @return True if the permission is currently granted by the system, false otherwise.
     */
    fun isSpecialPermissionGranted(context: Context, permission: String): Boolean {
        return isGranted(context, permission)
    }

    /**
     * Specialized utility to open specific system settings pages for permissions
     * that require manual intervention (e.g., Exact Alarms).
     *
     * Supporting Logic: If a specialized intent fails or is not supported by the
     * current API level, the function falls back to the general App Info settings
     * to ensure the user is never left on a dead screen.
     *
     * @param context The context used to start the intent.
     * @param manifestString The specific permission string requiring the settings jump.
     */
    fun openSpecialSettings(context: Context, manifestString: String) {
        val intent = when (manifestString) {
            "android.permission.SCHEDULE_EXACT_ALARM" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                } else null
            }
            else -> null
        }

        try {
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } else {
                // Fallback for permissions without specific intent mapping
                openSettings(context)
            }
        } catch (e: Exception) {
            // Final safety fallback to ensure the button always performs an action
            openSettings(context)
        }
    }
}
