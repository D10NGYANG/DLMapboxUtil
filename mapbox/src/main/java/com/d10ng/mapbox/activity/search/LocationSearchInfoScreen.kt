package com.d10ng.mapbox.activity.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.tianditu.bean.LocationSearch

@Composable
fun LocationSearchInfoScreen(
    controller: NavHostController,
    act: BaseActivity,
    search: String,
    area: String,
    areaCode: Int
) {
    val model: LocationSearchInfoScreenViewModel = viewModel(factory = LocationSearchInfoScreenViewModel.Factory(controller, act, search, area, areaCode))
    val result by model.resultFlow.collectAsState()

    LocationSearchInfoScreenView(
        area = area,
        result = result,
        onClickBack = { model.onClickBack() },
        onClickAreaItem = { model.onClickItem(it) },
        onClickAdminItem = { model.onClickItem(it) },
        onClickPoiItem = { model.onClickItem(it) }
    )
}

@Composable
private fun LocationSearchInfoScreenView(
    area: String,
    result: LocationSearch?,
    onClickBack: () -> Unit = {},
    onClickAreaItem: (LocationSearch.Area) -> Unit = {},
    onClickAdminItem: (LocationSearch.Statistics.AllAdmin) -> Unit = {},
    onClickPoiItem: (LocationSearch.Poi) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.System.background)
            .navigationBarsPadding()
    ) {
        TitleBar(value = area, onClickBack = onClickBack)
        LocationSearchView(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            result = result,
            onClickAreaItem = onClickAreaItem,
            onClickAdminItem = onClickAdminItem,
            onClickPoiItem = onClickPoiItem
        )
    }
}