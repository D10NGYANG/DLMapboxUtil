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
            abiFilters.addAll(mutableSetOf(/*"arm64-v8a", */"armeabi-v7a"))
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = compose_compiler_ver
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLBasicJetpackComposeApp:1.2.3")
    // 日期工具兼容Android8.0以下设备
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.0")

    implementation(project(":mapbox"))
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi-jvm:0.4")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil-jvm:0.7")

    // 内存泄漏检查
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}