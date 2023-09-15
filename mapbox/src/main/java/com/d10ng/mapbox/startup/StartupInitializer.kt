package com.d10ng.mapbox.startup

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.d10ng.mapbox.stores.MapboxStore

/**
 * 启动初始化
 * @Author d10ng
 * @Date 2023/9/15 18:28
 */
class StartupInitializer : Initializer<Unit> {

    companion object {
        lateinit var application: Application
    }

    override fun create(context: Context) {
        application = context as Application
        MapboxStore.init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}