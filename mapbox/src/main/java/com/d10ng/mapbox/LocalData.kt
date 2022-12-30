package com.d10ng.mapbox

import android.content.Context
import com.d10ng.applib.system.SpfUtils
import com.d10ng.mapbox.constant.MapLayerType

/** 默认表单 */
private const val SP_CONFIG = "map_box_config_data"

/**
 * 获取本地存储器工具
 * @receiver Context
 * @param spName String
 * @return SharedPreferences
 */
fun Context.getSpf(spName: String = SP_CONFIG) = SpfUtils.instant(this, spName).getSpf()

/** 地图图层类型 */
private const val SPF_MAP_LAYER_TYPE = "map_layer_type"
fun Context.getSpfMapLayerType() =
    MapLayerType.parseByInt(getSpf().getInt(SPF_MAP_LAYER_TYPE, MapLayerType.TIAN_VECTOR.intValue))

fun Context.setSpfMapLayerType(type: MapLayerType) {
    getSpf().edit().putInt(SPF_MAP_LAYER_TYPE, type.intValue).apply()
}