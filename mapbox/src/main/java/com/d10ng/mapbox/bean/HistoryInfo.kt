package com.d10ng.mapbox.bean

import com.d10ng.tianditu.bean.LocationSearch
import com.d10ng.tianditu.bean.PerimeterSearch
import com.d10ng.tianditu.bean.ReGeocode
import com.mapbox.geojson.Point
import kotlinx.serialization.Serializable

/**
 * 历史记录
 * @Author d10ng
 * @Date 2023/9/16 14:12
 */
@Serializable
data class HistoryInfo(
    /** 名称 */
    val name: String,
    /** 地址 */
    val address: String,
    /** 经度 */
    val longitude: Double,
    /** 纬度 */
    val latitude: Double,
    /** 时间 */
    val time: Long
)

fun createHistoryInfo(
    name: String,
    address: String,
    point: Point
) = HistoryInfo(
    name = name,
    address = address,
    longitude = point.longitude(),
    latitude = point.latitude(),
    time = System.currentTimeMillis()
)

fun PerimeterSearch.Poi.toHistoryInfo(): HistoryInfo {
    val ls = lonlat.split(",")
    return HistoryInfo(
        name = name,
        address = address,
        longitude = ls[0].toDoubleOrNull() ?: 0.0,
        latitude = ls[1].toDoubleOrNull() ?: 0.0,
        time = System.currentTimeMillis()
    )
}

fun LocationSearch.Poi.toHistoryInfo(): HistoryInfo {
    val ls = lonlat.split(",")
    return HistoryInfo(
        name = name,
        address = address,
        longitude = ls[0].toDoubleOrNull() ?: 0.0,
        latitude = ls[1].toDoubleOrNull() ?: 0.0,
        time = System.currentTimeMillis()
    )
}

fun ReGeocode.toHistoryInfo() = HistoryInfo(
    name = result.addressComponent.poi,
    address = result.formattedAddress,
    longitude = result.location.lon,
    latitude = result.location.lat,
    time = System.currentTimeMillis()
)
