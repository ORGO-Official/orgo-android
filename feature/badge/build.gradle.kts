plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.badge"

}

dependencies {
    implementation(libs.timber)
    implementation(libs.coil.compose)
}