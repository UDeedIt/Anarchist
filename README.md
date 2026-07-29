# Anarchist 🏴‍☠️

Android Permissions Library — Allow them all!

Anarchist is a developer-centric (D2D) library designed to liberate your code from the boilerplate of Android permission handling. It provides a clean, reactive, and unified way to request, check, and manage permission states across all API levels.

---

## 🏗️ Architecture & Patterns
The project follows strict architectural principles to ensure scalability and maintainability:
- Registry Pattern: Decouples permission definitions from the UI layer. See Documentation
- Lifecycle Synchronization: Uses reactive observers to auto-refresh states when returning from system settings. See Documentation
- State-Driven UI: Components adapt visually to reflect standard, rationale, and permanent denial states.

---

## 🚀 Key Features
- Unified API: A single object (Anarchist) to handle both checking and requesting logic.
- Permanent Denial Detection: Integrated persistence to track and handle "Don't ask again" states.
- D2D Supporting UI: Interactive dashboard with technical metadata, manifest requirements, and API level guidance.
- Functional Testing Tools: Built-in "Clear History" utilities for developers to test the "First-Run" experience repeatedly.

---

## 🛠️ Quick Start

### 1. Check & Request
```kotlin 
val result = Anarchist.checkAndRequestPermissions( 
    activity = this, 
    permissions = listOf(Manifest.permission.CAMERA), 
    requestCode = 101 
)
```


### 2. Open Settings (Recovery)
```kotlin 
if (result.finalStatus == AnarchistStatus.DENIED_PERMANENTLY) { 
    Anarchist.openSettings(context) 
}
```


---

## 📄 License
This project is licensed under the MIT License.

---
Designed and built for developers by developers.
🚀 UDeedIt Pro: DevTools Suite