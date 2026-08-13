# Architectural Pattern: UI State Persistence

## Overview
The Anarchist Dashboard is designed to survive System-Initiated Process Death. This occurs frequently in permission management when the Android OS kills the application process to apply security changes made in the system settings.

## Implementation: SavedStateHandle
We utilize the Jetpack Lifecycle SavedStateHandle within the DashboardViewModel to preserve the user's navigation context.

### 1. State Preservation
When a user selects a navigation tab, the unique route string of the Screen object is persisted into the system-managed state map:

```kotlin
savedStateHandle[KEY_ACTIVE_TAB] = screen.route
```

### 2. State Restoration
Upon ViewModel recreation, the engine attempts to retrieve the last active route. It then performs a lookup against the navItems registry to restore the exact Screen object and its associated data projection:

```kotlin
// Logic restores the active Screen object from the route string
val savedRoute = savedStateHandle.get<String>(KEY_ACTIVE_TAB)
```

## D2D Supporting Benefits
- Frictionless Navigation: Users land exactly where they left off after returning from system settings, preventing "navigation reset" frustration.

- Robust Architecture: Demonstrates the library's integration with standard Android lifecycle-safety components.

- Predictable Testing: Allows developers to test complex navigation flows without losing state during configuration changes.
