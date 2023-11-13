import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("orgo.android.library")
    id("orgo.android.hilt")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.orgo.core.datastore"

    defaultConfig {
        consumerProguardFiles("proguard-rules.pro")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.14.0"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins{
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {

    implementation(project(":core:common"))
    implementation(project(":core:model"))

    testImplementation(libs.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    api(libs.hilt.android.testing)


    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.protobuf.kotlin.lite)
    implementation(libs.timber)

}