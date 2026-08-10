# Quality Assurance: UI Automation with Kaspresso

## Overview
The Anarchist project utilizes the Kaspresso framework to perform automated end-to-end (E2E) testing of the dashboard. This ensures that navigation, reactive list updates, and informational components remain functional across different Android versions and device configurations.

## Testing Strategy

### 1. Lifecycle-Aware Activity Testing
We utilize createAndroidComposeRule<MainActivity>() to ensure that the library’s lifecycle-dependent logic (such as ON_RESUME status refreshes) is fully active during the test execution. This provides a 1:1 simulation of real-world usage.

### 2. Precise Targeting with Test Tags
To avoid ambiguity in complex lists (like our multi-permission dashboard), we have implemented a Test Tag Contract. Every interactive element is assigned a unique technical identifier:
*   Cards: permission_card_${feature.id}
*   Buttons: btn_info_${feature.id}

### 3. Dynamic Node Discovery (Custom Matchers)
In scenarios where we need to verify component integrity without targeting a specific feature, we use a custom SemanticsMatcher.
*   The Logic: The matcher scans the UI tree for any node whose TestTag starts with a specific prefix (e.g., btn_info_).
*   Benefit: This allows the test suite to remain stable even if the order or content of the permission registry changes.

## Reliability Logic
By leveraging Kaspresso's flakySafely and step DSL, our tests include built-in retry mechanisms and human-readable logging. This reduces "false negative" reports caused by device lag or asynchronous re-compositions.

## D2D Benefits
Automated UI tests serve as a "living specification," proving to other developers that the library’s UI components are robust and its orchestration layer is correctly synchronized with the Android system.