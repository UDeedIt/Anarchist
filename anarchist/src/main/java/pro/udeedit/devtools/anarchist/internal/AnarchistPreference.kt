package pro.udeedit.devtools.anarchist.internal

import android.content.Context
import androidx.core.content.edit

/**
 * Internal helper to track the history of permission requests.
 *
 * This is essential for distinguishing between a standard "Denied" state
 * and a "Permanently Denied" state where the system dialog will no longer appear.
 */
internal class AnarchistPreference(context: Context) {

    companion object {
        private const val PREF_FILE = "pro.udeedit.devtools.anarchist.prefs"
    }

    private val sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    /**
     * Checks if a specific permission has been requested from the user before.
     */
    fun isRequestedBefore(permission: String): Boolean {
        return sharedPrefs.getBoolean(permission, false)
    }

    /**
     * Marks a permission as having been requested at least once.
     */
    fun markAsRequested(permission: String) {
        sharedPrefs.edit { putBoolean(permission, true) }
    }

    /**
     * Clears the requested flag (used if permission is granted later).
     */
    fun clearRequestedFlag(permission: String) {
        sharedPrefs.edit { putBoolean(permission, false) }
    }
}
