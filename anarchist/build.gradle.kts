plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.dokka) // Dokka v2 applied
}

android {
    namespace = "pro.udeedit.devtools.anarchist"

    // Downgraded to 35 for maximum compatibility with existing projects
    //noinspection GradleDependency
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    /**
     * Modern configuration for Kotlin 2.x.
     * This forces the Kotlin compiler to use JVM 17, matching the
     * Java compiler and the :app module settings.
     */
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
}

/**
 * Task to create a JAR containing the Dokka-generated HTML.
 * This is a mandatory requirement for Maven Central publishing.
 */
val javadocJar by tasks.registering(Jar::class) {
    group = "publishing" // Categorizes the task in the Gradle menu
    description = "Assembles a JAR archive containing the Dokka documentation"

    archiveClassifier.set("javadoc")
    from(layout.buildDirectory.dir("dokka/html"))
    dependsOn("dokkaGenerate")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
