val myMapboxToken: String by project
val myTiandituToken: String by project

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.d10ng.mapbox.demo"
    compileSdk = Project.compile_sdk

    defaultConfig {
        applicationId = "com.d10ng.mapbox.demo"
        minSdk = Project.min_sdk
        targetSdk = Project.target_sdk
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
    composeOptions {
        kotlinCompilerExtensionVersion = compose_compiler_ver
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // Android
    implementation("androidx.core:core-ktx:1.12.0")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:2.0.11")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:2.4.12")
    // 日期工具兼容Android8.0以下设备
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(project(":mapbox"))
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi:0.5.5")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil:0.9.0")

    // 内存泄漏检查
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.12")
}