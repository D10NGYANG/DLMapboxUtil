package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.MiniButton
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.google.accompanist.insets.navigationBarsPadding
import com.mapbox.geojson.Point

@Composable
fun MapMainScreen(
    controller: NavHostController,
    act: MapActivity
) {
    val model: MapMainScreenViewModel = viewModel(factory = MapMainScreenViewModel.Factory(controller, act))
    val locationText by model.locationTextFlow.collectAsState()
    val layer by model.layerFlow.collectAsState()
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()

    MapMainScreenView(
        locationText = locationText,
        layer = layer,
        zoom = zoom,
        target = target,
        onClickBack = { model.onClickBack() },
        onClickOffline = { model.onClickOffline() },
        onClickZoomIn = { model.onClickZoomIn() },
        onClickZoomOut = { model.onClickZoomOut() },
        onClickLayer = { model.onClickLayer() },
        onClickLocation = { model.onClickLocation() },
        onUpdateZoom = { model.updateZoom(it) },
        onUpdateTarget = { model.updateTarget(it) }
    )
}

@Composable
private fun MapMainScreenView(
    locationText: String,
    layer: MapLayerType,
    zoom: Double,
    target: Point,
    onClickBack: () -> Unit = {},
    onClickOffline: () -> Unit = {},
    onClickZoomIn: () -> Unit = {},
    onClickZoomOut: () -> Unit = {},
    onClickLayer: () -> Unit = {},
    onClickLocation: () -> Unit = {},
    onUpdateZoom: (Double) -> Unit = {},
    onUpdateTarget: (Point) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = "地图", onClickBack = onClickBack) {
            MiniButton(
                text = "离线地图",
                onClick = onClickOffline,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            MapboxView(
                layer = layer,
                cameraZoom = zoom,
                cameraTarget = target,
                onCameraZoomChange = onUpdateZoom,
                onCameraCenterChange = onUpdateTarget
            )

            UserLocationBar(text = locationText)
            Compass()

            MapZoomControllerBar(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 50.dp),
                onClickZoomIn = onClickZoomIn,
                onClickZoomOut = onClickZoomOut
            )

            MapLayerLocationControllerBar(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 50.dp),
                onClickLayer = onClickLayer,
                onClickLocation = onClickLocation
            )
        }
    }
}

@Composable
fun BoxScope.Compass() {
    Image(
        painter = painterResource(id = R.mipmap.ic_map_compass),
        contentDescription = "北向指示",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .padding(16.dp)
            .size(30.dp)
            .align(Alignment.TopEnd)
    )
}

@Composable
private fun BoxScope.UserLocationBar(
    text: String
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xCCFFFFFF), AppShape.RC.v8)
            .padding(8.dp, 4.dp)
            .align(Alignment.TopStart),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = AppText.Normal.Title.v12)
    }
}