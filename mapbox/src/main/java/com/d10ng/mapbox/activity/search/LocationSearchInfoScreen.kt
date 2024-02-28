package com.d10ng.mapbox.activity.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.tianditu.bean.LocationSearch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@LocationSearchNavGraph
@Destination(
    style = PageTransitions::class,
    navArgsDelegate = LocationSearchInfoScreenNavArgs::class
)
@Composable
fun LocationSearchInfoScreen(
    nav: DestinationsNavigator,
    model: LocationSearchInfoScreenViewModel = viewModel()
) {
    val result by model.resultFlow.collectAsState()

    LocationSearchInfoScreenView(
        area = model.getArea(),
        result = result,
        onClickBack = nav::navigateUp,
        onClickAreaItem = { model.onClickItem(nav, it) },
        onClickAdminItem = { model.onClickItem(nav, it) },
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