package pro.udeedit.devtools.anarchist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
     * @param context The context used for the system check.
     * @param permission The manifest permission string to verify.
     * @return True if [PackageManager.PERMISSION_GRANTED] is returned, false otherwise.
     */
    private fun isGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
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
}
