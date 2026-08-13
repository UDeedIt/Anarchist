# Technical Note: Asynchronous Revocation of One-Time Permissions

## Observation
During the development and testing of the Anarchist library, a specific behavior was identified regarding "Only this time" (Session-based) permissions introduced in Android 11 (API 30).

## The "Asynchronous Lag"
When a user grants a permission using the "Only this time" option, the Android system creates a temporary session. Upon a "Force Stop" or process kill, the system is designed to revoke this permission. However, testing on physical hardware (e.g., Xiaomi devices) revealed that this revocation is not instantaneous.

### Key Findings:
- System Delay: checkSelfPermission() may continue to return PERMISSION_GRANTED for several minutes after an app has been killed.

- Background Cleanup: The Android Permission Controller handles the cleanup asynchronously. If the app is restarted immediately, it may encounter a "False Positive" granted state.

- Verification: Testing confirmed that after waiting approximately 5 minutes post-kill, the system correctly reports the state as DENIED.

## Implementation Impact
Because the Android SDK only returns a binary GRANTED or DENIED status, it is impossible to programmatically detect if a permission is session-based.

Recommendation:
- Never cache the ALLOWED state across app sessions.

- Always perform a fresh system check during the ON_RESUME lifecycle event.

- For high-security actions, perform a check immediately before the action is executed.
