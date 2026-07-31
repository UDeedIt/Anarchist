package pro.udeedit.devtools.anarchist.demo.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.GridView
import androidx.compose.ui.graphics.vector.ImageVector
import pro.udeedit.devtools.anarchist.demo.R

/**
 * Defines the navigation hierarchy for the Anarchist Demo dashboard.
 *
 * This sealed class provides a type-safe roadmap for the Bottom Navigation Bar,
 * separating permission types into logical functional areas.
 *
 * @property route The unique identifier for the navigation path.
 * @property titleRes The integer resource ID for the localized tab title.
 * @property icon The visual representation for the navigation item.
 * @property contentDescriptionRes The integer resource ID for the localized accessibility
 * description, ensuring the dashboard is fully compatible with Android TalkBack.
 */
sealed class Screen(
    val route: String,
    @get:StringRes val titleRes: Int,
    val icon: ImageVector,
    @get:StringRes val contentDescriptionRes: Int
) {

    /**
     * Destination for standard dangerous permissions that utilize
     * the unified system request dialog.
     */
    object Standard : Screen(
        "standard",
        R.string.tab_standard,
        Icons.AutoMirrored.Filled.List,
        R.string.nav_desc_standard
    )

    /**
     * Destination for special permissions requiring specific system intent
     * navigation to settings sub-pages (e.g. Exact Alarms).
     */
    object Special : Screen(
        route = "special_permissions",
        titleRes = R.string.tab_special,
        icon = Icons.Default.Category,
        R.string.nav_desc_special
    )

    /**
     * Destination for grouped permission bundles, demonstrating the library's
     * capability to handle multiple asynchronous requests in a single transaction.
     */
    object Bundles : Screen(
        route = "bundled_permissions",
        titleRes = R.string.tab_bundles,
        icon = Icons.Default.GridView,
        R.string.nav_desc_bundles
    )
}

/**
 * The complete collection of destinations for the primary navigation component.
 */
val navItems = listOf(
    Screen.Standard,
    Screen.Special,
    Screen.Bundles
)
