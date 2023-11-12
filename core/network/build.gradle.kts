plugins {
    id("orgo.android.library")
    id("orgo.android.hilt")
}

android {
    namespace = "com.orgo.core.network"

    defaultConfig {
        buildConfigField("String", "BASE_URL", getApiKey("BASE_URL"))
    }
}
dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)
    implementation(libs.timber)
    implementation(libs.retrofit.convertor.gson)

}

fun getApiKey(propertyKey: String): String {
    return com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty(propertyKey)
}