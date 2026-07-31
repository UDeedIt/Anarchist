package pro.udeedit.devtools.anarchist.demo.data.mocks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.PhotoCamera
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

/**
 * Providing static data for IDE Previews and UI testing.
 *
 * D2D Supporting Utility: This object centralizes mock data to keep
 * Composable preview functions clean and focused on layout verification.
 */
object PreviewMocks {

    /** Mock list for the 'Standard' tab demonstration */
    val mockStandardList = listOf(
        PermissionFeature(
            id = "MOCK_NOTIF",
            title = "Post Notifications",
            manifestString = "",
            icon = Icons.Default.Notifications,
            description = "Enables push notifications in the system tray.",
            apiRange = "API 33+",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.POST_NOTIFICATIONS\" />"),
            rationaleLong = "Required for real-time alerts.",
            currentStatus = AnarchistStatus.DENIED,
            wasAskedBefore = false
        ),
        PermissionFeature(
            id = "MOCK_CAMERA",
            title = "Camera Access",
            manifestString = "",
            icon = Icons.Default.PhotoCamera,
            description = "Provides access to device camera hardware.",
            apiRange = "All API levels.",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.CAMERA\" />"),
            rationaleLong = "Standard dangerous permission for photo/video capture.",
            currentStatus = AnarchistStatus.ALLOWED,
            wasAskedBefore = true
        )
    )

    /** Mock list for the 'Special' tab demonstration */
    val mockSpecialList = listOf(
        PermissionFeature(
            id = "MOCK_ALARM",
            title = "Schedule Exact Alarms",
            manifestString = "",
            icon = Icons.Default.Alarm,
            description = "Required for high-precision background tasks.",
            apiRange = "API 31+",
            manifestTags = listOf("<uses-permission android:name=\"android.permission.SCHEDULE_EXACT_ALARM\" />"),
            rationaleLong = "Cannot be granted via standard dialog; requires settings navigation.",
            isManualOnly = true, // Shows orange/red button immediately
            currentStatus = AnarchistStatus.DENIED,
            wasAskedBefore = false
        )
    )

    /** Mock list for the 'Bundles' tab demonstration */
    val mockBundlesList = listOf(
        PermissionFeature(
            id = "MOCK_MEDIA",
            title = "Media Capture Bundle",
            manifestString = "",
            icon = Icons.Default.PermMedia,
            description = "Requests Camera and Microphone access together.",
            apiRange = "All API levels.",
            manifestTags = listOf(
                "<uses-permission android:name=\"android.permission.CAMERA\" />",
                "<uses-permission android:name=\"android.permission.RECORD_AUDIO\" />"
            ),
            rationaleLong = "Demonstrates multi-permission request handling.",
            currentStatus = AnarchistStatus.DENIED,
            wasAskedBefore = true
        )
    )
}
