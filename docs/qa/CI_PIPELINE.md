# Quality Assurance: Continuous Integration Pipeline

## Overview
The Anarchist project utilizes GitHub Actions to enforce high code quality and build stability. The pipeline is optimized for a Developer-to-Developer (D2D) workflow, focusing on resource efficiency and strict quality gates.

## Workflow Strategy
To optimize build resources, the CI pipeline is configured to trigger specifically on Pull Request events targeting the master, main, or develop branches. This ensures that every piece of code is verified before it is merged into a shared branch.

## Quality Gates
The pipeline executes the following automated steps in a clean Ubuntu environment:

1. Environment Setup: Initializes the runner with JDK 17 (Temurin distribution) and configures the Gradle Cache to accelerate subsequent builds.

2. Unit Verification: Executes ./gradlew :anarchist:testDebugUnitTest to ensure core permission logic and environment detection remain intact.

3. Static Analysis: Runs ./gradlew :anarchist:lintDebug to enforce Android coding standards and identify potential performance or security issues.

4. Build Validation: Performs a full ./gradlew :anarchist:assembleDebug to verify that the library packages correctly and is ready for distribution.

## D2D Supporting Benefits
- Regression Prevention: Automated testing ensures that new features do not break existing permission logic.

- Build Transparency: Public CI logs allow contributors to verify the health of the project at a glance.
