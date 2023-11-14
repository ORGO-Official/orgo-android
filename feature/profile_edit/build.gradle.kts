plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.profile_edit"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.coil)
    implementation(libs.coil.compose)
}