package com.d10ng.mapbox.view

import android.view.Gravity
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.d10ng.mapbox.R
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.utils.Logger
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CoordinateBounds
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.ImageHolder
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotation
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import com.mapbox.maps.plugin.attribution.attribution
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.logo
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.maps.toCameraOptions
import java.util.concurrent.atomic.AtomicLong

@Composable
fun MapboxView(
    modifier: Modifier = Modifier,
    layer: MapLayerType = MapLayerType.TD_VECTOR,
    cameraZoom: Double = 10.0,
    cameraTarget: Point = Point.fromLngLat(113.3946198, 23.0374143),
    cameraPadding: EdgeInsets = EdgeInsets(0.0, 0.0, 0.0, 0.0),
    pointOptions: Map<Int, PointAnnotationOptions> = mapOf(),
    lineOptions: Map<Int, PolylineAnnotationOptions> = mapOf(),
    isShowUserLocation: Boolean = true,
    onCameraZoomChange: (Double) -> Unit = {},
    onCameraCenterChange: (Point) -> Unit = {},
    onCameraChange: (CoordinateBounds) -> Unit = {},
    onMapClickListener: (MapView, Point) -> Boolean = { _, _ -> false },
    onGesturesMoveListener: (MapView, MoveGestureDetector, Boolean) -> Unit = { _, _, _ -> },
    onStyleLoad: (Style) -> Unit = {},
    update: (MapView) -> Unit = {}
) {
    var mapView by remember {
        mutableStateOf<MapView?>(null)
    }
    // Camera callbacks are emitted for every animation frame. Do not feed those
    // intermediate values back into Compose while applying an external target.
    val cameraAnimationGeneration = remember { AtomicLong(0L) }
    // 图标管理器
    val pointManager = remember(mapView) {
        mapView?.annotations?.createPointAnnotationManager()
    }
    // 已创建图标列表
    val pointAnnotations = remember {
        mutableStateMapOf<Int, PointAnnotation>()
    }
    // 上次图标配置列表
    var oldPointOptions by remember {
        mutableStateOf<Map<Int, PointAnnotationOptions>?>(null)
    }
    // 线条管理器
    val lineManager = remember(mapView) {
        mapView?.annotations?.createPolylineAnnotationManager()
    }
    // 已创建线条列表
    val lineAnnotations = remember {
        mutableStateMapOf<Int, PolylineAnnotation>()
    }
    // 上次线条配置列表
    var oldLineOptions by remember {
        mutableStateOf<Map<Int, PolylineAnnotationOptions>?>(null)
    }

    remember(mapView) {
        oldPointOptions = null
        oldLineOptions = null
        true
    }

    AndroidView(
        factory = { context ->
            MapView(
                context
            ).apply {
                // 不显示地图官方LOGO
                logo.updateSettings { enabled = false }
                // 不显示地图官方LOGO隔壁的图标
                attribution.updateSettings { enabled = false }
                // 不支持旋转
                gestures.updateSettings { rotateEnabled = false }
                compass.updateSettings {
                    // 不显示指南针
                    enabled = false
                    // 取消正北方向时不显示指南针的效果
                    fadeWhenFacingNorth = false
                    // 指南针图标
                    image = ImageHolder.from(R.mipmap.ic_map_compass)
                }
                // 将比例尺移动到左下角
                scalebar.updateSettings {
                    position = Gravity.BOTTOM or Gravity.START
                    marginBottom = cameraPadding.bottom.toFloat() + DEFAULT_SCALE_BAR_MARGIN_PX
                }
                // 开启用户当前位置显示
                location.updateSettings { enabled = isShowUserLocation }
                // 监听缩放
                camera.addCameraZoomChangeListener {
                    if (cameraAnimationGeneration.get() == 0L) {
                        onCameraZoomChange.invoke(it)
                    }
                }
                // 监听移动
                camera.addCameraCenterChangeListener {
                    if (cameraAnimationGeneration.get() == 0L) {
                        onCameraCenterChange.invoke(it)
                    }
                }
                // 监听是否被手指触摸
                gestures.addOnMoveListener(object : OnMoveListener {
                    override fun onMove(detector: MoveGestureDetector): Boolean {
                        return false
                    }

                    override fun onMoveBegin(detector: MoveGestureDetector) {
                        onGesturesMoveListener(this@apply, detector, true)
                    }

                    override fun onMoveEnd(detector: MoveGestureDetector) {
                        onGesturesMoveListener(this@apply, detector, false)
                    }
                })
                // 镜头改变
                getMapboxMap().addOnCameraChangeListener {
                    val bounds =
                        getMapboxMap().coordinateBoundsForCamera(getMapboxMap().cameraState.toCameraOptions())
                    onCameraChange.invoke(bounds)
                }
                // 监听地图点击
                getMapboxMap().addOnMapClickListener { point ->
                    onMapClickListener.invoke(this, point)
                }
            }
        },
        modifier = modifier,
        update = { map ->
            mapView = map
            val mapbox = map.getMapboxMap()
            map.scalebar.updateSettings {
                marginBottom = cameraPadding.bottom.toFloat() + DEFAULT_SCALE_BAR_MARGIN_PX
            }
            // 设置地图样式
            if (mapbox.getStyle()?.styleURI != layer.source) {
                Logger.i("MapboxView 设置地图样式 ${layer.source}")
                mapbox.loadStyleUri(layer.source) { style ->
                    onStyleLoad.invoke(style)
                }
            }
            // 设置图标
            if (oldPointOptions != pointOptions) {
                oldPointOptions = pointOptions
                pointManager?.let { manager ->
                    // 先将没有的图标删除
                    val deleteList = pointAnnotations.filter { s ->
                        !pointOptions.containsKey(s.key)
                    }
                    Logger.i("MapboxView 先将没有的图标删除, size=${deleteList.size}")
                    manager.delete(deleteList.values.toList())
                    deleteList.forEach { pointAnnotations.remove(it.key) }
                    // 将已经有的图标进行调整
                    val oldList: List<PointAnnotation> = pointAnnotations.map { s ->
                        pointOptions[s.key]?.let { s.value.updateByOptions(it) }
                        s.value
                    }
                    Logger.i("MapboxView 将已经有的图标进行调整, size=${oldList.size}")
                    manager.update(oldList)
                    // 添加新增的图标
                    val newList = pointOptions.filter { p ->
                        !pointAnnotations.containsKey(p.key)
                    }
                    Logger.i("MapboxView 添加新增的图标, size=${newList.size}")
                    newList.forEach { pointAnnotations[it.key] = manager.create(it.value) }
                }
            }
            // 设置线条
            if (oldLineOptions != lineOptions) {
                oldLineOptions = lineOptions
                lineManager?.let { manager ->
                    // 先将没有的线条删除
                    val deleteList = lineAnnotations.filter { s ->
                        !lineOptions.containsKey(s.key)
                    }
                    Logger.i("MapboxView 先将没有的线条删除, size=${deleteList.size}")
                    manager.delete(deleteList.values.toList())
                    deleteList.forEach { lineAnnotations.remove(it.key) }
                    // 将已经有的线条进行调整
                    val oldList: List<PolylineAnnotation> = lineAnnotations.map { s ->
                        lineOptions[s.key]?.let { s.value.updateByOptions(it) }
                        s.value
                    }
                    Logger.i("MapboxView 将已经有的线条进行调整, size=${oldList.size}")
                    manager.update(oldList)
                    // 添加新增的线条
                    val newList = lineOptions.filter { p ->
                        !lineAnnotations.containsKey(p.key)
                    }
                    Logger.i("MapboxView 添加新增的线条, size=${newList.size}")
                    newList.forEach { lineAnnotations[it.key] = manager.create(it.value) }
                }
            }
            // 自定义更新
            update.invoke(map)
        },
        onRelease = { map ->
            if (mapView === map) mapView = null
        }
    )

    // Keep camera synchronization independent from AndroidView's update timing.
    // This is important when the first location arrives while the native view is
    // still being attached: the target change will be replayed once mapView exists.
    LaunchedEffect(mapView, cameraZoom, cameraTarget, cameraPadding) {
        val map = mapView ?: return@LaunchedEffect
        val mapbox = map.getMapboxMap()
        val camera = mapbox.cameraState
        if (!shouldUpdateCamera(camera.zoom, cameraZoom, camera.center, cameraTarget, camera.padding, cameraPadding)) {
            return@LaunchedEffect
        }
        Logger.i("MapboxView 设置显示位置和缩放比例 $cameraZoom, $cameraTarget")
        val position = CameraOptions.Builder()
            .zoom(cameraZoom)
            .center(cameraTarget)
            .padding(cameraPadding)
            .build()
        val animationOptions = MapAnimationOptions.Builder()
            .duration(CAMERA_ANIMATION_DURATION_MS)
            .build()
        val animationGeneration = cameraAnimationGeneration.incrementAndGet()
        map.camera.easeTo(
            position,
            animationOptions,
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    cameraAnimationGeneration.compareAndSet(animationGeneration, 0L)
                }

                override fun onAnimationCancel(animation: Animator) {
                    cameraAnimationGeneration.compareAndSet(animationGeneration, 0L)
                }
            }
        )
    }
}

private const val DEFAULT_SCALE_BAR_MARGIN_PX = 4f
private const val CAMERA_ANIMATION_DURATION_MS = 450L

/** Keeps the camera target centered inside the area above the system navigation bar. */
@Composable
fun navigationBarCameraPadding(additionalBottom: Dp = 0.dp): EdgeInsets {
    val density = LocalDensity.current
    val bottom = with(density) {
        WindowInsets.navigationBars.getBottom(this).toDouble() + additionalBottom.toPx()
    }
    return remember(bottom) { EdgeInsets(0.0, 0.0, bottom, 0.0) }
}

/**
 * 图标更新信息
 * @receiver PointAnnotation
 * @param options PointAnnotationOptions
 */
private fun PointAnnotation.updateByOptions(options: PointAnnotationOptions) {
    if (options.getPoint() != null) this.point = options.getPoint()!!
    if (options.symbolSortKey != null) symbolSortKey = options.symbolSortKey
    if (options.iconSize != null) iconSize = options.iconSize
    if (options.iconImage != null) iconImage = options.iconImage
    if (options.iconRotate != null) iconRotate = options.iconRotate
    if (!options.iconOffset.isNullOrEmpty()) iconOffset = options.iconOffset
    if (options.iconAnchor != null) iconAnchor = options.iconAnchor
    if (options.textField != null) textField = options.textField
    if (options.textSize != null) textSize = options.textSize
    if (options.textMaxWidth != null) textMaxWidth = options.textMaxWidth
    if (options.textLetterSpacing != null) textLetterSpacing = options.textLetterSpacing
    if (options.textJustify != null) textJustify = options.textJustify
    if (options.textRadialOffset != null) textRadialOffset = options.textRadialOffset
    if (options.textAnchor != null) textAnchor = options.textAnchor
    if (options.textRotate != null) textRotate = options.textRotate
    if (options.textTransform != null) textTransform = options.textTransform
    if (!options.textOffset.isNullOrEmpty()) textOffset = options.textOffset
    if (options.iconOpacity != null) iconOpacity = options.iconOpacity
    if (options.iconColor != null) iconColorString = options.iconColor
    if (options.iconHaloColor != null) iconHaloColorString = options.iconHaloColor
    if (options.iconHaloWidth != null) iconHaloWidth = options.iconHaloWidth
    if (options.iconHaloBlur != null) iconHaloBlur = options.iconHaloBlur
    if (options.textOpacity != null) textOpacity = options.textOpacity
    if (options.textColor != null) textColorString = options.textColor
    if (options.textHaloColor != null) textHaloColorString = options.textHaloColor
    if (options.textHaloWidth != null) textHaloWidth = options.textHaloWidth
    if (options.textHaloBlur != null) textHaloBlur = options.textHaloBlur
}

/**
 * 线条信息更新
 * @receiver Line
 * @param options LineOptions
 */
private fun PolylineAnnotation.updateByOptions(options: PolylineAnnotationOptions) {
    if (options.lineJoin != null) lineJoin = options.lineJoin
    if (options.lineOpacity != null) lineOpacity = options.lineOpacity
    if (options.lineColor != null) lineColorString = options.lineColor
    if (options.lineWidth != null) lineWidth = options.lineWidth
    if (options.lineGapWidth != null) lineGapWidth = options.lineGapWidth
    if (options.lineOffset != null) lineOffset = options.lineOffset
    if (options.lineBlur != null) lineBlur = options.lineBlur
    if (options.linePattern != null) linePattern = options.linePattern
    points = options.getPoints()
    isDraggable = options.getDraggable()
}
