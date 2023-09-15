package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@MapNavGraph(start = true)
@Destination(style = PageTransitions::class)
@Composable
fun MapMainScreen(
    nav: DestinationsNavigator,
    model: MapMainScreenViewModel = viewModel()
) {
    val locationText by model.locationTextFlow.collectAsState()
    val layer by model.layerFlow.collectAsState(initial = MapLayerType.TD_VECTOR)
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()

    MapMainScreenView(
        locationText = locationText,
        layer = layer,
        zoom = zoom,
        target = target,
        onClickBack = { model.onClickBack() },
        onClickOffline = { model.onClickOffline(nav) },
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
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = "地图", onClickBack = onClickBack) {
            Button(
                text = "离线地图",
                onClick = onClickOffline,
                modifier = Modifier
                    .padding(end = 16.dp),
                type = ButtonType.PRIMARY,
                size = ButtonSize.MINI
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            MapboxView(
                modifier = Modifier.fillMaxSize(),
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
fun BoxScope.UserLocationBar(
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
        Text(text = text, style = AppText.Normal.Title.mini)
    }
}