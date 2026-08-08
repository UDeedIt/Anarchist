package pro.udeedit.devtools.anarchist.demo.data.constants

/**
 * Centralized identifiers for all permission features showcased in the demo.
 *
 * Supporting Logic: Using constants instead of raw strings prevents typos
 * across the registry and functional action dispatcher.
 */
object FeatureIds {
    // --- 🟢 STANDARD TAB (Dangerous Permissions) ---
    const val ID_NOTIFICATIONS = "NOTIFICATIONS"
    const val ID_CAMERA = "CAMERA"
    const val ID_LOCATION_FINE = "LOCATION_FINE"
    const val ID_LOCATION_COARSE = "LOCATION_COARSE"
    const val ID_MICROPHONE = "MICROPHONE"
    const val ID_CONTACTS_READ = "CONTACTS_READ"
    const val ID_CONTACTS_WRITE = "CONTACTS_WRITE"
    const val ID_CALENDAR = "CALENDAR"
    const val ID_BODY_SENSORS = "BODY_SENSORS"

    // --- 🟠 SPECIAL TAB (System Intents / Manual Only) ---
    const val ID_EXACT_ALARM = "EXACT_ALARM"
    const val ID_SYSTEM_OVERLAY = "SYSTEM_OVERLAY" // Draw over other apps
    const val ID_WRITE_SETTINGS = "WRITE_SETTINGS" // Modify system settings
    const val ID_NOTIFICATION_ACCESS = "NOTIFICATION_LISTENER" // Read other apps' notifications
    const val ID_USAGE_STATS = "USAGE_STATS" // Monitor app usage
    const val ID_BATTERY_OPTIMIZATION = "BATTERY_OPTIMIZATION" // Ignore battery optimizations

    // --- 🔵 BUNDLES TAB (Multi-Permission Requests) ---
    const val ID_MEDIA_BUNDLE = "MEDIA_BUNDLE" // Camera + Mic
    const val ID_LOCATION_BUNDLE = "LOCATION_BUNDLE" // Fine + Coarse
    const val ID_PERSONAL_DATA_BUNDLE = "PERSONAL_DATA_BUNDLE" // Contacts + Calendar
}