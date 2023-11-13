plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.mountain_detail"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.tedPermission)

    implementation(libs.androidx.test.ext)

}