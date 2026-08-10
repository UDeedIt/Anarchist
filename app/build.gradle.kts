plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.ktlint)
}

detekt {
    toolVersion = "1.23.8"
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
}

/**
 * Modern Ktlint configuration for the entire project.
 * This block enforces the official Android Kotlin style guide.
 */
ktlint {
    version.set("1.3.1") // Internal Ktlint engine version
    android.set(true) // Enforces Android-specific spacing and rules
    ignoreFailures.set(false) // Fails the build if style is incorrect
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
    }
}

android {
    namespace = "pro.udeedit.devtools.anarchist.demo"

    compileSdk {
        version =
            release(37) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        applicationId = "pro.udeedit.devtools.anarchist.demo"
        minSdk = 24
        targetSdk = 37
        versionCode = 2
        versionName = "1.0.0 merge 19"

        // vc3, 1.0.0 merge 19 - finished ANARCH-6 Anarchist Library Core
        //      this branch will collect chnages made here.
        // test commit - correct commit message
        // vc 1.0.0 merge 2 - ANARCH-11 Implement Dashboard Orchestration and Navigation
        // vc 1.0.0 - initial complete permission's livecycle

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }

    /**
     * Modern configuration for Kotlin 2.x.
     * This block replaces the deprecated 'kotlinOptions' to set the JVM target.
     */
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(project(":anarchist"))
}
