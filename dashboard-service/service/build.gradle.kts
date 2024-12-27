plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlinx.serialization)
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.bundles.test.kotest)
    testImplementation(libs.bundles.test.ktor)
    testImplementation(libs.bundles.ktor.server)

    implementation(libs.bundles.ktor.client)
    implementation(libs.logback)
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kafka.clients)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "it.unibo.sap.ass02.dashboard.LauncherKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
