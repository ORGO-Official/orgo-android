plugins {
    id("orgo.android.library")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.core.ui"
}

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))

    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.coil)
    implementation(libs.coil.compose)

}