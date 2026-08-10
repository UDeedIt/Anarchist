package pro.udeedit.devtools.anarchist.demo.ui

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import pro.udeedit.devtools.anarchist.demo.data.constants.FeatureIds

/**
 * UI Automation suite for the Anarchist Dashboard.
 *
 * This suite utilizes the Kaspresso framework to perform end-to-end
 * validation of the dashboard's navigation, data rendering,
 * and informational components.
 */
class DashboardTest :
    TestCase(
        kaspressoBuilder = Kaspresso.Builder.withComposeSupport(),
    ) {
    /**
     * Rule that launches the MainActivity and provides a controlled
     * environment for Jetpack Compose testing.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
//    val composeTestRule = createComposeRule()

    /**
     * Verifies the integrity of the Bottom Navigation system.
     *
     * Ensures that switching between tabs correctly filters and
     * displays the intended permission categories.
     */
    @Test
    fun testNavigationTabSwitching() =
        run {
//        val activity = composeTestRule.activity
//        val specialTabLabel = activity.getString(R.string.tab_special)

            // The Dashboard is already initialized by the ActivityRule

//        step("Initialize Dashboard") {
//            composeTestRule.setContent {
//                AnarchistDashboard()
//            }
//        }

            step("Verify Standard permissions are listed") {
                // Standard tab is default
                composeTestRule.onNodeWithText("Camera Access").assertExists()
            }

            step("Switch to Special permissions tab") {
                // Click the tab
                composeTestRule.onNodeWithText("Special").performClick()

                // Wait for Compose to finish the transition
                composeTestRule.waitForIdle()
            }

            step("Verify Special permissions are now visible") {
                /**
                 * Using flakySafely
                 * This handles the 'Race Condition'. If the LazyColumn takes 200ms
                 * to swap, Kaspresso will wait and retry until the text appears.
                 *
                 * We use onNodeWithTag and assertIsDisplayed.
                 * If the list is long, we use the unmerged tree to ensure
                 * the node is found regardless of parent complexity.
                 */
                flakySafely {
                    composeTestRule
                        .onNodeWithTag(
                            "permission_card_${FeatureIds.ID_EXACT_ALARM}",
                            useUnmergedTree = true,
                        ).assertIsDisplayed()
                }
            }
        }

    /**
     * Verifies the functional state of the Supporting Information Dialog.
     *
     * Confirms that technical metadata and manifest requirements are
     * accessible to the developer through the interactive dashboard.
     */
    @Test
    fun testInformationDialogVisibility() =
        run {
            // We target the 'NOTIFICATIONS' card for this specific test
            val targetId = FeatureIds.ID_NOTIFICATIONS

            step("Open Technical Information Dialog") {
                // Using a unique testTag ensures we click the icon for the correct card
                composeTestRule
                    .onNodeWithTag("btn_info_$targetId", useUnmergedTree = true)
                    .performClick()
            }

            step("Verify Dialog Content is visible") {
                flakySafely {
                    // Verifies that the technical header appears in the popup
                    composeTestRule
                        .onNodeWithText("Technical Metadata", useUnmergedTree = true)
                        .assertIsDisplayed()
                }
            }

            step("Close Information Dialog") {
                composeTestRule.onNodeWithText("Close").performClick()
            }
        }

    /**
     * Verifies the functional state of the Supporting Information Dialog
     * for the FIRST card in the list, ensuring generic component reliability.
     */
    @Test
    fun testInformationDialogOnFirstCard() =
        run {
            step("Open Info Dialog for the first card in the sequence") {
                /**
                 * The Fix: Create a custom SemanticsMatcher.
                 * This logic manually inspects the TestTag property of each node
                 * and returns true if the tag starts with our identifier prefix.
                 */
                val prefixMatcher =
                    SemanticsMatcher("TestTag starts with 'btn_info_'") { node ->
                        val tag = node.config.getOrElse(androidx.compose.ui.semantics.SemanticsProperties.TestTag) { "" }
                        tag.startsWith("btn_info_")
                    }

                composeTestRule
                    .onAllNodes(prefixMatcher, useUnmergedTree = true)
                    .onFirst()
                    .performClick()
            }

            step("Verify Dialog Content visibility") {
                flakySafely {
                    composeTestRule
                        .onNodeWithText("Technical Metadata", useUnmergedTree = true)
                        .assertIsDisplayed()
                }
            }

            step("Close Dialog") {
                composeTestRule.onNodeWithText("Close").performClick()
            }
        }
}
