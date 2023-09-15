package com.d10ng.mapbox.constant

import com.d10ng.latlnglib.constant.CoordinateSystemType
import com.d10ng.mapbox.R
import com.mapbox.maps.Style

/**
 * 图层类型
 * @Author: D10NG
 * @Time: 2021/3/5 4:29 下午
 */
enum class MapLayerType(
    // TODO delete
    val intValue: Int,
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
        1,
        "https://oss.irim.online/mob/map/tian_vector.json",
        "天地图标准",
        R.mipmap.img_map_layer_type_1,
        CoordinateSystemType.WGS84,
        false
    ),

    // 天地图 卫星地图
    TD_SATELLITE(
        2,
        "https://oss.irim.online/mob/map/tain_satellite.json",
        "天地图卫星",
        R.mipmap.img_map_layer_type_2,
        CoordinateSystemType.WGS84,
        false
    ),

    // mapbox 标准地图
    MAPBOX_STREETS(
        5,
        Style.LIGHT,
        "Mapbox标准",
        R.mipmap.img_map_layer_type_5,
        CoordinateSystemType.WGS84,
        true
    ),

    // mapbox 卫星地图
    MAPBOX_SATELLITE_STREETS(
        6,
        Style.SATELLITE_STREETS,
        "Mapbox卫星",
        R.mipmap.img_map_layer_type_6,
        CoordinateSystemType.WGS84,
        true
    );


    companion object {

        @JvmStatic
        fun parseByInt(value: Int) = values().find { it.intValue == value } ?: TD_VECTOR

        @JvmStatic
        fun parseBySource(value: String) = values().find { it.source == value } ?: TD_VECTOR
    }
}