package com.d10ng.mapbox.activity.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonSize
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Search
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.constant.MapLayerType
import com.d10ng.mapbox.stores.NavigationStore
import com.d10ng.mapbox.view.Compass
import com.d10ng.mapbox.view.MapLayerLocationControllerBar
import com.d10ng.mapbox.view.MapZoomControllerBar
import com.d10ng.mapbox.view.MapboxView
import com.d10ng.mapbox.view.UserLocationTextBar
import com.d10ng.mapbox.view.navigationBarCameraPadding
import com.mapbox.geojson.Point

/**
 * 地图导航
 * @Author d10ng
 * @Date 2023/9/16 17:08
 */
@Composable
fun MapNavigationHomeScreen(
    onNavigateOn: () -> Unit,
    model: MapNavigationHomeScreenViewModel = viewModel()
) {
    val navigationTarget by NavigationStore.targetFlow.collectAsState()
    LaunchedEffect(navigationTarget) {
        if (navigationTarget != null) onNavigateOn()
    }

    val layer by model.layerFlow.collectAsState()
    val zoom by model.zoomFlow.collectAsState()
    val target by model.targetFlow.collectAsState()
    val cameraPadding = navigationBarCameraPadding()

    MapNavigationHomeScreenView(
        layer = layer,
        zoom = zoom,
        target = target,
        cameraPadding = cameraPadding,
        onClickBack = model::onClickBack,
        onClickOffline = model::onClickOffline,
        onClickSearch = { model.onClickSearch(onNavigateOn) },
        onClickZoomIn = { model.onClickZoomIn() },
        onClickZoomOut = { model.onClickZoomOut() },
        onClickLayer = { model.onClickLayer() },
        onClickLocation = { model.onClickLocation() },
        onUpdateZoom = { model.updateZoom(it) },
        onUpdateTarget = { model.updateTarget(it) },
        onClickSet = { model.onClickSet(onNavigateOn) }
    )
}

@Composable
private fun MapNavigationHomeScreenView(
    layer: MapLayerType,
    zoom: Double,
    target: Point,
    cameraPadding: com.mapbox.maps.EdgeInsets,
    onClickBack: () -> Unit = {},
    onClickOffline: () -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickZoomIn: () -> Unit = {},
    onClickZoomOut: () -> Unit = {},
    onClickLayer: () -> Unit = {},
    onClickLocation: () -> Unit = {},
    onUpdateZoom: (Double) -> Unit = {},
    onUpdateTarget: (Point) -> Unit = {},
    onClickSet: () -> Unit = {}
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
        ) {
            Button(
                text = "离线地图",
                onClick = onClickOffline,
                modifier = Modifier
                    .padding(end = 16.dp),
                type = ButtonType.PRIMARY,
                size = ButtonSize.MINI,
                shape = AppShape.RC.v6
            )
        }
        CellGroup(modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickSearch() }) {
            Search(value = "", disabled = true, placeholder = "搜索位置")
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
                cameraPadding = cameraPadding,
                onCameraZoomChange = onUpdateZoom,
                onCameraCenterChange = onUpdateTarget
            )

            Box(Modifier.fillMaxSize().navigationBarsPadding()) {
                UserLocationTextBar()
                Compass()

                Image(
                    painter = painterResource(id = R.drawable.ic_map_location_target_25),
                    contentDescription = "选择位置",
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    text = "拖动地图选择目的地",
                    style = AppText.Normal.Surface.default,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = 45.dp)
                        .background(Color.Black.copy(alpha = 0.4f), AppShape.RC.Cycle)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    text = "设置为目的地",
                    onClick = onClickSet,
                    type = ButtonType.PRIMARY,
                    shape = AppShape.RC.Cycle
                )
            }
        }
    }
}
