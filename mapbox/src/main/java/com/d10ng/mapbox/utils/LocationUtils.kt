package com.d10ng.mapbox.utils

import android.location.Location
import com.d10ng.common.coordinate.toLatitudeString
import com.d10ng.common.coordinate.toLongitudeString
import com.d10ng.tianditu.bean.LocationSearch
import com.d10ng.tianditu.bean.PerimeterSearch
import com.mapbox.geojson.Point

/**
 * 位置数据处理
 * @Author d10ng
 * @Date 2023/9/16 10:39
 */

/**
 * 将位置数据转换为显示字符串
 * @receiver Location
 * @param prefix String 前缀
 * @param pattern String 格式
 * @return String
 */
fun Location.toShowString(prefix: String = "位置:", pattern: String = "Fd°m′S.ss″") =
    toShowLocationText(latitude, longitude, prefix, pattern)

/**
 * 将位置数据转换为显示字符串
 * @receiver Point
 * @param prefix String 前缀
 * @param pattern String 格式
 * @return String
 */
fun Point.toShowString(prefix: String = "位置:", pattern: String = "Fd°m′S.ss″") =
    toShowLocationText(latitude(), longitude(), prefix, pattern)

/**
 * 将位置数据转换为显示字符串
 * @param lat Double
 * @param lng Double
 * @param prefix String
 * @param pattern String
 * @return String
 */
fun toShowLocationText(
    lat: Double,
    lng: Double,
    prefix: String = "位置:",
    pattern: String = "Fd°m′S.ss″"
): String {
    val builder = StringBuilder(prefix)
    builder.append(lat.toLatitudeString(pattern))
        .append(",")
        .append(lng.toLongitudeString(pattern))
    return builder.toString()
}

/**
 * 将 Poi 转换为 Point
 * @receiver LocationSearch.Poi
 * @return Point
 */
fun LocationSearch.Poi.toPoint() = lonlat.toPoint()

/**
 * 将 Poi 转换为 Point
 * @receiverPerimeterSearch.Poi
 * @return Point
 */
fun PerimeterSearch.Poi.toPoint() = lonlat.toPoint()

/**
 * 将格式为"经度,纬度"的字符串转换为 Point
 * @receiver String
 * @return Point
 */
private fun String.toPoint(): Point {
    val ls = split(",")
    val lng = ls[0].toDoubleOrNull() ?: 0.0
    val lat = ls[1].toDoubleOrNull() ?: 0.0
    return Point.fromLngLat(lng, lat)
}