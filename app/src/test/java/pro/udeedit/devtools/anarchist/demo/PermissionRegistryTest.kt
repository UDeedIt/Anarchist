package pro.udeedit.devtools.anarchist.demo

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds
import pro.udeedit.devtools.anarchist.demo.data.registry.PermissionRegistry

/**
 * Unit tests to verify the integrity of the Permission Registry.
 */
class PermissionRegistryTest {
    @Test
    fun testStandardRegistryCompleteness() {
        val standard = PermissionRegistry.getStandardPermissions()

        // Verifying that the minimum expected permissions are present
        assertTrue(
            "Registry should contain Notification permission",
            standard.any { it.id == FeatureIds.ID_NOTIFICATIONS },
        )

        assertTrue(
            "Registry should contain Camera permission",
            standard.any { it.id == FeatureIds.ID_CAMERA },
        )
    }

    @Test
    fun testRegistryIdsAreUnique() {
        val allIds =
            (
                PermissionRegistry.getStandardPermissions() +
                    PermissionRegistry.getSpecialPermissions() +
                    PermissionRegistry.getGroupedPermissions()
            ).map { it.id }

        // Ensure no two features share the same ID
        assertEquals(
            "Every feature in the registry must have a unique ID",
            allIds.size,
            allIds.toSet().size,
        )
    }
}
