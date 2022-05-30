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
    val intValue: Int,
    val source: String,
    val nameSrcId: Int,
    val imgSrcId: Int,
    val coordinateSystemType: CoordinateSystemType,
    val isCanDown: Boolean
) {

    // 天地图 标准地图
    TIAN_VECTOR(
        1,
        "https://oss.irim.online/mob/map/tian_vector.json",
        R.string.map_type_1,
        R.mipmap.img_map_layer_type_1,
        CoordinateSystemType.WGS84,
        false
    ),

    // 天地图 卫星地图
    TIAN_SATELLITE(
        2,
        "https://oss.irim.online/mob/map/tain_satellite.json",
        R.string.map_type_2,
        R.mipmap.img_map_layer_type_2,
        CoordinateSystemType.WGS84,
        false
    ),

    // 高德 标准地图
    /*GD_NORMAL(
        3,
        "https://oss.irim.online/mob/map/gd_vector.json",
        R.string.map_type_3,
        R.mipmap.img_map_layer_type_1,
        CoordinateSystemType.GCJ02,
        true
    ),*/

    // 高德 卫星地图
    /*GD_SATELLITE(
        4,
        "https://oss.irim.online/mob/map/gd_satellite.json",
        R.string.map_type_4,
        R.mipmap.img_map_layer_type_2,
        CoordinateSystemType.GCJ02,
        true
    ),*/

    // mapbox 标准地图
    MAPBOX_STREETS(
        5,
        Style.LIGHT,
        R.string.map_type_5,
        R.mipmap.img_map_layer_type_5,
        CoordinateSystemType.WGS84,
        true
    ),

    // mapbox 卫星地图
    MAPBOX_SATELLITE_STREETS(
        6,
        Style.SATELLITE_STREETS,
        R.string.map_type_6,
        R.mipmap.img_map_layer_type_6,
        CoordinateSystemType.WGS84,
        true
    );

    // 测试
    //SATELLITE(2, "asset://map_5.json", R.mipmap.img_map_layer_type_2);


    companion object {

        @JvmStatic
        fun parseByInt(value: Int) = values().find { it.intValue == value }?: TIAN_VECTOR

        @JvmStatic
        fun parseBySource(value: String) = values().find { it.source == value }?: TIAN_VECTOR
    }
}