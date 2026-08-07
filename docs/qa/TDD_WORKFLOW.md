# Quality Practice: Test-Driven Development (TDD)

## Overview
The Anarchist project follows a strict Test-First approach for critical UI transitions and logic gates. This practice ensures that bugs are identified by automated suites before they reach production.

## The TDD Cycle in Action
A real-world example of this workflow occurred during the development of the "Pre-Settings Guidance Dialog":

### 1. Identifying the "Negative" Edge Case
While the positive path (showing a dialog) was straightforward, we identified a risk of "Ghost Dialogs" appearing for standard permissions that require no guidance.

### 2. Writing the Failing Test
We developed a Negative Instrumentation Test (testNegative_NoGuidanceBypassesDialog) designed to fail if a dialog appeared when the guidance data was null.

### 3. Red-to-Green Implementation
The initial run resulted in an AssertionError, proving the logic flaw existed. We then applied the if (not null) check to the code, bringing the test to a green (passing) state.

## Benefits for D2D Development
- Regression Safety: Future updates to the navigation logic won't accidentally break the direct settings path.
- Living Documentation: The test cases act as an explicit technical spec of how the PermissionCard should behave under different data inputs.
- Higher Trust: Other developers using the Anarchist library can rely on the fact that every UI state has been verified by an automated suite.

