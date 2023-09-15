package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.view.InputItem
import com.d10ng.mapbox.view.SureButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.roundToInt

@MapNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun MapOfflineAddScreen(
    nav: DestinationsNavigator,
    model: MapOfflineAddScreenViewModel = viewModel()
) {
    val inputName by model.inputNameFlow.collectAsState()
    val zoomRange by model.zoomRangeFlow.collectAsState()

    MapOfflineAddScreenView(
        inputName = inputName,
        zoomRange = zoomRange,
        onClickBack = nav::navigateUp,
        onUpdateInputName = { model.updateInputName(it) },
        onUpdateZoomRange = { model.updateZoomRange(it) },
        onClickSure = { model.onClickSure(nav) }
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
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = "配置离线地图", onClickBack = onClickBack)

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
            .background(AppColor.Neutral.bg)
            .padding(16.dp)
    ) {
        Text(
            text = "地图层级：${zoomRange.start.roundToInt()} ~ ${zoomRange.endInclusive.roundToInt()} 级",
            style = AppText.Normal.Title.small
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
                thumbColor = AppColor.Main.primary,
                activeTrackColor = AppColor.Neutral.bg,
                inactiveTrackColor = AppColor.Neutral.bg,
                activeTickColor = AppColor.Main.primary,
                inactiveTickColor = AppColor.Main.primary
            )
        )

        Text(text = "层级说明：", style = AppText.Normal.Body.v12)
        Text(text = "* 全球覆盖：0 - 5", style = AppText.Normal.Hint.v12)
        Text(text = "* 区域信息：6 - 10", style = AppText.Normal.Hint.v12)
        Text(text = "* 本地信息：11 - 14", style = AppText.Normal.Hint.v12)
        Text(text = "* 街道细节：15 - 16", style = AppText.Normal.Hint.v12)
    }
}