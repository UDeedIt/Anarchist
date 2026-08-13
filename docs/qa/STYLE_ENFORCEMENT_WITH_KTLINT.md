# Quality Assurance: Style Enforcement with Ktlint

## Overview
To maintain a consistent and professional codebase, the Anarchist project utilizes Ktlint. This ensures that all contributors follow the official Kotlin Style Guide and Google's Android-specific formatting rules.

## Configuration Strategy
We utilize a project-root .editorconfig file to synchronize the rules between the Gradle build system and the Android Studio IDE.

### 1. Reconciling Standards with Jetpack Compose
Traditional Kotlin rules require camelCase for all functions. However, Jetpack Compose idiomatic style requires PascalCase for @Composable components.
*   The Resolution: We explicitly disabled the ktlint_standard_function-naming and ktlint_standard_property-naming rules to support modern UI development without sacrificing code quality.

### 2. Backing Property Support
The project utilizes the underscore prefix pattern for private StateFlows (e.g., _allFeatures). We configured Ktlint to support this pattern by disabling the strict backing-property naming rule.

## Automation
- Check: ./gradlew ktlintCheck is integrated into the CI/CD pipeline.

- Auto-Fix: ./gradlew ktlintFormat is used during development to automatically resolve spacing, indentation, and newline violations.

## D2D Benefits
Automated formatting removes "visual noise" from Pull Requests, allowing reviewers to focus purely on logic and architecture rather than spacing or bracket placement.
