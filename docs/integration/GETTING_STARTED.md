# Integration Guide: Getting Started with Anarchist

## Overview
Anarchist is designed for rapid integration with minimal boilerplate. This guide outlines the steps required to implement the library in a professional Android application.

## 1. Dependency Integration
Add the library to your build.gradle.kts file. Anarchist is hosted on Maven Central:

```kotlin 
dependencies { 
    implementation("pro.udeedit.devtools:anarchist:1.0.3") 
}
```

## 2. Global Initialization
For optimal performance, initialize the library once within your custom Application class. This prepares the internal persistence layer globally.

```kotlin 
class MyApp : Application() { 
    override fun onCreate() { 
        super.onCreate() 
        // Unified initialization 
        Anarchist.init(this) 
    } 
} 
```

## 3. Basic Permission Request
Execute a unified "Check and Request" flow from any Activity. The library handles the logic for standard denials, rationales, and permanent blocks.

```kotlin 
val result = Anarchist.checkAndRequestPermissions( 
    activity = this, 
    permissions = listOf(Manifest.permission.CAMERA), 
    requestCode = 101 
) 

if (result.finalStatus == AnarchistStatus.ALLOWED) { 
    // Execute feature logic 
} 
```

## 4. Handling Permanent Denials
When a user has permanently denied a permission, utilize the built-in utility to guide them to the settings page:

```kotlin 
if (result.finalStatus == AnarchistStatus.DENIED_PERMANENTLY) { 
    Anarchist.openSettings(context) 
} 
```