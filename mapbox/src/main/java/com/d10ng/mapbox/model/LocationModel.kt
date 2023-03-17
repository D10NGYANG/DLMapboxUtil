package com.d10ng.mapbox.model

import android.Manifest
import com.d10ng.app.app.hasPermissions
import com.d10ng.app.app.requestPermissions
import com.d10ng.compose.BaseActivity
import com.d10ng.gps.ALocationListener
import com.d10ng.gps.isLocationEnabled
import com.d10ng.gps.startRequestLocation
import com.d10ng.gps.stopRequestLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
    val isHasLocationPermissionFlow = MutableStateFlow<Boolean?>(null)

    /** 定位监听器 */
    private val locationGpsListener = ALocationListener()

    /** 定位数据 */
    val locationFlow = locationGpsListener.locationFlow

    /** 检查定位权限 */
    @Synchronized
    fun checkLocationPermission(act: BaseActivity, result: (Boolean) -> Unit = {}) {
        CoroutineScope(Dispatchers.Main).launch {
            if (isHasLocationPermissionFlow.value == true) {
                result(true)
                return@launch
            }
            if (act.hasPermissions(locationPermission)) {
                isHasLocationPermissionFlow.value = true
                result(true)
            } else if (act.requestPermissions(*locationPermission.toList().toTypedArray())){
                isHasLocationPermissionFlow.value = true
                result(true)
            } else {
                isHasLocationPermissionFlow.value = false
                result(false)
            }
        }
    }

    /** 开启获取手机定位 */
    @Synchronized
    fun startRequestLocation(act: BaseActivity) {
        if (!act.isLocationEnabled()) return
        checkLocationPermission(act) {
            if (it) act.application.startRequestLocation(locationGpsListener)
        }
    }

    /** 关闭获取手机定位 */
    @Synchronized
    fun stopRequestLocation(act: BaseActivity) {
        act.application.stopRequestLocation(locationGpsListener)
    }
}