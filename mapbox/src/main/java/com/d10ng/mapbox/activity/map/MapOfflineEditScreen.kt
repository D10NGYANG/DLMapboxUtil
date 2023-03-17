package com.d10ng.mapbox.activity.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.view.TitleBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.view.InputItem
import com.d10ng.mapbox.view.PageTransitions
import com.d10ng.mapbox.view.SureButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@MapNavGraph
@Destination(
    style = PageTransitions::class,
    navArgsDelegate = MapOfflineEditScreenNavArg::class
)
@Composable
fun MapOfflineEditScreen(
    nav: DestinationsNavigator,
    model: MapOfflineEditScreenViewModel = viewModel()
) {
    val inputName by model.inputNameFlow.collectAsState()

    MapOfflineEditScreenView(
        inputName = inputName,
        onClickBack = nav::navigateUp,
        onClickDelete = { model.onClickDelete(nav) },
        onUpdateInputName = { model.updateInputName(it) },
        onClickSure = { model.onClickSure(nav) }
    )
}

@Composable
fun MapOfflineEditScreenView(
    inputName: String,
    onClickBack: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onUpdateInputName: (String) -> Unit = {},
    onClickSure: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = "编辑离线地图", onClickBack = onClickBack) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = "删除",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .size(46.dp)
                    .clip(AppShape.RC.Cycle)
                    .clickable { onClickDelete() },
                contentScale = ContentScale.Inside
            )
        }
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
                SureButton(
                    modifier = Modifier
                        .padding(vertical = 30.dp)
                        .width(150.dp),
                    text = "更新",
                    onClick = onClickSure
                )
            }
        }
    }
}