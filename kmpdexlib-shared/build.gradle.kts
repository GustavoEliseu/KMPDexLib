import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("native.cocoapods")
    id("maven-publish")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishLibraryVariants("release")
    }
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "KMPDexLib — Pokédex Compose Multiplatform screens"
        homepage = "https://github.com/GustavoEliseu/KMPDexLib"
        ios.deploymentTarget = "15.0"
        version = project.version.toString()
        framework {
            baseName = "KMPDexLib"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":concept-list"))
            implementation(project(":concept-detail"))
        }
    }
}

android {
    namespace = "com.gustavo.kmpdexlib.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
