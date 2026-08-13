# Architectural Pattern: Multi-Tab Navigation Orchestration

## Overview
To handle a growing number of permissions without overwhelming the user, the dashboard utilizes a Categorized Navigation system.

## Implementation
- Navigation Contract (Screen): A sealed class that defines the route, icon, and localized resource IDs for each tab.

- ViewModel State Management: The DashboardViewModel tracks the currentScreen and dynamically filters the PermissionRegistry based on the selected tab.

- Stateless UI: The DashboardContent receives the navigation state and callbacks, ensuring the UI remains a pure renderer.

## D2D Supporting Benefits
By using a resource-based contract (@get:StringRes), the navigation system is fully localized and accessible (TalkBack ready) out of the box.
