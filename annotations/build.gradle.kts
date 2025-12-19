plugins {
    // We use the standard Kotlin JVM plugin because annotations are just
    // pure Kotlin/Java code and don't require the full Android runtime.
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"]) // 👈 Note: 'java' here for JVM modules

            groupId = "com.github.atfotiad"
            artifactId = name // Uses the folder name (annotations or ksp-processor)
            version = "1.0.0-alpha"
        }
    }
}
// We remove the entire 'android' block because this is a non-Android module.

dependencies {
    // No special dependencies needed, just standard Kotlin library usage.
}