# DLMapboxUtil
Mapbox for jetpack compose 地图封装（含离线地图）

[![](https://jitpack.io/v/D10NGYANG/DLMapboxUtil.svg)](https://jitpack.io/#D10NGYANG/DLMapboxUtil)

# 使用说明
Step 1. Add the JitPack repository to your build file
```build.gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Step 2. Add the dependency
```build.gradle
dependencies {
  // 地图封装
  implementation("com.github.D10NGYANG:DLMapboxUtil:$release")
  // jetpack compose 框架
  implementation("com.github.D10NGYANG:DLBasicJetpackComposeApp:1.1.5-RC13")
  // 定位工具
  implementation("com.github.D10NGYANG:DLGpsUtil:2.1-RC01")
  // 天地图API
  implementation("com.github.D10NGYANG:DLTianDiTuApi:0.1")
  // 网络请求框架
  implementation("com.github.D10NGYANG:DLHttpUtil:0.3")
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
