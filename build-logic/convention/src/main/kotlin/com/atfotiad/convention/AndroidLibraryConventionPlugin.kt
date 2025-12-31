package com.atfotiad.convention


import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
            }

            extensions.configure<LibraryExtension> {
                namespace?.let { this.namespace = it }
                compileSdk = 36

                defaultConfig {
                    minSdk = 24
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                // ADD THIS BLOCK: It forces Kotlin and KSP to use Java 17
                target.tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
                    compilerOptions {
                        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
                    }
                }
                buildFeatures {
                    compose = true
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                // Api (Public) to all modules
                "api"(project(":annotations"))
                "api"(libs.findLibrary("kotlinx-serialization-json").get())

                // Implementation (Private) to each module
                "implementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
                "implementation"(libs.findLibrary("androidx-core-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
                "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
                // Hilt: implementation + KSP (Private)
                "implementation"(libs.findLibrary("hilt-android").get())
                "ksp"(libs.findLibrary("hilt-compiler").get())

                "ksp"(project(":ksp-processor"))
            }
        }
    }
}
