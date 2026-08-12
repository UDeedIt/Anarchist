# Quality Assurance: Appium Automation Interface

## Element Selectors
The following identifiers are exposed via the testTag modifier to enable reliable element discovery in external automation scripts.

### 1. Dashboard Navigation
- Standard Tab: tab_standard_permissions
- Special Tab: tab_special_permissions
- Bundles Tab: tab_bundled_permissions
- Global Reset (🏴‍☠️): btn_global_reset

### 2. Permission Components
- Feature Card: permission_card_[ID] (e.g., permission_card_CAMERA)
- Action Button: btn_action_[ID] (Context-aware button: Request/Settings/Test)
- Info Button: btn_info_[ID] (Triggers technical metadata dialog)

## Priority Automation Scenarios

### Scenario: Multi-Tab Status Synchronization
1. Action: Navigate to tab_special_permissions.
2. Action: Click btn_action_EXACT_ALARM.
3. System Transition: Navigate the system settings to enable the permission.
4. Verification: Return to the app and confirm permission_card_EXACT_ALARM reflects the ALLOWED state.

### Scenario: Rationale Path Verification
1. Action: Click btn_action_CAMERA.
2. Action: Deny the system dialog.
3. Verification: Confirm btn_info_CAMERA icon color updates to indicate rationale availability (WarningOrange).

### Scenario: Global History Reset
1. Action: Click btn_global_reset.
2. Action: Confirm the reset dialog.
3. Verification: Iterate through all tabs and confirm every permission_card has returned to the UNKNOWN state.

## Technical Requirements
- Driver: UIAutomator2
- Locator Strategy: Accessibility ID
