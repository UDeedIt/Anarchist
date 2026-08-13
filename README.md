# Anarchist 🏴‍☠️

"Everything is permitted!"

Anarchist is a developer-centric (D2D) library designed to liberate your code from the boilerplate of Android permission handling. It provides a clean, reactive, and unified way to request, check, and manage permission states across all API levels.

---

## 🏗️ Architecture & Patterns

The project follows strict architectural principles to ensure scalability and maintainability:

- **Registry Pattern:** Decouples permission definitions from the UI layer. [Read Architecture Record](docs/architecture/REGISTRY_PATTERN.md)

- **Lifecycle Synchronization:** Uses reactive observers to auto-refresh states when returning from system settings. [Read Architecture Record](docs/architecture/LIFECYCLE_SYNC.md)

- **State Persistence:** Ensures the UI context survives system-initiated process death using SavedStateHandle. [Read Architecture Record](docs/architecture/STATE_PERSISTENCE.md)

- **Conditional Guidance:** Provides step-by-step navigation instructions for manual system settings. [Read Architecture Record](docs/architecture/GUIDANCE_PATTERN.md)

- **Action Strategy:** Decouples system intents from the registry via a centralized command dispatcher. [Read Architecture Record](docs/architecture/ACTION_STRATEGY.md)


## 🛡️ Quality Assurance (QA)

We maintain high code standards through a modern, automated pipeline:

- **Static Analysis:** Strict logic enforcement via customized Detekt rules. [View QA Record](docs/qa/STATIC_ANALYSIS.md)

- **Style Enforcement:** Project-wide formatting and consistency with Ktlint. [View QA Record](docs/qa/STYLE_ENFORCEMENT.md)

- **UI Automation:** End-to-end dashboard verification using the Kaspresso framework. [View QA Record](docs/qa/UI_AUTOMATION.md)

- **Black-box Readiness:** Comprehensive technical selector map for external Appium automation. [View QA Record](docs/qa/APPIUM_HANDOFF.md)

- **CI/CD:** Automated builds and quality gates via GitHub Actions. [View QA Record](docs/qa/CI_PIPELINE.md)

---

## 📦 Installation

Anarchist is available on Maven Central. Add the following dependency to your build.gradle.kts:

```kotlin
dependencies {
    implementation("pro.udeedit.devtools:anarchist:1.0.0")
}
```

---

## 🚀 Key Features

- Unified API: A single object (Anarchist) to handle both checking and requesting logic.

- Global Synchronization: A Master List architecture ensures all permission categories are updated simultaneously upon app resumption.

- Permanent Denial Detection: Integrated persistence to track and handle "Don't ask again" states.

- Navigation Stability: Automatically restores the active tab and scroll position after returning from system settings.

- D2D Supporting UI: Interactive dashboard with technical metadata, manifest requirements, and navigation guidance.

- JVM Unit Test Safety: Environment detection prevents hardware-related crashes during local unit testing.

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

### 2. Handle Results
```kotlin
when (result.finalStatus) {
    AnarchistStatus.ALLOWED -> // Access hardware
    AnarchistStatus.DENIED_PERMANENTLY -> Anarchist.openSettings(context)
    else -> // Handle standard denial or rationale
}
```

---

## 📄 License
This project is licensed under the MIT License.

---
Designed and built for developers by developers. Focus on your code — we'll handle the permissions.
🚀 UDeedIt Pro: DevTools Suite
