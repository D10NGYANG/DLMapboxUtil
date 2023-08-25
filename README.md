# DLMapboxUtil
Mapbox for jetpack compose 地图封装（含离线地图）

# 效果预览
<img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/map.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/map_type.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/map_offline.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/map_offline_add.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/map_offline_add_config.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/search.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/search_sure.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/show.png" width="240"/> <img src="https://github.com/D10NGYANG/DLMapboxUtil/blob/master/image/show_other.png" width="240"/>

# 使用说明
Step 1. 添加仓库
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

Step 2. 添加依赖
```build.gradle
dependencies {
    // 地图封装
    implementation("com.github.D10NGYANG:DLMapboxUtil:1.0.1")
    // jetpack compose 框架
    implementation("com.github.D10NGYANG:DLJetpackComposeUtil:1.3.7")
    // APP通用工具
    implementation("com.github.D10NGYANG:DLAppUtil:2.3.4")
    // 定位工具
    implementation("com.github.D10NGYANG:DLGpsUtil:2.3.5")
    // 经纬度工具
    implementation("com.github.D10NGYANG:DLLatLngUtil-jvm:1.7.5")
    // 天地图API
    implementation("com.github.D10NGYANG:DLTianDiTuApi-jvm:0.5.4")
    // 网络请求框架
    implementation("com.github.D10NGYANG:DLHttpUtil-jvm:0.8.5")
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
