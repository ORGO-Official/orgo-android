import java.util.Properties
import java.io.FileInputStream

plugins {
    id("orgo.android.application")
    id("orgo.android.application.compose")
    id("orgo.android.hilt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.orgo.android"
    compileSdk = DefaultConfig.COMPILE_SDK_VERSION

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    signingConfigs {
        val properties = Properties().apply {
            load(FileInputStream("${rootDir}/local.properties"))
        }
        create("release") {
            storeFile = file("${properties["releaseStoreFile"]}")
            keyAlias = "${properties["releaseKeyAlias"]}"
            keyPassword = "${properties["releaseKeyPassword"]}"
            storePassword = "${properties["releaseStorePassword"]}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        named("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    defaultConfig {
        applicationId =  "com.orgo.android"
        minSdk = DefaultConfig.MIN_SDK_VERSION
        targetSdk = DefaultConfig.TARGET_SDK_VERSION
        versionCode = DefaultConfig.VERSION_CODE
        versionName = DefaultConfig.VERSION_NAME

        buildConfigField("String","NAVER_CLIENT_ID",getApiKey("NAVER_CLIENT_ID"))
        buildConfigField("String","NAVER_CLIENT_SECRET",getApiKey("NAVER_CLIENT_SECRET"))
        buildConfigField("String","NAVER_CLIENT_NAME",getApiKey("NAVER_CLIENT_NAME"))
        buildConfigField("String","KAKAO_NATIVE_APP_KEY",getApiKey("KAKAO_NATIVE_APP_KEY"))

        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = getApiKey("KAKAO_NATIVE_APP_KEY").replace("\"", "")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = DefaultConfig.COMPOSE_COMPILER
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    // 따로 추가
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // module
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))

    implementation(project(":feature:login"))
    implementation(project(":feature:map"))
    implementation(project(":feature:badge"))
    implementation(project(":feature:mypage"))
    implementation(project(":feature:mountain_detail"))
    implementation(project(":feature:profile_edit"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:climb_complete"))

//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(files("libs/libDaumMapAndroid.jar"))
    // library
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
//    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    /* test */
    testImplementation(libs.junit4)
    implementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.ext)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(libs.timber)
    implementation(libs.naver.oauth)
    implementation(libs.kakao.user)
}

fun getApiKey(propertyKey: String): String {
    return com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir).getProperty(propertyKey)
}