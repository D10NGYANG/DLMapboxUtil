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
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.view.InputItem
import com.d10ng.mapbox.view.SureButton
import com.google.accompanist.insets.navigationBarsPadding
import kotlin.math.roundToInt

@Composable
fun MapOfflineAddScreen(
    controller: NavHostController,
    act: MapActivity
) {
    val model: MapOfflineAddScreenViewModel = viewModel(factory = MapOfflineAddScreenViewModel.Factory(controller, act))
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
        TitleBar(value = "??????????????????", onClickBack = onClickBack)

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { InputItem(
                value = inputName,
                onValueChange = onUpdateInputName,
                title = "???????????????",
                placeholder = "???????????????????????????"
            ) }
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
                    text = "??????",
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
        Text(text = "???????????????${zoomRange.start.roundToInt()} ~ ${zoomRange.endInclusive.roundToInt()} ???", style = AppText.Normal.Title.v14)

        RangeSlider(
            values = zoomRange,
            onValueChange = {
                tempRange = it
                onUpdateZoomRange(it)
            },
            valueRange = 1f .. 25f,
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

        Text(text = "???????????????", style = AppText.Normal.Body.v12)
        Text(text = "* ???????????????0 - 5", style = AppText.Normal.Hint.v12)
        Text(text = "* ???????????????6 - 10", style = AppText.Normal.Hint.v12)
        Text(text = "* ???????????????11 - 14", style = AppText.Normal.Hint.v12)
        Text(text = "* ???????????????15 - 16", style = AppText.Normal.Hint.v12)
    }
}