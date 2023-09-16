package com.d10ng.mapbox.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.d10ng.app.resource.makeBitmapFromDrawable
import com.d10ng.compose.ui.AppText
import com.d10ng.mapbox.R
import com.d10ng.mapbox.stores.MapViewStore
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions

/**
 * 位置确认视图
 * @Author d10ng
 * @Date 2023/9/16 15:37
 */

private const val TARGET = "TARGET"

@Composable
fun LocationConfirmView(
    label: String,
    description: String,
    point: Point
) {
    Text(
        text = label,
        style = AppText.Normal.Title.default,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth()
    )
    Text(
        text = description,
        style = AppText.Normal.Tips.mini,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    )
    val context = LocalContext.current
    val pointOptions = remember(point) {
        mapOf(
            1 to PointAnnotationOptions()
                .withGeometry(point)
                .withIconImage(TARGET)
                .withIconSize(1.2)
                .withIconOffset(listOf(0.0, -20.0))
        )
    }
    val zoom by MapViewStore.zoomFlow.collectAsState()
    var center by remember(point) {
        mutableStateOf(point)
    }
    val height = LocalConfiguration.current.screenHeightDp.dp / 4
    MapboxView(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(height),
        layer = MapViewStore.getCurrentLayer(),
        cameraZoom = zoom,
        cameraTarget = center,
        onCameraZoomChange = { MapViewStore.updateZoom(it) },
        onCameraCenterChange = { center = it },
        onStyleLoad = { style ->
            context.makeBitmapFromDrawable(R.drawable.ic_map_location_target_25)?.apply {
                style.addImage(TARGET, this)
            }
        },
        pointOptions = pointOptions
    )
}