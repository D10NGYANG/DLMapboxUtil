package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.view.InputItem
import com.d10ng.mapbox.view.SureButton
import kotlin.math.roundToInt

@Composable
fun MapOfflineAddScreen(
    controller: NavHostController,
    act: BaseActivity,
    model: MapOfflineAddScreenViewModel = viewModel()
) {
    LaunchedEffect(controller, act) { model.init(act, controller) }

    val inputName by model.inputNameFlow.collectAsState()
    val zoomRange by model.zoomRangeFlow.collectAsState()

    MapOfflineAddScreenView(
        inputName = inputName,
        zoomRange = zoomRange,
        onClickBack = { model.onClickBack() },
        onUpdateInputName = { model.updateInputName(it) },
        onUpdateZoomRange = { model.updateZoomRange(it) },
        onClickSure = { model.onClickSure() }
    )
}

@Composable
private fun MapOfflineAddScreenView(
    inputName: String,
    zoomRange: ClosedFloatingPointRange<Float>,
    onClickBack: () -> Unit = {},
    onUpdateInputName: (String) -> Unit = {},
    onUpdateZoomRange: (ClosedFloatingPointRange<Float>) -> Unit = {},
    onClickSure: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = "配置离线地图", onClickBack = onClickBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                InputItem(
                    value = inputName,
                    onValueChange = onUpdateInputName,
                    title = "地图名称：",
                    placeholder = "请输入备注地图名称"
                )
            }
            item {
                ZoomRangePicker(
                    zoomRange = zoomRange,
                    onUpdateZoomRange = onUpdateZoomRange
                )
            }
            item {
                SureButton(
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .width(150.dp),
                    text = "下载",
                    onClick = onClickSure
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ZoomRangePicker(
    zoomRange: ClosedFloatingPointRange<Float>,
    onUpdateZoomRange: (ClosedFloatingPointRange<Float>) -> Unit = {},
) {
    var tempRange by remember {
        mutableStateOf(zoomRange)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(AppColor.System.background)
            .padding(16.dp)
    ) {
        Text(
            text = "地图层级：${zoomRange.start.roundToInt()} ~ ${zoomRange.endInclusive.roundToInt()} 级",
            style = AppText.Normal.Title.v14
        )

        RangeSlider(
            value = zoomRange,
            onValueChange = {
                tempRange = it
                onUpdateZoomRange(it)
            },
            valueRange = 1f..25f,
            steps = 25,
            onValueChangeFinished = {
                onUpdateZoomRange(tempRange)
            },
            colors = SliderDefaults.colors(
                thumbColor = AppColor.System.secondary,
                activeTrackColor = AppColor.System.background,
                inactiveTrackColor = AppColor.System.background,
                activeTickColor = AppColor.System.secondary,
                inactiveTickColor = AppColor.System.secondary
            )
        )

        Text(text = "层级说明：", style = AppText.Normal.Body.v12)
        Text(text = "* 全球覆盖：0 - 5", style = AppText.Normal.Hint.v12)
        Text(text = "* 区域信息：6 - 10", style = AppText.Normal.Hint.v12)
        Text(text = "* 本地信息：11 - 14", style = AppText.Normal.Hint.v12)
        Text(text = "* 街道细节：15 - 16", style = AppText.Normal.Hint.v12)
    }
}