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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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

    // Android
    implementation("androidx.core:core-ktx:1.9.0")

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:1.3.0")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:2.2.2")
    // 日期工具兼容Android8.0以下设备
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.2")

    implementation(project(":mapbox"))
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi-jvm:0.5.0")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil-jvm:0.8.0")

    // 内存泄漏检查
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")
}