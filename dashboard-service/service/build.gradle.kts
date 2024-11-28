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

    implementation(libs.bundles.ktor.client)
    implementation(libs.logback)
    implementation(libs.kotlinx.coroutine)
    implementation(libs.kotlinx.serialization.json)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "it.unibo.sap.ass02.dashboard.ServerKt"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
