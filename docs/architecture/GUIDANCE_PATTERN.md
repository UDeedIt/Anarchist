# Architectural Pattern: Conditional Guidance Dialogs

## Overview
The Anarchist Dashboard employs a Conditional Guidance Pattern to assist users during manual permission enablement. This pattern ensures that users are never sent to a complex Android settings page without a clear "map" of the required actions.

## Logic Design

### 1. Nullable Control Flags
The system uses the manualEnablementGuidance property as a logic gate.
*   Non-Null State: Triggers a preparatory AlertDialog containing step-by-step instructions.
*   Null State: Signals that the permission is a standard system request and bypasses the dialog for a frictionless experience.

### 2. Dual-Path Execution
The UI logic is bifurcated to handle both "Standard" and "Special" permissions within a single button component:

```kotlin 
if (feature.manualEnablementGuidance != null) { 
    showGuidanceDialog = true // Guided Path 
} else { 
    onOpenSettings() // Direct Path 
} 
```

## Automated Verification
To maintain the integrity of this pattern, the project includes both Positive and Negative instrumentation tests:
*   Positive Test: Verifies the dialog correctly renders and triggers the intent when instructions are present.
*   Negative Test: Ensures that standard permissions (where guidance is null) do not trigger "Ghost Dialogs," protecting the user from redundant UI steps.

## D2D Supporting Benefits
- UX Consistency: Standardizes the "Settings Jump" behavior across all permission categories.
- Contextual Knowledge: Allows the PermissionRegistry to act as a documentation provider, explaining specific system navigation steps to the end developer.
- Friction Reduction: Preparation via the dialog reduces the rate of user abandonment when navigating specialized system settings.

