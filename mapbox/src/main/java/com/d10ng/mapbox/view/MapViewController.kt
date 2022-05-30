package com.d10ng.mapbox.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        modifier = modifier.clickable { onClick.invoke() }
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
            iconID = R.drawable.ic_map_zoom_in_35,
            description = "放大",
            onClick = onClickZoomIn
        )
        MapController(
            modifier = Modifier.padding(top = 16.dp),
            iconID = R.drawable.ic_map_zoom_out_35,
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
            iconID = R.drawable.ic_map_layer_35,
            description = "图层",
            onClick = onClickLayer
        )
        MapController(
            modifier = Modifier.padding(top = 16.dp),
            iconID = R.drawable.ic_map_location_35,
            description = "位置",
            onClick = onClickLocation
        )
    }
}