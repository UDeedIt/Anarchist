package pro.udeedit.devtools.anarchist.demo.data.registry

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoCamera
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
            id = "NOTIFICATIONS",
            title = "Post Notifications",
            //noinspection NewApi
            manifestString = Manifest.permission.POST_NOTIFICATIONS,
            icon = Icons.Default.Notifications,
            description = "Enables the application to display push notifications in the system tray.",
            apiRange = "API 33 (Tiramisu) and above.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.POST_NOTIFICATIONS\" />"),
            rationaleLong = "Required for delivering real-time alerts. On devices running API 32 and below, this permission is granted automatically at install time.",
            actionIfAllowed = { context ->
                Toast.makeText(context, "Notification Action Triggered", Toast.LENGTH_SHORT).show()
            }
        ),
        PermissionFeature(
            id = "CAMERA",
            title = "Camera Access",
            manifestString = Manifest.permission.CAMERA,
            icon = Icons.Default.PhotoCamera,
            description = "Provides access to device camera hardware.",
            apiRange = "All API levels.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.CAMERA\" />"),
            rationaleLong = "Standard dangerous permission requiring runtime approval.",
            isManualOnly = false, // Must be false to trigger the standard dialog first
            actionIfAllowed = { context ->
                Toast.makeText(context, "Camera ready for use!", Toast.LENGTH_SHORT).show()
            }
        )

    )
}
