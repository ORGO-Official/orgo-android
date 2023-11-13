plugins {
    id("orgo.android.feature")
    id("orgo.android.library.compose")
}

android {
    namespace = "com.orgo.feature.map"
}

dependencies {

    implementation(libs.timber)
    implementation(files("${rootDir}/app/libs/libDaumMapAndroid.jar"))
    implementation(libs.gms.location)
    implementation(libs.tedPermission)
//    implementation(libs.androidx.compose.material)
}