val myMapboxToken: String by project
val myTiandituToken: String by project

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.d10ng.mapbox.demo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.d10ng.mapbox.demo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            // 选择要添加的对应cpu类型的.so库
            abiFilters.addAll(mutableSetOf("arm64-v8a", "armeabi-v7a"))
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "myMapboxToken", "\"$myMapboxToken\"")
            buildConfigField("String", "myTiandituToken", "\"$myTiandituToken\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        release {
            buildConfigField("String", "myMapboxToken", "\"$myMapboxToken\"")
            buildConfigField("String", "myTiandituToken", "\"$myTiandituToken\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlin {
        jvmToolchain(8)
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // 单元测试（可选）
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)

    // jetpack compose 框架
    implementation(libs.dl.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout.compose)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // APP工具
    implementation(libs.dl.app)
    // 地图库
    implementation(project(":mapbox"))
    // 天地图API
    implementation(libs.dl.tianditu)

    // 内存泄漏检查
    debugImplementation(libs.leakcanary)
}