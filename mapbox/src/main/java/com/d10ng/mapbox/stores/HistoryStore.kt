package com.d10ng.mapbox.stores

import com.d10ng.common.transform.json
import com.d10ng.mapbox.bean.HistoryInfo
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

/**
 * 历史记录
 * @Author d10ng
 * @Date 2023/9/16 14:09
 */
object HistoryStore {

    // 历史记录列表
    val valueFlow = MapboxConfigDataStore.instant.getHistoryFlow().map {
        it?.map { item ->
            json.decodeFromString<HistoryInfo>(item)
        }?.sortedByDescending { item -> item.time } ?: listOf()
    }

    /**
     * 添加历史记录
     * @param info HistoryInfo
     */
    suspend fun add(info: HistoryInfo) {
        val str = json.encodeToString(info)
        val value = MapboxConfigDataStore.instant.getHistory()?.toMutableSet() ?: mutableSetOf()
        if (value.contains(str)) {
            val temp = info.copy(time = System.currentTimeMillis())
            value.remove(str)
            value.add(json.encodeToString(temp))
        } else {
            value.add(str)
        }
        MapboxConfigDataStore.instant.setHistory(value)
    }

    /**
     * 删除历史记录
     * @param info HistoryInfo
     */
    suspend fun remove(info: HistoryInfo) {
        val str = json.encodeToString(info)
        val value = MapboxConfigDataStore.instant.getHistory()?.toMutableSet() ?: mutableSetOf()
        value.remove(str)
        MapboxConfigDataStore.instant.setHistory(value)
    }

    /**
     * 清空历史记录
     */
    suspend fun clear() {
        MapboxConfigDataStore.instant.setHistory(setOf())
    }
}