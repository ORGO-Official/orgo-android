
plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.login"

}

dependencies {
    implementation(libs.naver.oauth)
    implementation(libs.kakao.user)
    implementation(libs.timber)
    implementation(libs.androidx.lifecycle.runtimeCompose)
}
