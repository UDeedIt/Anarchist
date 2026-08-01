package pro.udeedit.devtools.anarchist.demo.data.constants

/**
 * Centralized identifiers for all permission features showcased in the demo.
 *
 * Supporting Logic: Using constants instead of raw strings prevents typos
 * across the registry and functional action dispatcher.
 */
object FeatureIds {
    const val ID_NOTIFICATIONS = "NOTIFICATIONS"
    const val ID_CAMERA = "CAMERA"
    const val ID_EXACT_ALARM = "EXACT_ALARM"
    const val ID_MEDIA_BUNDLE = "MEDIA_BUNDLE"
}
