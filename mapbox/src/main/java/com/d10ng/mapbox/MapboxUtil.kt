package com.d10ng.mapbox

import com.d10ng.app.status.NetworkStatusManager
import com.d10ng.mapbox.stores.MapboxStore
import com.d10ng.mapbox.utils.Logger
import com.d10ng.tianditu.api.TDTApi
import com.d10ng.tianditu.constant.TokenType
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json

/**
 * 地图工具
 * @Author d10ng
 * @Date 2023/9/16 11:50
 */
object MapboxUtil {

    private val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(TDTApi.useJson)
            }
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        runCatching { Logger.i(message) }
                    }
                }
                level = LogLevel.ALL
            }
            install(HttpTimeout)
        }
    }

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
        NetworkStatusManager.start()
        MapboxStore.init(mapboxToken)
        TDTApi.init(tiandiToken, TokenType.ANDROID, client = client)
        Logger.debug = debug
    }
}