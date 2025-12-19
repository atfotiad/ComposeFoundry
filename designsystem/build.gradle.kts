
plugins {
    // 1. Android Library (Using Alias)
    alias(libs.plugins.android.library)
    // 2. Kotlin Android (Using Alias)
    alias(libs.plugins.kotlin.android)
    // 3. Compose Compiler (Using Alias)
    alias(libs.plugins.kotlin.compose)
    // 4. Serialization Plugin (Using Alias)
    alias(libs.plugins.kotlin.serialization)
    // 5. Hilt Android (Using Alias)
    alias(libs.plugins.hilt.android)
    // 6. KSP (Using Alias)
    alias(libs.plugins.ksp)
    id("maven-publish")

}

android {
    // Using the specified root package name
    namespace = "com.atfotiad.composefoundry.designsystem"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // Enable Compose for this module
    buildFeatures {
        compose = true
    }
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
    api(project(":annotations"))
    // --- UI & Compose ---
    implementation(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.compose.material3.adaptive.navigation.suite)
    api(libs.coil.compose)
    implementation(libs.coil.network)


    // --- Architecture ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // --- Networking (NEW) ---
    api(libs.retrofit)
    api(libs.retrofit.kotlin.serialization)
    api(libs.kotlinx.serialization.json)
    api(libs.okhttp)
    api(libs.okhttp.logging)

    // --- Storage & Security (NEW) ---
    api(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)

    // --- Dependency Injection (NEW) ---
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}