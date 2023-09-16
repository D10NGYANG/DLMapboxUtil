package com.d10ng.mapbox.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape
import com.d10ng.mapbox.R

@Composable
fun MapController(
    modifier: Modifier = Modifier,
    iconID: Int,
    description: String,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = iconID),
        contentDescription = description,
        modifier = modifier
            .clip(AppShape.RC.v8)
            .clickable { onClick.invoke() }
    )
}

@Composable
fun MapZoomControllerBar(
    modifier: Modifier,
    onClickZoomIn: () -> Unit,
    onClickZoomOut: () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        MapController(
            iconID = R.drawable.ic_map_zoom_in_44,
            description = "放大",
            onClick = onClickZoomIn
        )
        MapController(
            modifier = Modifier.padding(top = 16.dp),
            iconID = R.drawable.ic_map_zoom_out_44,
            description = "缩小",
            onClick = onClickZoomOut
        )
    }
}

@Composable
fun MapLayerLocationControllerBar(
    modifier: Modifier,
    onClickLayer: () -> Unit,
    onClickLocation: () -> Unit
) {

    Column(
        modifier = modifier
    ) {
        MapController(
            iconID = R.drawable.ic_map_layer_44,
            description = "图层",
            onClick = onClickLayer
        )
        MapController(
            modifier = Modifier.padding(top = 16.dp),
            iconID = R.drawable.ic_map_location_44,
            description = "位置",
            onClick = onClickLocation
        )
    }
}