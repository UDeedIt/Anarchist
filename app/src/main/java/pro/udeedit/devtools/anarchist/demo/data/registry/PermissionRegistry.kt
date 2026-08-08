package pro.udeedit.devtools.anarchist.demo.data.registry

import android.Manifest
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.*
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

/**
 * Centralized data provider for the complete Anarchist Demo v1.0 suite.
 *
 * This registry acts as a technical specification, mapping functional IDs
 * to their system requirements and technical metadata.
 */
object PermissionRegistry {

    fun getStandardPermissions(): List<PermissionFeature> = listOf(

        PermissionFeature(
            id = ID_NOTIFICATIONS,
            title = "Post Notifications",
            //noinspection NewApi
            manifestString = Manifest.permission.POST_NOTIFICATIONS,
            icon = Icons.Default.Notifications,
            description = "Enables push notifications in the system tray.",
            apiRange = "API 33+.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.POST_NOTIFICATIONS\" />"),
            supportingRationale = "Required for real-time engagement alerts."
        ),

        PermissionFeature(
            id = ID_CAMERA,
            title = "Camera Access",
            manifestString = Manifest.permission.CAMERA,
            icon = Icons.Default.PhotoCamera,
            description = "Access to hardware camera sensors.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.CAMERA\" />"),
            supportingRationale = "Required for photo and video capture demonstration."
        ),

        PermissionFeature(
            id = ID_LOCATION_FINE,
            title = "Fine Location",
            manifestString = Manifest.permission.ACCESS_FINE_LOCATION,
            icon = Icons.Default.LocationOn,
            description = "High-accuracy GPS coordinate access.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\" />"),
            supportingRationale = "Used for precise mapping and geofencing features."
        ),

        PermissionFeature(
            id = ID_LOCATION_COARSE,
            title = "Coarse Location",
            manifestString = Manifest.permission.ACCESS_COARSE_LOCATION,
            icon = Icons.Default.Map,
            description = "Approximate location access via cellular/WiFi data.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.ACCESS_COARSE_LOCATION\" />"),
            supportingRationale = "Provides general location awareness without high battery drain."
        ),

        PermissionFeature(
            id = ID_MICROPHONE,
            title = "Microphone",
            manifestString = Manifest.permission.RECORD_AUDIO,
            icon = Icons.Default.Mic,
            description = "Access to audio recording hardware.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.RECORD_AUDIO\" />"),
            supportingRationale = "Required for voice input and recording features."
        ),

        PermissionFeature(
            id = ID_CONTACTS_READ,
            title = "Read Contacts",
            manifestString = Manifest.permission.READ_CONTACTS,
            icon = Icons.Default.Contacts,
            description = "Access to the device contact list.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.READ_CONTACTS\" />"),
            supportingRationale = "Enables demonstration of social and contact syncing logic."
        ),

        PermissionFeature(
            id = ID_CONTACTS_WRITE,
            title = "Modify Contacts",
            manifestString = Manifest.permission.WRITE_CONTACTS,
            icon = Icons.Default.EditAttributes,
            description = "Ability to add or edit contact entries.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.WRITE_CONTACTS\" />"),
            supportingRationale = "Required to demonstrate programmatic contact management."
        ),

        PermissionFeature(
            id = ID_CALENDAR,
            title = "Calendar Access",
            manifestString = Manifest.permission.READ_CALENDAR,
            icon = Icons.Default.CalendarMonth,
            description = "Access to user calendar events.",
            apiRange = "All APIs (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.READ_CALENDAR\" />"),
            supportingRationale = "Necessary for demonstrating schedule-aware application features."
        ),

        PermissionFeature(
            id = ID_BODY_SENSORS,
            title = "Body Sensors",
            manifestString = Manifest.permission.BODY_SENSORS,
            icon = Icons.Default.MonitorHeart,
            description = "Access to health data from hardware sensors (e.g. Heart Rate).",
            apiRange = "API 20+ (Runtime since 23).",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.BODY_SENSORS\" />"),
            supportingRationale = "Provides access to real-time physiological data for health features."
        )
    )

    fun getSpecialPermissions(): List<PermissionFeature> = listOf(

        PermissionFeature(
            id = ID_EXACT_ALARM,
            title = "Exact Alarms",
            //noinspection NewApi
            manifestString = Manifest.permission.SCHEDULE_EXACT_ALARM,
            icon = Icons.Default.Alarm,
            description = "High-precision notification timing.",
            apiRange = "API 31+.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.SCHEDULE_EXACT_ALARM\" />"),
            supportingRationale = "Allows the device to wake at an exact millisecond.",
            isManualOnly = true,
            manualEnablementGuidance = "1. Locate 'Anarchist Demo' in the list.\n2. Toggle switch to ON.\n3. Return to the app."
        ),

        PermissionFeature(
            id = ID_SYSTEM_OVERLAY,
            title = "System Overlays",
            manifestString = Manifest.permission.SYSTEM_ALERT_WINDOW,
            icon = Icons.Default.Layers,
            description = "Ability to draw over other applications.",
            apiRange = "All API levels.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />"),
            supportingRationale = "Required for features that display persistent UI over other apps.",
            isManualOnly = true,
            manualEnablementGuidance = "1. Find 'Anarchist Demo' in the list.\n2. Toggle 'Allow display over other apps' to ON."
        )
    )

    fun getGroupedPermissions(): List<PermissionFeature> = listOf(

        PermissionFeature(
            id = ID_MEDIA_BUNDLE,
            title = "Media Capture Bundle",
            manifestString = "${Manifest.permission.CAMERA}, ${Manifest.permission.RECORD_AUDIO}",
            icon = Icons.Default.PermMedia,
            description = "Requests Camera and Microphone simultaneously.",
            apiRange = "All API levels.",
            manifestTags = listOf(
                "<uses-permission android:name=\"android.permission.CAMERA\" />",
                "<uses-permission android:name=\"android.permission.RECORD_AUDIO\" />"
            ),
            supportingRationale = "Unifies hardware access into a single user interaction flow."
        ),

        PermissionFeature(
            id = ID_LOCATION_BUNDLE,
            title = "Full Location Access",
            manifestString = "${Manifest.permission.ACCESS_FINE_LOCATION}, ${Manifest.permission.ACCESS_COARSE_LOCATION}",
            icon = Icons.Default.GpsFixed,
            description = "Requests both Precise and Approximate location.",
            apiRange = "All API levels.",
            manifestTags = listOf(
                "<uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\" />",
                "<uses-permission android:name=\"android.permission.ACCESS_COARSE_LOCATION\" />"
            ),
            supportingRationale = "Ensures the application has comprehensive spatial awareness."
        )
    )
}
