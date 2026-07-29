# Architectural Pattern: Reactive Lifecycle Synchronization

## Overview
The Anarchist Dashboard implements a robust State Synchronization pattern. This ensures that the application's UI accurately reflects the Android system's permission state at all times, specifically focusing on the moment a user returns from external system screens (such as the System Permission Dialog or the Application Settings page).

## Implementation Strategy

### 1. Concurrent Effect Orchestration
We utilize a combination of two specific Jetpack Compose side-effect APIs to manage the state lifecycle:

*   DisposableEffect: This effect is used to register a LifecycleEventObserver. By listening specifically for the ON_RESUME event, the dashboard triggers a silent status re-scan every time the app returns to the foreground. This is critical for capturing permission changes made manually by the user in the system settings.
*   LaunchedEffect(Unit): Performs the initial "First-Run" synchronization when the dashboard is first initialized, ensuring the UI is correct upon entry.

### 2. Reactive Data Flow
The synchronization process follows a strict unidirectional data flow:
1. Trigger: A lifecycle event (ON_RESUME) or initial load occurs.
2. Action: The DashboardViewModel executes a non-intrusive status check for all registered permissions.
3. Update: The StateFlow in the ViewModel is updated with the latest AnarchistStatus.
4. Re-composition: Compose observes the state change and updates the PermissionCard visuals instantly.

`Lifecycle Event -> ViewModel Refresh -> StateFlow Update -> UI Re-composition`

## D2D Supporting Benefits
- Zero-Click Updates: Users do not need to manually refresh the list after returning from settings.
- Predictable UI: By centralizing the refresh logic in the ON_RESUME event, we prevent the "Stale Data" bug common in permission handling.
- Resource Efficiency: The observer is automatically removed via onDispose when the 
