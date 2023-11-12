plugins {
    id("orgo.android.library")
    id("orgo.android.hilt")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.core.common"

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.naver.oauth)
    implementation(libs.kakao.user)
    implementation(libs.timber)
}