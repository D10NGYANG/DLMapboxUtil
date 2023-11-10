package com.d10ng.mapbox.stores

import android.Manifest
import android.annotation.SuppressLint
import com.d10ng.app.managers.PermissionManager
import com.d10ng.app.status.LocationStatusManager
import com.d10ng.app.status.isLocationEnabled
import com.d10ng.compose.model.UiViewModelManager

/**
 * 定位数据管理器
 * @Author d10ng
 * @Date 2023/9/15 17:45
 */
object LocationStore {

    /** 定位权限标记 */
    private val locationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * 开始定位
     */
    @SuppressLint("MissingPermission")
    suspend fun startRequest() {
        // 判断定位服务是否开启
        if (!isLocationEnabled()) {
            UiViewModelManager.showErrorNotify("定位服务未开启")
            return
        }
        // 判断是否有定位权限
        if (!PermissionManager.request(locationPermission)) {
            UiViewModelManager.showErrorNotify("获取定位权限失败")
            return
        }
        // 开始定位
        LocationStatusManager.start()
    }

    /**
     * 停止定位
     */
    fun stopRequest() {
        LocationStatusManager.stop()
    }

    /**
     * 获取定位数据
     */
    fun getValueFlow() = LocationStatusManager.getStatusFlow()
}