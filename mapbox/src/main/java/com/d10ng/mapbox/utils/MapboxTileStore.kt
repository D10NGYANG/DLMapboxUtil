package com.d10ng.mapbox.utils

import android.content.Context
import android.graphics.Bitmap
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.d10ng.mapbox.constant.MapLayerType
import com.mapbox.bindgen.Value
import com.mapbox.common.TileRegion
import com.mapbox.common.TileStore
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun TileStore.getAllOfflineInfo(): List<OfflineMapInfo> {
    val regions = getAllTileRegions()?: return emptyList()
    val result = mutableListOf<OfflineMapInfo>()
    for (region in regions) {
        val item = OfflineMapInfo(region)
        val geometry = getTileRegionGeometry(region.id)?: continue
        item.geometry = geometry
        val json = getTileRegionMetadata(region.id)?.contents?: continue
        if (json !is String) continue
        val obj = JSONObject(json)
        item.style = MapLayerType.parseBySource(obj.getString("style"))
        item.title = obj.getString("title")
        item.minZoom = obj.getInt("minZoom")
        item.maxZoom = obj.getInt("maxZoom")
        result.add(item)
    }
    return result
}

suspend fun TileStore.getAllTileRegions(): List<TileRegion>? {
    return suspendCoroutine { cont ->
        getAllTileRegions() { ex ->
            try {
                if (ex.isValue) {
                    cont.resume(ex.value)
                } else {
                    cont.resume(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

suspend fun TileStore.getTileRegion(id: String): TileRegion? {
    return suspendCoroutine { cont ->
        getTileRegion(id) { ex ->
            try {
                if (ex.isValue) {
                    cont.resume(ex.value)
                } else {
                    cont.resume(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

suspend fun TileStore.getTileRegionGeometry(id: String): Geometry? {
    return suspendCoroutine { cont ->
        getTileRegionGeometry(id) { ex ->
            try {
                if (ex.isValue) {
                    cont.resume(ex.value)
                } else {
                    cont.resume(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

suspend fun TileStore.getTileRegionMetadata(id: String): Value? {
    return suspendCoroutine { cont ->
        getTileRegionMetadata(id) { ex ->
            try {
                if (ex.isValue) {
                    cont.resume(ex.value)
                } else {
                    cont.resume(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

suspend fun OfflineMapInfo.getSnapshotBitmap(
    context: Context,
    width: Float = 160f,
    height: Float = 160f
): Bitmap? {
    val snapshotMapOptions = MapSnapshotOptions.Builder()
        .size(Size(width, height))
        .resourceOptions(MapInitOptions.getDefaultResourceOptions(context))
        .build()
    val shotter = Snapshotter(context, snapshotMapOptions).apply {
        setCamera(
            CameraOptions.Builder()
                .center(this@getSnapshotBitmap.geometry as Point)
                .zoom(this@getSnapshotBitmap.minZoom.toDouble())
                .build()
        )
        setStyleUri(this@getSnapshotBitmap.style.source)
    }
    return suspendCancellableCoroutine { cont ->
        shotter.start {
            if (it == null) cont.resume(null)
            else {
                cont.resume(it.bitmap())
            }
            shotter.destroy()
        }
    }
}