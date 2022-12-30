# DLMapboxUtil
Mapbox for jetpack compose 地图封装（含离线地图）

# 使用说明
Step 1. Add the JitPack repository to your build file
```build.gradle
allprojects {
  repositories {
    ...
    // 0.8版本(含)之前使用jitpack
    maven { url 'https://jitpack.io' }
    // 0.9.2版本以后不再使用jitpack
    maven { url "https://raw.githubusercontent.com/D10NGYANG/maven-repo/main/repository" }
  }
}
```
Step 2. Add the dependency
```build.gradle
dependencies {
    // 地图封装
    implementation("com.github.D10NGYANG:DLMapboxUtil:0.9.3")
    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLBasicJetpackComposeApp:1.2.3")
    // 日期工具兼容Android8.0以下设备
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.2.0")
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi-jvm:0.4")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil-jvm:0.7")
}
```

# 代码
```kotlin
class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initColor()
        MapboxModel.instant.init("pk.eyJ1IjoiaXJpbTEwMCIsIm-----SA")
        Http.init("", true)
        TianDiTuApiManager.init("1111111111")

        setContent {
            var selectPoint by remember {
                mutableStateOf(Point.fromLngLat(113.22, 22.33))
            }
            AppTheme(app = app) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColor.System.background)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SolidButtonWithText(text = "地图", onClick = {
                        goTo(MapActivity::class.java)
                    })
                    SolidButtonWithText(text = "搜索", onClick = {
                        LocationSearchManager.instant.startActivity(this@MainActivity) {
                            if (it != null) {
                                selectPoint = it
                            }
                        }
                    })
                    SolidButtonWithText(text = "显示", onClick = {
                        goToLocationShowActivity(selectPoint.latitude(), selectPoint.longitude())
                    })
                }
            }
        }
    }
}
```
