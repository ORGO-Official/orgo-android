plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.mypage"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.naver.oauth)
    implementation(libs.kakao.user)
    implementation(libs.coil)
    implementation(libs.coil.compose)
}