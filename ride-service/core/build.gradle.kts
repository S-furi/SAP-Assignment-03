plugins {
    id("buildlogic.kotlin-common-conventions")
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.logback)
    implementation(libs.bundles.ktor.client)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutine)
    testImplementation(libs.bundles.test.kotest)
}
