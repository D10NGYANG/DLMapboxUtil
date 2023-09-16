package com.d10ng.mapbox.activity.offline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Field
import com.d10ng.compose.ui.navigation.NavBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.math.roundToInt

@OfflineNavGraph
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
    ) {
        NavBar(
            title = "配置离线地图",
            onClickBack = onClickBack,
            titleAlignment = Alignment.CenterStart
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CellGroup(
                modifier = Modifier.padding(top = 12.dp),
                border = false
            ) {
                Field(
                    value = inputName,
                    onValueChange = onUpdateInputName,
                    label = "地图名称",
                    placeholder = "请输入备注地图名称",
                    border = false
                )
            }
            CellGroup(
                modifier = Modifier.padding(top = 12.dp),
                border = false
            ) {
                ZoomRangePicker(
                    zoomRange = zoomRange,
                    onUpdateZoomRange = onUpdateZoomRange
                )
            }
            Button(
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .width(150.dp),
                text = "下载",
                onClick = onClickSure,
                type = ButtonType.PRIMARY,
                shape = AppShape.RC.Cycle
            )
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
            .padding(top = 4.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "地图层级",
                style = AppText.Normal.Title.default
            )
            Box(modifier = Modifier.width(55.dp))
            Text(
                text = "${zoomRange.start.roundToInt()} ~ ${zoomRange.endInclusive.roundToInt()} 级",
                style = AppText.Normal.Body.default
            )
        }


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

        Text(text = "层级说明", style = AppText.Normal.Body.default)
        Text(text = "* 全球覆盖：0 - 5", style = AppText.Normal.Tips.small)
        Text(text = "* 区域信息：6 - 10", style = AppText.Normal.Tips.small)
        Text(text = "* 本地信息：11 - 14", style = AppText.Normal.Tips.small)
        Text(text = "* 街道细节：15 - 16", style = AppText.Normal.Tips.small)
    }
}