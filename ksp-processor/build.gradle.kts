plugins {
    // 1. This must be a standard Kotlin JVM module
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    // 2. KSP API: The main dependency to implement our code generator
    implementation(libs.ksp.api)

    // 3. Our Contracts: The processor needs to know about the annotations it's looking for.
    // We use 'compileOnly' here because the processor only needs the annotation definition
    // during compilation, not during its own runtime.
    implementation(project(":annotations"))

    // 4. KOTLINX POET: A utility for generating Kotlin source code files.
    // This is optional but essential for structured code generation.
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
}