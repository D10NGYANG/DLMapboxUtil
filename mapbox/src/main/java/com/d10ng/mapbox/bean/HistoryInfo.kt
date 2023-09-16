package com.d10ng.mapbox.bean

import com.d10ng.tianditu.bean.LocationSearch
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
