plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.settings"
}

dependencies {
    implementation(libs.timber)
}