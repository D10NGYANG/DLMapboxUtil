package com.d10ng.mapbox.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.d10ng.app.view.lockScreenOrientation
import com.d10ng.app.view.setStatusBar
import com.d10ng.mapbox.stores.LocationStore
import kotlinx.coroutines.launch

/**
 * 基础Activity
 * @Author d10ng
 * @Date 2023/9/15 17:54
 */
open class BaseMapboxActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 锁定屏幕方向
        lockScreenOrientation()
        // 设置状态栏颜色
        setStatusBar()

        // 开启定位
        lifecycleScope.launch { LocationStore.startRequest() }
    }

    override fun onDestroy() {
        // 停止定位
        LocationStore.stopRequest()
        super.onDestroy()
    }
}