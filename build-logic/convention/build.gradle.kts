
plugins {
    `kotlin-dsl`
}

group = "com.atfotiad.composefoundry.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.kotlin.serialization.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "foundry.android.library"
            implementationClass = "com.atfotiad.convention.AndroidLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "foundry.android.library.app" // Or foundry.android.application
            implementationClass = "com.atfotiad.convention.AndroidApplicationConventionPlugin"
        }
    }
}
