package pro.udeedit.devtools.anarchist.demo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import pro.udeedit.devtools.anarchist.AnarchistStatus
import pro.udeedit.devtools.anarchist.demo.data.models.PermissionFeature

class PermissionCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * POSITIVE TEST: Guidance Flow
     * Verifies that when a permission HAS guidance, clicking "Open Settings"
     * shows the dialog and then correctly triggers the callback.
     */
    @Test
    fun testPositive_GuidanceDialogTriggersCallback() {
        var settingsCalled = false

        val mockFeature =
            PermissionFeature(
                id = "TEST_POS",
                title = "Guidance Test",
                manifestString = "",
                icon = Icons.Default.Info,
                description = "Test Desc",
                apiRange = "API 24+",
                manifestTags = emptyList(),
                supportingRationale = "Rationale",
                manualEnablementGuidance = "Step 1: Test", // HAS guidance
                isManualOnly = true,
                currentStatus = AnarchistStatus.DENIED,
            )

        composeTestRule.setContent {
            PermissionCard(
                feature = mockFeature,
                onRequest = {},
                onOpenSettings = { settingsCalled = true },
                onRevoke = {},
            )
        }

        // Trigger the dialog
        composeTestRule.onNodeWithText("Open Settings").performClick()
        composeTestRule.onNodeWithText("How to enable Guidance Test").assertIsDisplayed()

        // Click through the dialog
        composeTestRule.onNodeWithText("Go to Settings").performClick()

        // Verify success
        assertTrue("Callback should be triggered after dialog confirmation", settingsCalled)
    }

    /**
     * NEGATIVE TEST: Direct Settings Flow
     * Verifies that when a permission has NO guidance (null), clicking
     * "Open Settings" bypasses the dialog and triggers the callback immediately.
     */
    @Test
    fun testNegative_NoGuidanceBypassesDialog() {
        var settingsCalled = false

        val mockFeature =
            PermissionFeature(
                id = "TEST_NEG",
                title = "No Guidance Test",
                manifestString = "",
                icon = Icons.Default.Info,
                description = "Test Desc",
                apiRange = "API 24+",
                manifestTags = emptyList(),
                supportingRationale = "Rationale",
                manualEnablementGuidance = null, // NO guidance
                isManualOnly = true,
                currentStatus = AnarchistStatus.DENIED,
            )

        composeTestRule.setContent {
            PermissionCard(
                feature = mockFeature,
                onRequest = {},
                onOpenSettings = { settingsCalled = true },
                onRevoke = {},
            )
        }

        // Click the button
        composeTestRule.onNodeWithText("Open Settings").performClick()

        // Verify the dialog did NOT appear
        composeTestRule.onNodeWithText("How to enable No Guidance Test").assertDoesNotExist()

        // Verify the callback was triggered immediately
        assertTrue("Callback should be triggered directly without a dialog", settingsCalled)
    }
}
