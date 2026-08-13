package com.d10ng.mapbox.activity.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.tianditu.bean.LocationSearch

@Composable
fun LocationSearchInfoScreen(
    onBack: () -> Unit,
    onNavigateInfo: (String, String, Int) -> Unit,
    model: LocationSearchInfoScreenViewModel
) {
    val result by model.resultFlow.collectAsState()

    LocationSearchInfoScreenView(
        area = model.getArea(),
        result = result,
        onClickBack = onBack,
        onClickAreaItem = { model.onClickItem(it, onNavigateInfo) },
        onClickAdminItem = { model.onClickItem(it, onNavigateInfo) },
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
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = area, onClickBack = onClickBack, titleAlignment = Alignment.CenterStart)
        LocationSearchView(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            result = null to result,
            onClickAreaItem = onClickAreaItem,
            onClickAdminItem = onClickAdminItem,
            onClickPoiItem = onClickPoiItem
        )
    }
}
