
plugins {
    id("foundry.android.library")
    id("maven-publish")
}

android {
    // Using the specified root package name
    namespace = "com.atfotiad.composefoundry.designsystem"
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                // For Android libraries, we use the 'release' component
                from(components["release"])
                groupId = "com.github.atfotiad"
                artifactId = "designsystem"
                version = project.version.toString()
            }
        }
    }
}


dependencies {
    // --- UI & Compose ---
    api(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.material3.adaptive.navigation.suite)
    api(libs.coil.compose)
    implementation(libs.coil.network)

    // --- Networking (NEW) ---
    api(libs.retrofit)
    api(libs.retrofit.kotlin.serialization)
    api(libs.okhttp)
    api(libs.okhttp.logging)

    // --- Storage & Security (NEW) ---
    api(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
}