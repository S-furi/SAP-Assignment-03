plugins {
    id("buildlogic.kotlin-application-conventions")
    alias(libs.plugins.ktor.plugin)
}

dependencies {
    implementation(libs.logback)
    implementation(libs.bundles.ktor.server)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.exposed)
    implementation(libs.registry.prometheus)
    implementation(libs.ktor.metrics)
    implementation(project(":core"))
    implementation(project(":persistence"))
}

application {
    mainClass.set("it.unibo.sap.ass02.service.ServerKt")
}
