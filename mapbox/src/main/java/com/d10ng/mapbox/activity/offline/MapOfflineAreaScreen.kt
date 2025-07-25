package com.d10ng.mapbox.activity.offline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.d10ng.mapbox.view.NavBarIconButton
import com.d10ng.mapbox.view.PageTransitions
import com.mapbox.geojson.Point
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<OfflineNavGraph>(style = PageTransitions::class)
@Composable
fun MapOfflineAreaScreen(
    nav: DestinationsNavigator,
    model: MapOfflineAreaScreenViewModel = viewModel()
) {
    val layer by model.layerFlow.collectAsState()
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()

    MapOfflineAreaScreenView(
        layer = layer,
        zoom = zoom,
        target = target,
        onClickBack = nav::navigateUp,
        onClickSearch = { model.onClickSearch() },
        onClickZoomIn = { model.onClickZoomIn() },
        onClickZoomOut = { model.onClickZoomOut() },
        onClickLayer = { model.onClickLayer() },
        onClickLocation = { model.onClickLocation() },
        onUpdateZoom = { model.updateZoom(it) },
        onUpdateTarget = { model.updateTarget(it) },
        onClickDownload = { model.onClickDownload(nav) }
    )
}

@Composable
private fun MapOfflineAreaScreenView(
    layer: MapLayerType,
    zoom: Double,
    target: Point,
    onClickBack: () -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickZoomIn: () -> Unit = {},
    onClickZoomOut: () -> Unit = {},
    onClickLayer: () -> Unit = {},
    onClickLocation: () -> Unit = {},
    onUpdateZoom: (Double) -> Unit = {},
    onUpdateTarget: (Point) -> Unit = {},
    onClickDownload: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(title = "地图", onClickBack = onClickBack, titleAlignment = Alignment.CenterStart) {
            NavBarIconButton(
                icon = R.drawable.ic_search_24,
                onClick = onClickSearch
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .align(Alignment.TopCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(30.dp)
                    .padding(top = 30.dp, bottom = 200.dp)
                    .align(Alignment.CenterStart)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(30.dp)
                    .padding(top = 30.dp, bottom = 200.dp)
                    .align(Alignment.CenterEnd)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp, bottom = 200.dp, start = 30.dp, end = 30.dp)
                    .align(Alignment.Center)
                    .border(1.dp, AppColor.Main.primary)
            )

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

            Button(
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
                text = "立即下载",
                onClick = onClickDownload,
                type = ButtonType.PRIMARY,
                shape = AppShape.RC.Cycle
            )
        }
    }
}

@Preview
@Composable
private fun MapOfflineAreaScreenViewPreview() {
    MapOfflineAreaScreenView(
        layer = MapLayerType.TD_VECTOR,
        zoom = 10.0,
        target = Point.fromLngLat(0.0, 0.0)
    )
}