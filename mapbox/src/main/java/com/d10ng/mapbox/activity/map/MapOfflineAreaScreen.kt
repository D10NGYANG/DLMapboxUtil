package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.d10ng.mapbox.view.SureButton
import com.google.accompanist.insets.navigationBarsPadding
import com.mapbox.geojson.Point

@Composable
fun MapOfflineAreaScreen(
    controller: NavHostController,
    act: MapActivity
) {
    val model: MapOfflineAreaScreenViewModel = viewModel(factory = MapOfflineAreaScreenViewModel.Factory(controller, act))
    val layer by model.layerFlow.collectAsState()
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()

    MapOfflineAreaScreenView(
        layer = layer,
        zoom = zoom,
        target = target,
        onClickBack = { model.onClickBack() },
        onClickSearch = { model.onClickSearch() },
        onClickZoomIn = { model.onClickZoomIn() },
        onClickZoomOut = { model.onClickZoomOut() },
        onClickLayer = { model.onClickLayer() },
        onClickLocation = { model.onClickLocation() },
        onUpdateZoom = { model.updateZoom(it) },
        onUpdateTarget = { model.updateTarget(it) },
        onClickDownload = { model.onClickDownload() }
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
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = "地图", onClickBack = onClickBack) {
            Image(
                painter = painterResource(id = R.drawable.ic_search_24),
                contentDescription = "搜索",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .size(46.dp)
                    .clip(AppShape.RC.Cycle)
                    .clickable { onClickSearch() },
                contentScale = ContentScale.Inside
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
                    .height(150.dp)
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(30.dp)
                    .padding(top = 30.dp, bottom = 150.dp)
                    .align(Alignment.CenterStart)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(30.dp)
                    .padding(top = 30.dp, bottom = 150.dp)
                    .align(Alignment.CenterEnd)
                    .background(Color.Black.copy(alpha = 0.5f))
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp, bottom = 150.dp, start = 30.dp, end = 30.dp)
                    .align(Alignment.Center)
                    .border(1.dp, AppColor.System.secondary)
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

            DownloadButton(onClick = onClickDownload)
        }
    }
}

@Composable
private fun BoxScope.DownloadButton(
    onClick: () -> Unit
) {
    SureButton(
        modifier = Modifier
            .width(160.dp)
            .align(Alignment.BottomCenter)
            .padding(bottom = 50.dp),
        text = "立即下载",
        onClick = onClick
    )
}