val myMapboxToken: String by project
val myTiandituToken: String by project

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.d10ng.mapbox.demo"
    compileSdk = android_compile_sdk

    defaultConfig {
        applicationId = "com.d10ng.mapbox.demo"
        minSdk = android_min_sdk
        targetSdk = android_target_sdk
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
    // Android
    implementation("androidx.core:core-ktx:$androidx_core_ver")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:$dl_compose_ver")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:$dl_app_ver")

    implementation(project(":mapbox"))
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi:$dl_tdt_ver")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil:$dl_http_ver")

    // 内存泄漏检查
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")
}