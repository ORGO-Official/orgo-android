// Top-level build file where you can add configuration options common to all sub-projects/modules.

@file:Suppress("DSL_SCOPE_VIOLATION")
buildscript{
    repositories{
        google()
        mavenCentral()
    }

    // 이걸 추가해주지 않으면 Plugin with id 'org.jetbrains.kotlin.android' not found 오류 발생함
    dependencies{
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt) apply false
}