package pro.udeedit.devtools.anarchist.internal

import android.content.Context
import androidx.core.content.edit

/**
 * Internal helper to track the history of permission requests.
 *
 * Distinguishing between 'Denied' and 'Permanently Denied' states is
 * not natively supported by the Android framework. This persistence layer
 * allows Anarchist to track if a permission was requested before.
 */
internal class AnarchistPreference(context: Context) {

    companion object {
        private const val PREF_FILE = "pro.udeedit.devtools.anarchist.prefs"
    }

    private val sharedPrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    /**
     * Checks if a specific permission has been requested from the user before.
     *
     * @param permission The manifest permission string.
     */
    fun isRequestedBefore(permission: String): Boolean {
        return sharedPrefs.getBoolean(permission, false)
    }

    /**
     * Marks a permission as having been requested from the system.
     *
     * @param permission The manifest permission string.
     */
    fun markAsRequested(permission: String) {
        sharedPrefs.edit { putBoolean(permission, true) }
    }

    /**
     * Clears the requested flag. Use this if a permission is later granted.
     *
     * @param permission The manifest permission string.
     */
    fun clearRequestedFlag(permission: String) {
        sharedPrefs.edit { putBoolean(permission, false) }
    }
}
