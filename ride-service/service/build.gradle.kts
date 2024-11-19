plugins {
    id("buildlogic.kotlin-application-conventions")
    alias(libs.plugins.ktor.plugin)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.ktor.server)
    implementation(libs.ktor.server.websocket)
    implementation(libs.logback)
}

application {
    mainClass = "it.unibo.sap.ass02.ServerKt"
}
