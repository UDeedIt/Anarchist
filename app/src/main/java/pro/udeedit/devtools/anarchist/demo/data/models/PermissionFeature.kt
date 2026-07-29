package pro.udeedit.devtools.anarchist.demo.data.models

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import pro.udeedit.devtools.anarchist.AnarchistStatus

/**
 * Encapsulates the metadata, requirements, and functional logic for a specific Android system permission.
 *
 * This model serves as the data source for the Anarchist Dashboard, providing both
 * user-facing UI elements and developer-facing supporting documentation.
 *
 * @property id Unique identifier for the feature.
 * @property title Human-readable label for the permission.
 * @property manifestString The exact system permission string from [android.Manifest.permission].
 * @property icon Visual representation used in dashboard cards and dialogs.
 * @property description A concise summary of what the permission enables within the application.
 * @property apiRange The specific Android API levels where this permission is enforced at runtime.
 * @property manifestTags A list of required XML tags to be declared in the `AndroidManifest.xml`.
 * @property rationaleLong A detailed technical explanation intended for the D2D supporting Information Dialog.
 * @property currentStatus The reactive state of the permission, determined by the Anarchist library.
 * @property isManualOnly Flag indicating if the permission requires manual intervention in system settings (e.g. Exact Alarms).
 * @property wasAskedBefore Determines if the "Clear History" button should be visible.
 *      Matches the library's internal 'requestedBefore' flag.
 * @property actionIfAllowed An optional functional callback to be executed once the permission is granted.
 */
data class PermissionFeature(
    val id: String,
    val title: String,
    val manifestString: String,
    val icon: ImageVector,
    val description: String,
    val apiRange: String,
    val manifestTags: List<String>,
    val rationaleLong: String,
    val currentStatus: AnarchistStatus = AnarchistStatus.DENIED,
    val isManualOnly: Boolean = false,
    val wasAskedBefore: Boolean = false,
    val actionIfAllowed: ((Context) -> Unit)? = null
)
