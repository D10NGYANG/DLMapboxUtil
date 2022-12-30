package com.d10ng.mapbox.model

import android.content.Context
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.getSpfMapLayerType
import com.d10ng.mapbox.setSpfMapLayerType
import com.d10ng.mapbox.view.MAP_BOX_ZOOM_DEFAULT
import com.d10ng.mapbox.view.MAP_BOX_ZOOM_MAX
import com.d10ng.mapbox.view.MAP_BOX_ZOOM_MIN
import com.mapbox.geojson.Point
import kotlinx.coroutines.flow.MutableStateFlow

class MapModel {

    companion object {
        val instant by lazy { MapModel() }

        /** 默认经纬度位置 */
        val defaultLngLat = Point.fromLngLat(113.031227, 23.153469)
    }

    /** 图层样式 */
    val layerTypeFlow = MutableStateFlow(MapLayerType.TIAN_VECTOR)

    /** 缩放比例 */
    val zoomFlow = MutableStateFlow(MAP_BOX_ZOOM_DEFAULT)

    /** 地图中心 */
    val targetFlow = MutableStateFlow(defaultLngLat)

    /** 初始化地图样式 */
    fun initLayer(context: Context) {
        layerTypeFlow.value = context.getSpfMapLayerType()
    }

    /** 更新地图样式 */
    fun updateLayer(context: Context, layer: MapLayerType) {
        layerTypeFlow.value = layer
        context.setSpfMapLayerType(layer)
    }

    /** 放大 */
    fun zoomIn() {
        if (zoomFlow.value < MAP_BOX_ZOOM_MAX) {
            zoomFlow.value = (zoomFlow.value + 1).coerceAtMost(MAP_BOX_ZOOM_MAX)
        }
    }

    /** 缩小 */
    fun zoomOut() {
        if (zoomFlow.value > MAP_BOX_ZOOM_MIN) {
            zoomFlow.value = (zoomFlow.value - 1).coerceAtLeast(MAP_BOX_ZOOM_MIN)
        }
    }

    /** 更新比例 */
    fun updateZoom(value: Double) {
        zoomFlow.value = value.coerceAtLeast(MAP_BOX_ZOOM_MIN).coerceAtMost(MAP_BOX_ZOOM_MAX)
    }

    /** 更新地图中心 */
    fun updateTarget(point: Point) {
        targetFlow.value = point
    }

    /** 移动地图中心到当前位置 */
    fun move2CurrentLocation() {
        LocationModel.instant.locationFlow.value?.apply {
            targetFlow.value = Point.fromLngLat(this.longitude, this.latitude)
        }
    }
}