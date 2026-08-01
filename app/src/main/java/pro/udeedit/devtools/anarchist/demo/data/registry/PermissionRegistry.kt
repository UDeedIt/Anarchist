package pro.udeedit.devtools.anarchist.demo.data.registry

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Alarm // Added for Special Permissions
import androidx.compose.material.icons.filled.PermMedia
import androidx.core.app.NotificationCompat
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_CAMERA
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_EXACT_ALARM
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_MEDIA_BUNDLE
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds.ID_NOTIFICATIONS
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

/**
 * Centralized data provider for all permissions showcased within the Anarchist Demo application.
 *
 * This registry facilitates a scalable architecture by decoupling the permission
 * definitions from the UI layer, allowing for dynamic dashboard generation.
 */
object PermissionRegistry {

    /**
     * Retrieves a curated list of standard "Dangerous" permissions.
     * These permissions utilize the standard Android system request dialog.
     *
     * @return A list of [PermissionFeature] objects for the Standard dashboard tab.
     */
    fun getStandardPermissions(): List<PermissionFeature> = listOf(
        PermissionFeature(
            id = ID_NOTIFICATIONS,
            title = "Post Notifications",
            //noinspection NewApi
            manifestString = Manifest.permission.POST_NOTIFICATIONS,
            icon = Icons.Default.Notifications,
            description = "Enables the application to display push notifications in the system tray.",
            apiRange = "API 33 (Tiramisu) and above.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.POST_NOTIFICATIONS\" />"),
            rationaleLong = "Required for delivering real-time alerts. On devices running API 32 and below, this permission is granted automatically at install time.",
            // functional action
//            actionIfAllowed = { context ->
//                sendTestNotification(context)
//            }
        ),
        PermissionFeature(
            id = ID_CAMERA,
            title = "Camera Access",
            manifestString = Manifest.permission.CAMERA,
            icon = Icons.Default.PhotoCamera,
            description = "Provides access to device camera hardware.",
            apiRange = "All API levels.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.CAMERA\" />"),
            rationaleLong = "Standard dangerous permission requiring runtime approval.",
            isManualOnly = false,
            // functional action
//            actionIfAllowed = { context ->
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                context.startActivity(intent)
//            }
        )
    )

    /**
     * Retrieves a list of special permissions that require manual user intervention
     * within the system settings.
     *
     * @return A list of [PermissionFeature] objects for the Special dashboard tab.
     */
    fun getSpecialPermissions(): List<PermissionFeature> = listOf(
        PermissionFeature(
            id = ID_EXACT_ALARM,
            title = "Schedule Exact Alarms",
            //noinspection NewApi
            manifestString = Manifest.permission.SCHEDULE_EXACT_ALARM,
            icon = Icons.Default.Alarm,
            description = "Required for high-precision, time-sensitive background tasks.",
            apiRange = "API 31 (S) and above.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.SCHEDULE_EXACT_ALARM\" />"),
            rationaleLong = "This permission allows the app to wake the device at an exact millisecond. It cannot be granted via a standard dialog and requires the user to toggle a switch in the system settings.",
            isManualOnly = true, // Signals the UI to show 'Open Settings' immediately
            actionIfAllowed = { context ->
                Toast.makeText(context, "Exact Alarm scheduled successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    )

    /**
     * Retrieves a list of "Feature Bundles" where multiple permissions are
     * requested together to enable a specific application capability.
     *
     * @return A list of [PermissionFeature] objects for the Bundles dashboard tab.
     */
    fun getGroupedPermissions(): List<PermissionFeature> = listOf(
        PermissionFeature(
            id = ID_MEDIA_BUNDLE,
            title = "Media Capture Bundle",
            // For bundles, we use a custom identifier; the ViewModel will map this to a list
            manifestString = "android.permission.CAMERA, android.permission.RECORD_AUDIO",
            icon = Icons.Default.PermMedia,
            description = "Requests both Camera and Microphone access for full media capture support.",
            apiRange = "All API levels.",
            manifestTags = listOf(
                "<uses-permission android:name=\"android.permission.CAMERA\" />",
                "<uses-permission android:name=\"android.permission.RECORD_AUDIO\" />"
            ),
            rationaleLong = "Demonstrates the library's ability to process multiple permissions simultaneously and return a consolidated status.",
            actionIfAllowed = { context ->
                Toast.makeText(context, "Full Media Bundle Granted!", Toast.LENGTH_SHORT).show()
            }
        )
    )
}
