# Anarchist 🏴‍☠️

Android Permissions Library — Everything is permitted!

Anarchist is a developer-centric (D2D) library designed to liberate your code from the boilerplate of Android permission handling. It provides a clean, reactive, and unified way to request, check, and manage permission states across all API levels.

---

## Architecture & Patterns
The project follows strict architectural principles to ensure scalability and maintainability:
- Registry Pattern: Decouples permission definitions from the UI layer. See Documentation
- Lifecycle Synchronization: Uses reactive observers to auto-refresh states when returning from system settings. See Documentation
- State Persistence: Ensures the UI context survives system-initiated process death using SavedStateHandle. See Documentation
- Conditional Guidance: Provides step-by-step navigation instructions for manual system settings. See Documentation

---

## Key Features
- Unified API: A single object (Anarchist) to handle both checking and requesting logic.
- Global Synchronization: A "Master List" architecture ensures all permission categories are updated simultaneously upon app resumption.
- Permanent Denial Detection: Integrated persistence to track and handle "Don't ask again" states.
- Navigation Stability: Automatically restores the user's active tab and scroll position after returning from system settings.
- D2D Supporting UI: Interactive dashboard with technical metadata, manifest requirements, and navigation guidance.
- Functional Testing Tools: Built-in "Clear History" utilities to reset the internal request history for first-run testing.

---

## Quick Start

### 1. Check & Request
```kotlin 
val result = Anarchist.checkAndRequestPermissions( 
    activity = this, 
    permissions = listOf(Manifest.permission.CAMERA), 
    requestCode = 101 
)
```

### 2. Handle Results

```kotlin 
when (result.finalStatus) { 
    AnarchistStatus.ALLOWED -> // Use the feature 
    AnarchistStatus.DENIED_PERMANENTLY -> Anarchist.openSettings(context) 
    else -> // Handle standard denial or rationale 
}
```


---

## License
This project is licensed under the MIT License.

---
Designed and built for developers by developers. 
UDeedIt Pro: DevTools Suite