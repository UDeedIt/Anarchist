plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.dokka) // Dokka v2 applied
    `maven-publish` // Enabling standard Maven artifact creation
    id("signing")   // Enabling PGP/GPG signing for Maven Central
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
     * Modern AGP configuration to automatically prepare the library
     * and its source code for publication.
     */
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
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

afterEvaluate {
    publishing {
        publications {
            // Define the Maven Publication for the 'release' variant
            create<MavenPublication>("release") {
                groupId = project.property("LIBRARY_GROUP").toString()
                artifactId = project.property("LIBRARY_ARTIFACT_ID").toString()
                version = project.property("LIBRARY_VERSION").toString()

                from(components["release"]) // Attach the compiled AAR

                // Attach the generated Javadoc and Source JARs
                artifact(tasks.named("javadocJar"))

                // --- POM Metadata for Maven Central ---
                pom {
                    name.set("Anarchist")
                    description.set("A reactive, boilerplate-free wrapper for Android permission management.")
                    url.set("https://github.com/UDeedIt/Anarchist")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }

                    developers {
                        developer {
                            id.set("UDeedIt") // Your GitHub ID
                            name.set("Sargis Simonyan") // Your real name
                            email.set("udeedit.pro@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:https://github.com/UDeedIt/Anarchist.git")
                        developerConnection.set("scm:git:ssh://git@github.com/UDeedIt/Anarchist.git")
                        url.set("https://github.com/UDeedIt/Anarchist")
                    }
                }
            }
        }

        // for manual upload to Maven Central
        repositories {
            maven {
                name = "Bundle"
                // This creates a folder inside your project build directory
                url = uri(layout.buildDirectory.dir("bundle"))
            }
        }
    }

    /**
     * Signing Configuration
     * Uses in-memory PGP keys to avoid local agent conflicts on macOS.
     */
    project.extensions.configure(SigningExtension::class) {
        val secretKey = project.findProperty("signing.secretKey") as String?
        val password = project.findProperty("signing.password") as String?

        if (secretKey != null && password != null) {
            // Direct signing using armored key content from properties
            useInMemoryPgpKeys(secretKey, password)
        } else {
            // Local fallback
            useGpgCmd()
        }

        sign(publishing.publications["release"])
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}
