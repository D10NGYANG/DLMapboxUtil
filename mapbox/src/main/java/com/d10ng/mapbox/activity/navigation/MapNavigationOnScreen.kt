package com.d10ng.mapbox.activity.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.view.Compass
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.d10ng.mapbox.view.PageTransitions
import com.d10ng.mapbox.view.UserLocationTextBar
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:44
 */
@Destination<NavigationNavGraph>(style = PageTransitions::class)
@Composable
fun MapNavigationOnScreen(
    nav: DestinationsNavigator,
    model: MapNavigationOnScreenViewModel = viewModel()
) {
    val layer by model.layerFlow.collectAsState()
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()
    val pointOptions by model.pointOptionsFlow.collectAsState()
    val lineOptions by model.lineOptionsFlow.collectAsState()
    val distanceText by model.distanceTextFlow.collectAsState()
    val isTouchMap by model.isTouchMapFlow.collectAsState()

    MapNavigationOnScreenView(
        layer = layer,
        zoom = zoom,
        target = target,
        pointOptions = pointOptions,
        lineOptions = lineOptions,
        distanceText = distanceText,
        isTouchMap = isTouchMap,
        onClickBack = { model.onClickBack(nav) },
        onMapStyleLoad = { model.onMapStyleLoad(it) },
        onUpdateUserTouchMap = { model.updateUserTouchMap(it) },
        onClickZoomIn = { model.onClickZoomIn() },
        onClickZoomOut = { model.onClickZoomOut() },
        onClickLayer = { model.onClickLayer() },
        onClickLocation = { model.onClickLocation() },
        onUpdateZoom = { model.updateZoom(it) },
        onUpdateTarget = { model.updateTarget(it) },
        onClickCancel = { model.onClickCancel() },
        onClickResume = { model.onClickResume() }
    )

    BackHandler(true) {
        model.onClickBack(nav)
    }
}

@Composable
private fun MapNavigationOnScreenView(
    layer: MapLayerType,
    zoom: Double,
    target: Point,
    pointOptions: Map<Int, PointAnnotationOptions>,
    lineOptions: Map<Int, PolylineAnnotationOptions>,
    distanceText: String,
    isTouchMap: Boolean,
    onClickBack: () -> Unit = {},
    onMapStyleLoad: (Style) -> Unit = {},
    onUpdateUserTouchMap: (Boolean) -> Unit = {},
    onClickZoomIn: () -> Unit = {},
    onClickZoomOut: () -> Unit = {},
    onClickLayer: () -> Unit = {},
    onClickLocation: () -> Unit = {},
    onUpdateZoom: (Double) -> Unit = {},
    onUpdateTarget: (Point) -> Unit = {},
    onClickCancel: () -> Unit = {},
    onClickResume: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(
            title = "地图导航",
            onClickBack = onClickBack,
            titleAlignment = Alignment.CenterStart
        )
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
                isShowUserLocation = false,
                pointOptions = pointOptions,
                lineOptions = lineOptions,
                onCameraZoomChange = onUpdateZoom,
                onCameraCenterChange = onUpdateTarget,
                onGesturesMoveListener = { _, _, isTouch ->
                    onUpdateUserTouchMap(isTouch)
                },
                onStyleLoad = onMapStyleLoad
            )

            UserLocationTextBar()
            Compass()

            MapZoomControllerBar(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp),
                onClickZoomIn = onClickZoomIn,
                onClickZoomOut = onClickZoomOut
            )

            MapLayerLocationControllerBar(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp),
                onClickLayer = onClickLayer,
                onClickLocation = onClickLocation
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isTouchMap) {
                TextButton(
                    onClick = onClickResume,
                ) {
                    Text(
                        text = "继续导航",
                        style = AppText.Normal.Title.default
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "直线距离：",
                        style = AppText.Normal.Title.default
                    )
                    Text(
                        text = distanceText,
                        style = AppText.Normal.Primary.default
                    )
                }
                Button(
                    text = "结束",
                    onClick = onClickCancel,
                    type = ButtonType.PRIMARY,
                    size = ButtonSize.SMALL,
                    shape = AppShape.RC.v6
                )
            }
        }
    }
}