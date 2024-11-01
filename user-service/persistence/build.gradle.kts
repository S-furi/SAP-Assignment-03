plugins {
    id("buildlogic.kotlin-application-conventions")
}

dependencies {
    implementation(libs.logback)
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.test.kotest)
    implementation(libs.bundles.exposed)
    implementation(libs.postgres)
    implementation(project(":core"))
}