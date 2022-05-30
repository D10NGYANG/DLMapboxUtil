package com.d10ng.mapbox.model

import android.Manifest
import android.os.Build
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

        /** 前台权限 */
        private val permissionFront = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        /** 后台权限 */
        private val permissionBack = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    /** 全部权限 */
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        permissionFront.plus(permissionBack)
    } else {
        permissionFront
    }

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
            if (act.hasPermissions(permissions)) {
                isHasLocationPermissionFlow.update { true }
                result(true)
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                act.reqPermissions(permissions, {
                    isHasLocationPermissionFlow.update { true }
                    result(true)
                }, {
                    isHasLocationPermissionFlow.update { false }
                    result(false)
                })
            } else {
                act.reqPermissions(permissionFront, {
                    act.reqPermissions(permissionBack, {
                        isHasLocationPermissionFlow.update { true }
                        result(true)
                    }, {
                        isHasLocationPermissionFlow.update { false }
                        result(false)
                    })
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