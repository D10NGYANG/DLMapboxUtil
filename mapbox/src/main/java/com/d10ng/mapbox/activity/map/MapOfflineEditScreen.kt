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
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.view.InputItem
import com.d10ng.mapbox.view.SureButton
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun MapOfflineEditScreen(
    controller: NavHostController,
    act: MapActivity,
    id: String
) {
    val model: MapOfflineEditScreenViewModel = viewModel(factory = MapOfflineEditScreenViewModel.Factory(controller, act, id))
    val inputName by model.inputNameFlow.collectAsState()

    MapOfflineEditScreenView(
        inputName = inputName,
        onClickBack = { model.onClickBack() },
        onClickDelete = { model.onClickDelete() },
        onUpdateInputName = { model.updateInputName(it) },
        onClickSure = { model.onClickSure() }
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
        TitleBar(value = "??????????????????", onClickBack = onClickBack) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                contentDescription = "??????",
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
            item { InputItem(
                value = inputName,
                onValueChange = onUpdateInputName,
                title = "???????????????",
                placeholder = "???????????????????????????"
            ) }
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