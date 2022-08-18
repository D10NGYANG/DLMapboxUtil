plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
}

group = "com.github.D10NGYANG"
version = "0.7"

android {
    compileSdk = Project.compile_sdk

    defaultConfig {
        minSdk = Project.min_sdk
        targetSdk = Project.target_sdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = compose_ver
    }
    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {

    // 单元测试（可选）
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    // kotlinx.serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlin_serialization_json")

    // MapBox 地图
    api("com.mapbox.maps:android:10.5.0")

    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLBasicJetpackComposeApp:1.2.0-RC03")
    // 定位工具
    implementation("com.github.D10NGYANG:DLGpsUtil:2.1-RC01")
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi:0.3")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil:0.6")
}

afterEvaluate {
    publishing {
        publications {
            create("release", MavenPublication::class) {
                from(components.getByName("release"))
            }
        }
    }
}