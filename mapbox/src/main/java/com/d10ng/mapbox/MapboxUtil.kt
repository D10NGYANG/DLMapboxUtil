package com.d10ng.mapbox

import com.d10ng.mapbox.stores.MapboxStore
import com.d10ng.mapbox.utils.Logger
import com.d10ng.tianditu.TianDiTuApiManager

/**
 * 地图工具
 * @Author d10ng
 * @Date 2023/9/16 11:50
 */
object MapboxUtil {

    /**
     * 初始化
     * @param mapboxToken String
     * @param tiandiToken String
     * @param debug Boolean
     */
    fun init(
        mapboxToken: String,
        tiandiToken: String,
        debug: Boolean = false
    ) {
        MapboxStore.init(mapboxToken)
        TianDiTuApiManager.init(tiandiToken, debug = debug)
        Logger.debug = debug
    }
}