plugins {
    id("buildlogic.kotlin-common-conventions")
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":persistence"))
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.exposed)
    testImplementation(libs.bundles.test.kotest)
}