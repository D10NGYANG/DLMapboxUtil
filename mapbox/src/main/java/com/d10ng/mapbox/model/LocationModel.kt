package com.d10ng.mapbox.model

import android.Manifest
import androidx.activity.ComponentActivity
import com.d10ng.app.base.PermissionManager
import com.d10ng.gps.ALocationListener
import com.d10ng.gps.isLocationEnabled
import com.d10ng.gps.startRequestLocation
import com.d10ng.gps.stopRequestLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class LocationModel {

    companion object {
        val instant by lazy { LocationModel() }
    }

    /** 定位权限标记 */
    private val locationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /** 是否有定位权限 */
    private val isHasLocationPermissionFlow = MutableStateFlow<Boolean?>(null)

    /** 定位监听器 */
    private val locationGpsListener = ALocationListener()

    /** 定位数据 */
    val locationFlow = locationGpsListener.locationFlow

    /** 检查定位权限 */
    @Synchronized
    private fun checkLocationPermission(act: ComponentActivity, result: (Boolean) -> Unit = {}) {
        if (isHasLocationPermissionFlow.value == true || PermissionManager.has(locationPermission)) {
            if (isHasLocationPermissionFlow.value != true) isHasLocationPermissionFlow.value = true
            result(true)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val isSuccess = PermissionManager.request(locationPermission)
            withContext(Dispatchers.Main) {
                isHasLocationPermissionFlow.value = isSuccess
                result(isSuccess)
            }
        }
    }

    /**
     * 开启获取手机定位
     * - ⚠️仅能在Activity的onCreate函数中使用
     * @param act BaseActivity
     */
    @Synchronized
    fun startRequestLocation(act: ComponentActivity) {
        if (!act.isLocationEnabled()) return
        checkLocationPermission(act) {
            if (it) act.application.startRequestLocation(locationGpsListener)
        }
    }

    /** 关闭获取手机定位 */
    @Synchronized
    fun stopRequestLocation(act: ComponentActivity) {
        act.application.stopRequestLocation(locationGpsListener)
    }
}