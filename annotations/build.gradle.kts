plugins {
    // We use the standard Kotlin JVM plugin because annotations are just
    // pure Kotlin/Java code and don't require the full Android runtime.
    id("org.jetbrains.kotlin.jvm")
}

// We remove the entire 'android' block because this is a non-Android module.

dependencies {
    // No special dependencies needed, just standard Kotlin library usage.
}