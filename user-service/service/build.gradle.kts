plugins {
    id("buildlogic.kotlin-application-conventions")
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlinx.serialization)
}

application {
    mainClass.set("it.unibo.sap.ass02.service.ServerKt")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":persistence"))
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.exposed)
    implementation(libs.registry.prometheus)
    implementation(libs.ktor.metrics)
    implementation(libs.kafka.clients)
    testImplementation(libs.bundles.test.kotest)
}
