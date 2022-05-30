package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.Input
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.view.SureButton
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun MapAreaSearchScreen(
    controller: NavHostController,
    act: MapActivity
) {
    val model: MapAreaSearchScreenViewModel = viewModel(factory = MapAreaSearchScreenViewModel.Factory(controller, act))
    val inputLat by model.inputLatFlow.collectAsState()
    val inputLng by model.inputLngFlow.collectAsState()

    MapAreaSearchScreenView(
        inputLat = inputLat,
        inputLng = inputLng,
        onClickBack = { model.onClickBack() },
        onUpdateInputLat = { model.updateInputLat(it) },
        onUpdateInputLng = { model.updateInputLng(it) },
        onClickSure = { model.onClickSure() }
    )
}

@Composable
private fun MapAreaSearchScreenView(
    inputLat: String,
    inputLng: String,
    onClickBack: () -> Unit = {},
    onUpdateInputLat: (String) -> Unit = {},
    onUpdateInputLng: (String) -> Unit = {},
    onClickSure: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = "搜索", onClickBack = onClickBack)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { InputItem(
                value = inputLat,
                onValueChange = onUpdateInputLat,
                title = "纬度：",
                placeholder = "请输入目标纬度，-90至90，eg:22.3"
            ) }
            item { InputItem(
                value = inputLng,
                onValueChange = onUpdateInputLng,
                title = "经度：",
                placeholder = "请输入目标经度，-180～180，eg:113.2"
            ) }
            item {
                SureButton(
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .width(150.dp),
                    onClick = onClickSure
                )
            }
        }
    }
}

@Composable
fun InputItem(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    placeholder: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(AppColor.System.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = AppText.Normal.Title.v14)

        Input(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            textStyle = AppText.Normal.Body.v14,
            placeholderStyle = AppText.Normal.Hint.v14,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp),
            singleLine = true
        )
    }
}