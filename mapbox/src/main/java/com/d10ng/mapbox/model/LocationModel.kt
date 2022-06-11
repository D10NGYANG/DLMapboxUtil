package com.d10ng.mapbox.model

import android.Manifest
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.hasPermissions
import com.d10ng.coroutines.launchMain
import com.d10ng.gpslib.ALocationListener
import com.d10ng.gpslib.isLocationEnabled
import com.d10ng.gpslib.startRequestLocation
import com.d10ng.gpslib.stopRequestLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

internal class LocationModel {

    companion object {
        val instant by lazy { LocationModel() }
    }

    /** 定位权限标记 */
    private val locationPermission = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    /** 是否有定位权限 */
    val isHasLocationPermissionFlow = MutableStateFlow<Boolean?>(null)

    /** 定位监听器 */
    private val locationGpsListener = ALocationListener()

    /** 定位数据 */
    val locationFlow = locationGpsListener.locationLive

    /** 检查定位权限 */
    @Synchronized
    fun checkLocationPermission(act: BaseActivity, result: (Boolean) -> Unit = {}) {
        launchMain {
            if (isHasLocationPermissionFlow.value == true) {
                result(true)
                return@launchMain
            }
            if (act.hasPermissions(locationPermission)) {
                isHasLocationPermissionFlow.update { true }
                result(true)
            } else {
                act.reqPermissions(locationPermission, {
                    isHasLocationPermissionFlow.update { true }
                    result(true)
                }, {
                    isHasLocationPermissionFlow.update { false }
                    result(false)
                })
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