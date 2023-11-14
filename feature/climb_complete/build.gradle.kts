
plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.climb_complete"

}

dependencies {
    implementation(libs.timber)
    implementation(libs.coil.compose)
    implementation(libs.tedPermission)
    implementation(libs.kakao.share)
}
