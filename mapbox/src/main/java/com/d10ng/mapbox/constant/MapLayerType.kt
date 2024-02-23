package com.d10ng.mapbox.constant

import com.d10ng.common.coordinate.CoordinateSystemType
import com.d10ng.mapbox.R
import com.mapbox.maps.Style

/**
 * 图层类型
 * @Author: D10NG
 * @Time: 2021/3/5 4:29 下午
 */
enum class MapLayerType(
    // 源地址
    val source: String,
    // 名称
    val label: String,
    // 图片资源ID
    val imgSrcId: Int,
    // 坐标系类型
    val coordinateSystemType: CoordinateSystemType,
    // 是否可下载
    val isCanDown: Boolean
) {

    // 天地图 标准地图
    TD_VECTOR(
        "https://oss.irim.online/mob/map/tian_vector.json",
        "天地图标准",
        R.mipmap.img_map_layer_type_1,
        CoordinateSystemType.WGS84,
        false
    ),

    // 天地图 卫星地图
    TD_SATELLITE(
        "https://oss.irim.online/mob/map/tain_satellite.json",
        "天地图卫星",
        R.mipmap.img_map_layer_type_2,
        CoordinateSystemType.WGS84,
        false
    ),

    // mapbox 标准地图
    MAPBOX_LIGHT(
        Style.LIGHT,
        "Mapbox标准",
        R.mipmap.img_map_layer_type_5,
        CoordinateSystemType.WGS84,
        true
    ),

    // mapbox 卫星地图
    MAPBOX_SATELLITE_STREETS(
        Style.SATELLITE_STREETS,
        "Mapbox卫星",
        R.mipmap.img_map_layer_type_6,
        CoordinateSystemType.WGS84,
        true
    ),

    // mapbox 街道地图 中文标注
    MAPBOX_STREETS_ZH(
        "mapbox://styles/ianjoyce/clsvk6w40004n01o3ani7ft1j",
        "Mapbox街道中文",
        R.mipmap.img_map_layer_type_7,
        CoordinateSystemType.WGS84,
        true
    ),

    // mapbox 卫星地图
    MAPBOX_SATELLITE_STREETS_ZH(
        "mapbox://styles/ianjoyce/clsy40r6a00bs01qo77oi62ik",
        "Mapbox卫星中文",
        R.mipmap.img_map_layer_type_8,
        CoordinateSystemType.WGS84,
        true
    ),

    ;


    companion object {

        @JvmStatic
        fun parseBySource(value: String) = entries.find { it.source == value } ?: TD_VECTOR
    }
}