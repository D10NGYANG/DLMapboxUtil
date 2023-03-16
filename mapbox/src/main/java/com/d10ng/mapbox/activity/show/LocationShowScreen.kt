package com.d10ng.mapbox.activity.show

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.view.TitleBar
import com.d10ng.mapbox.activity.map.Compass
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.d10ng.mapbox.view.SureButton
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions

@Composable
fun LocationShowScreen(
    act: BaseActivity,
    point: Point,
    model: LocationShowScreenViewModel = viewModel(
        key = "LocationShowScreen",
        factory = LocationShowScreenViewModel.Factory(act, point)
    )
) {
    LaunchedEffect(act) {
        model.init(act)
    }

    val layer by model.layerFlow.collectAsState()
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()
    val pointOption by model.pointOptionFlow.collectAsState()

    LocationShowScreenView(
        layer = layer,
        zoom = zoom,
        target = target,
        pointOption = pointOption,
        onClickBack = { model.onClickBack() },
        onMapStyleLoad = { model.onMapStyleLoad(it) },
        onClickZoomIn = { model.onClickZoomIn() },
        onClickZoomOut = { model.onClickZoomOut() },
        onClickLayer = { model.onClickLayer() },
        onClickLocation = { model.onClickLocation() },
        onUpdateZoom = { model.updateZoom(it) },
        onUpdateTarget = { model.updateTarget(it) },
        onClickGo = { model.onClickGo() }
    )
}

@Composable
fun LocationShowScreenView(
    layer: MapLayerType,
    zoom: Double,
    target: Point,
    pointOption: PointAnnotationOptions?,
    onClickBack: () -> Unit = {},
    onMapStyleLoad: (Style) -> Unit = {},
    onClickZoomIn: () -> Unit = {},
    onClickZoomOut: () -> Unit = {},
    onClickLayer: () -> Unit = {},
    onClickLocation: () -> Unit = {},
    onUpdateZoom: (Double) -> Unit = {},
    onUpdateTarget: (Point) -> Unit = {},
    onClickGo: () -> Unit = {},
) {
    val points = remember(pointOption) {
        if (pointOption != null) mapOf(1 to pointOption) else mapOf()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = "位置", onClickBack = onClickBack)
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
                onCameraCenterChange = onUpdateTarget,
                onStyleLoad = onMapStyleLoad,
                pointOptions = points
            )

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

            SureButton(
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp),
                text = "到这去",
                onClick = onClickGo
            )
        }
    }
}