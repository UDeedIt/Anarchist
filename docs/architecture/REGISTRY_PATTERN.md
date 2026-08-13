# Architectural Pattern: Scalable Permission Registry

## Overview
The Anarchist Demo Application utilizes a Provider-Registry Pattern to manage the various Android system permissions it showcases. This approach ensures that the application can scale to dozens of permissions without increasing the complexity of the UI layer or the primary Activity logic.

## Design Principles

### 1. Open-Closed Principle (OCP)
The system is designed to be open for extension but closed for modification.
*   Implementation: Adding a new permission demonstration (e.g., Microphone or Location) only requires adding a new entry to the PermissionRegistry.kt. The UI rendering logic (LazyColumn) and the permission request logic remain untouched.

### 2. Separation of Concerns (SoC)
We strictly decouple technical metadata from visual representation.
*   The Model (PermissionFeature): Acts as the "Contract." It holds the technical requirements (Manifest tags, API ranges) and the functional callbacks.

*   The Registry (PermissionRegistry): Acts as the "Data Source." It centralizes the configuration of the permissions.

*   The UI: Acts as the "Renderer." It is purely responsible for displaying the data provided by the registry.

## Data Structure: The PermissionFeature
Every permission in the registry is encapsulated in a PermissionFeature data class. This ensures consistency across the dashboard and provides a unified interface for the "D2D" (Developer-to-Developer) supporting components.

### Key Components:
- Manifest Metadata: Direct provision of XML tags to help other developers implement the permission in their own projects.

- API Range Mapping: Explicit documentation of which Android versions require the specific permission.

- Functional Callbacks: Execution of "Reward Actions" (e.g., opening a camera) only when the state is verified as ALLOWED.

## Future Scalability
This pattern allows for easy integration of:
- Tabbed Navigation: By grouping list outputs from the registry.

- Search Functionality: By filtering the registry list based on IDs or Titles.

- Special Intent Mapping: Providing unique settings-based intents for "Special" permissions without altering the standard request flow.
