package com.d10ng.mapbox.activity.offline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Field
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.view.NavBarIconButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OfflineNavGraph
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
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(
            title = "编辑离线地图",
            onClickBack = onClickBack,
            titleAlignment = Alignment.CenterStart
        ) {
            NavBarIconButton(
                icon = R.drawable.ic_baseline_delete_24,
                onClick = onClickDelete
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CellGroup(
                title = "基本信息",
                border = false
            ) {
                Field(
                    value = inputName,
                    onValueChange = onUpdateInputName,
                    label = "地图名称",
                    placeholder = "请输入备注地图名称"
                )
            }
            Button(
                modifier = Modifier
                    .padding(vertical = 30.dp)
                    .width(150.dp),
                text = "更新",
                onClick = onClickSure,
                type = ButtonType.PRIMARY,
                shape = AppShape.RC.Cycle
            )
        }
    }
}