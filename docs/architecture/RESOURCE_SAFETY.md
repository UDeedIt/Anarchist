# Architectural Pattern: Resource Safety and Use-Site Targets

## Overview
The Anarchist project enforces strict type safety for Android resources. This ensures that only valid resource IDs are passed between components, preventing runtime crashes due to "Resource Not Found" errors.

## Implementation: Kotlin Use-Site Targets

In our navigation contract (NavDestinations.kt), we utilize the @get:StringRes annotation on constructor properties.

### Why we use @get:
In Kotlin, a property declared in a constructor is simultaneously a parameter, a field, and a getter function. By default, an annotation like @StringRes only applies to the constructor parameter.

To ensure that the Android Lint tool can verify the resource type throughout the app's lifecycle, we explicitly target the getter:

```kotlin 
@get:StringRes val titleRes: Int
``` 


## D2D Supporting Benefits
*   Compiler-Level Validation: The IDE will flag an error if a developer attempts to assign a raw integer or a wrong resource type (e.g., a Color ID) to a Title property.
*   Clean Documentation: Using standard Android annotations makes the code self-documenting for other engineers.
*   Localization Ready: By forcing the use of resource IDs, we ensure the library is fully prepared for multi-language support from day one.