# Architectural Pattern: Functional Action Strategy

## Overview
The Anarchist Demo implements a Strategy/Command Dispatcher pattern to handle functional test actions (system operations that run only when a permission is granted). This architectural choice prevents the core PermissionRegistry from becoming a monolithic class with over-centralized logic, ensuring the project remains modular and maintainable.

## Design Principles

### 1. Separation of Responsibilities
*   The Registry: Handles "What" (Metadata like names and API ranges).
*   The Card UI: Handles "When" (User interaction).
*   The Executor (PermissionActionExecutor): Handles "How" (The actual Android system intents and service calls).

### 2. Defense in Depth (Safety Guards)
Every action undergoes a double-verification process:
1. UI Guard: The "Test Feature" button is only rendered by Compose when the AnarchistStatus is ALLOWED.
2. Logic Guard: The performAction function performs a final validation of the currentStatus before triggering any system intent.

## Functional Mappings
Actions are mapped via unique constants defined in FeatureIds.kt. This ensures type safety and prevents "Magic String" errors during development.

Current implementations include:
- NOTIFICATIONS: Dispatches a system notification channel and alert.
- CAMERA: Launches the ACTION_IMAGE_CAPTURE system intent.
- EXACT_ALARM: Simulates a time-sensitive background task trigger.