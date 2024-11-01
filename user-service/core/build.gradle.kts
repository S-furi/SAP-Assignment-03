plugins {
    id("buildlogic.kotlin-application-conventions")
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.server)
    testImplementation(libs.bundles.test.kotest)
}

application {
    // Define the main class for the application.
    mainClass = "it.unibo.sap.ass02.MainKt"
}
