plugins {
    id("buildlogic.kotlin-common-conventions")
}

dependencies {
    implementation(project(":core"))
    implementation(libs.bundles.exposed)
    implementation(libs.postgres)
    implementation(libs.logback)
}
