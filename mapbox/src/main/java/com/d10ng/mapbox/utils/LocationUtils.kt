package com.d10ng.mapbox.utils

import android.location.Location
import com.d10ng.latlnglib.toLatitudeString
import com.d10ng.latlnglib.toLongitudeString
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