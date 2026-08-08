# Quality Assurance: Static Analysis with Detekt

## Overview
In the Anarchist project, static analysis is treated as a "Shift-Left" quality gate. By utilizing Detekt, we ensure that the codebase adheres to strict Kotlin standards, identifies technical debt early, and maintains architectural integrity across both the library and demo modules.

## Technical Challenges & Architectural Evolutions

Integrating static analysis into a modern project using Jetpack Compose and specialized system APIs required a series of deliberate architectural decisions.

### 1. Reconciling Compose with Standard Naming Rules
Standard Kotlin rules require functions to use camelCase. However, Jetpack Compose idiomatic style requires @Composable UI components to use PascalCase.
*   The Conflict: Detekt initially flagged every UI component as a naming violation.
*   The Resolution: We configured the FunctionNaming rule to ignore any function annotated with @Composable. This demonstrates an understanding of framework-specific standards while maintaining strict rules for standard logic functions.

### 2. Solving Parameter Bloat via the Command Pattern
The DashboardContent component initially triggered the LongParameterList rule due to its high number of interaction callbacks (state, navigation, and functional triggers).
*   The Refactor: Instead of simply increasing the threshold, we utilized the Command Pattern. By encapsulating all UI interactions into a single DashboardActions data class, we reduced the parameter count from 8 down to 4.
*   Benefit: This improved the decoupling of our stateful and stateless components and made the API significantly more scalable for future features.

### 3. Stability vs. Purity: Strategic Suppression
A key rule in static analysis is TooGenericExceptionCaught, which discourages catching the base Exception class.
*   The Context: In our PermissionActionExecutor, the app interacts with diverse system intents (Camera, Maps, Settings). Across the fragmented Android ecosystem (OEMs like Xiaomi, Samsung), these intents can throw unpredictable, non-standard exceptions.
*   The Decision: We implemented local @Suppress("TooGenericExceptionCaught") annotations. This is a senior-level trade-off: we prioritized application stability (ensuring the app never crashes on a specific device) over exception granularity, which is essential for a robust utility tool.

### 4. Semantic Color Refactoring
Initial analysis flagged several "Magic Numbers" related to hex color codes (e.g., 0xFF4CAF50).
*   The Cleanup: All hardcoded hex values were extracted into a centralized Color.kt file and assigned semantic names like SuccessGreen and WarningOrange.
*   Benefit: This ensures visual consistency across the dashboard and makes the theme easily maintainable.

## Technical Thresholds for 2026
To accommodate the nested nature of Jetpack Compose UI trees, we have tuned our complexity thresholds to be realistic yet firm:
- LongMethod: Set to 200 lines to allow for comprehensive screen definitions.
- CyclomaticComplexity: Set to 25 to handle multi-state permission logic matrices.
- MaxLineLength: Set to 160 characters to support technical KDoc and long Manifest XML strings.

## D2D Supporting Impact
By maintaining a "Zero-Debt" Detekt configuration, we provide other developers with a codebase that is not only functional but serves as a gold standard for Kotlin development practices.