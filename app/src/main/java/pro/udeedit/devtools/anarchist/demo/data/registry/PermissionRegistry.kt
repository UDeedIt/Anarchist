package pro.udeedit.devtools.anarchist.demo.data.registry

import android.Manifest
import android.os.Build
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.EditAttributes
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_BATTERY_OPTIMIZATION
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CALENDAR
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CAMERA
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CONTACTS_READ
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CONTACTS_WRITE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_EXACT_ALARM
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_LOCATION_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_LOCATION_COARSE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_LOCATION_FINE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MEDIA_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MEDIA_IMAGES
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MICROPHONE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_NOTIFICATIONS
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_PERSONAL_DATA_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_SYSTEM_OVERLAY
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_USAGE_STATS
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_WRITE_SETTINGS
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
            id = ID_MEDIA_IMAGES,
            title = "Media Library",
            //noinspection NewApi
            manifestString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            },
            icon = Icons.Default.PhotoLibrary,
            description = "Access to the device's image and photo gallery.",
            apiRange = "Runtime since API 23. Granular media since API 33.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.READ_MEDIA_IMAGES\" />"),
            supportingRationale = "Required to demonstrate access to the system photo library."
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
        ),

        PermissionFeature(
            id = ID_WRITE_SETTINGS,
            title = "Modify System Settings",
            manifestString = Manifest.permission.WRITE_SETTINGS,
            icon = Icons.Default.Settings,
            description = "Allows the application to modify system-level settings (e.g. Brightness).",
            apiRange = "All API levels.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.WRITE_SETTINGS\" />"),
            supportingRationale = "Demonstrates the ability to adjust global system parameters programmatically.",
            isManualOnly = true,
            manualEnablementGuidance = "1. Locate 'Anarchist Demo' in the settings list.\n2. Toggle 'Allow modifying system settings' to ON."
        ),

        PermissionFeature(
            id = ID_USAGE_STATS,
            title = "Usage Stats Access",
            //noinspection NewApi
            manifestString = Manifest.permission.PACKAGE_USAGE_STATS,
            icon = Icons.Default.Insights,
            description = "Allows monitoring of application usage and engagement metrics.",
            apiRange = "API 21 (Lollipop) and above.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.PACKAGE_USAGE_STATS\" tools:ignore=\"ProtectedPermissions\" />"),
            supportingRationale = "Used to demonstrate system-level analytic access. " +
                    "Allows the app to see which other apps are being used.",
            isManualOnly = true,
            manualEnablementGuidance = "1. Find 'Anarchist Demo' in the Usage Access list.\n2. Toggle 'Permit usage access' to ON."
        ),

        PermissionFeature(
            id = ID_BATTERY_OPTIMIZATION,
            title = "Battery Optimization",
            //noinspection NewApi
            manifestString = Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            icon = Icons.Default.BatteryChargingFull,
            description = "Requests to bypass system-level battery saving restrictions.",
            apiRange = "API 23 (Marshmallow) and above.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS\" />"),
            supportingRationale = "Necessary for background tasks that must remain active during system Doze modes.",
            isManualOnly = true,
            manualEnablementGuidance = "1. Find 'Anarchist Demo' in the battery optimization list.\n" +
                    "2. Select 'Don't optimize'.\n3. Confirm to allow persistent background activity."
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
        ),

        PermissionFeature(
            id = ID_PERSONAL_DATA_BUNDLE,
            title = "Personal Data Bundle",
            // Unifying Contacts and Calendar into one request
            manifestString = "${Manifest.permission.READ_CONTACTS}, ${Manifest.permission.READ_CALENDAR}",
            icon = Icons.Default.FolderShared,
            description = "Simultaneous request for Contacts and Calendar access.",
            apiRange = "All API levels.",
            manifestTags = listOf(
                "<uses-permission android:name=\"android.permission.READ_CONTACTS\" />",
                "<uses-permission android:name=\"android.permission.READ_CALENDAR\" />"
            ),
            supportingRationale = "Demonstrates the library's ability to consolidate " +
                    "privacy requests into a single user interaction."
        )
    )
}
