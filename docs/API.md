# DLMapboxUtil API 文档

本文档对应 `DLMapboxUtil 1.5.0`，列出宿主应用集成地图功能时需要使用的公开 API。内部实现类和 `internal`/`private` 符号不属于稳定 API。

## 初始化

### `MapboxUtil.init`

```kotlin
object MapboxUtil {
    fun init(
        mapboxToken: String,
        tiandituToken: String,
        isDebug: Boolean = false
    )
}
```

在宿主 `Application` 或启动页面中调用一次。`mapboxToken` 为 Mapbox 公钥，`tiandituToken` 为天地图服务密钥；不要将私钥写入源码或提交到版本库。

## Activity

### `MapActivity`

打开基础地图页，提供图层切换、缩放、当前位置、比例尺和离线地图入口。

### `MapOfflineActivity`

管理离线地图列表。用户可以进入区域选择页下载地图，或编辑、删除已保存区域。

### `LocationSearchActivity`

提供位置搜索、历史记录和经纬度输入流程。

### `MapNavigationActivity`

提供起点/目的地选择和直线导航展示。1.5.0 使用 Navigation 3 管理页面栈。

### `LocationShowActivity`

展示指定经纬度位置。可通过以下扩展函数打开：

```kotlin
fun goToLocationShowActivity(latitude: Double, longitude: Double)
```

## Compose 组件

### `MapboxView`

```kotlin
@Composable
fun MapboxView(
    modifier: Modifier = Modifier,
    layer: MapLayerType = MapLayerType.TD_VECTOR,
    cameraZoom: Double = 10.0,
    cameraTarget: Point = Point.fromLngLat(113.3946198, 23.0374143),
    cameraPadding: EdgeInsets = EdgeInsets(0.0, 0.0, 0.0, 0.0),
    pointOptions: Map<Int, PointAnnotationOptions> = emptyMap(),
    lineOptions: Map<Int, PolylineAnnotationOptions> = emptyMap(),
    isShowUserLocation: Boolean = true,
    onCameraZoomChange: (Double) -> Unit = {},
    onCameraCenterChange: (Point) -> Unit = {},
    onCameraChange: (CoordinateBounds) -> Unit = {},
    onMapClickListener: (MapView, Point) -> Boolean = { _, _ -> false },
    onStyleLoad: (Style) -> Unit = {},
    update: (MapView) -> Unit = {}
)
```

`MapboxView` 是地图渲染入口。`cameraPadding` 使用 Mapbox 的像素单位，适合为底部导航栏或操作面板预留安全空间。外部状态变化会通过平滑 `easeTo` 动画应用到地图。

### `navigationBarCameraPadding`

```kotlin
@Composable
fun navigationBarCameraPadding(additionalBottom: Dp = 0.dp): EdgeInsets
```

根据系统导航栏高度生成底部相机 padding。地图延伸到导航栏时建议将返回值传给 `MapboxView.cameraPadding`。

### 地图浮层组件

- `Compass()`：显示北向指示器。
- `UserLocationTextBar()`：显示当前定位文本。
- `LocationTextBar(text)`：显示自定义位置文本。
- `MapZoomControllerBar(...)`：提供放大/缩小操作。
- `MapLayerLocationControllerBar(...)`：提供图层和当前位置操作。

## 图层

### `MapLayerType`

`MapLayerType` 表示可用地图样式，包含天地图矢量、影像、地形和 Mapbox Streets 等类型。通过 `MapViewStore.updateLayer(layer)` 修改当前样式；读取当前样式可使用 `MapViewStore.layerTypeFlow`。

## 地图状态

### `MapViewStore`

```kotlin
object MapViewStore {
    val layerTypeFlow: StateFlow<MapLayerType>
    val zoomFlow: StateFlow<Double>
    val targetFlow: StateFlow<Point>

    fun updateLayer(layer: MapLayerType)
    fun zoomIn()
    fun zoomOut()
    fun updateZoom(value: Double)
    fun updateTarget(point: Point)
    fun moveToCurrentLocation()
}
```

`moveToCurrentLocation()` 会立即使用已有定位；如果定位尚未返回，则等待首个有效位置后再更新目标点。地图页首次进入时会自动执行同样的初始化流程。

## 构建要求

- compileSdk/targetSdk `36`。
- AGP `8.13.2`，Gradle `8.13`，Kotlin `2.2.20`。
- Java/Kotlin toolchain `17`。
- 宿主应用使用 `useLegacyPackaging = false`，以保留 16 KB 原生库对齐能力。
