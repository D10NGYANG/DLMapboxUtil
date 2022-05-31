package com.d10ng.mapbox.activity.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.Input
import com.d10ng.basicjetpackcomposeapp.view.MiniButton
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.R
import com.d10ng.tianditu.bean.LocationSearch
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun LocationSearchMainScreen(
    controller: NavHostController,
    act: BaseActivity
) {
    val model: LocationSearchMainScreenViewModel = viewModel(factory = LocationSearchMainScreenViewModel.Factory(controller, act))
    val input by model.inputFlow.collectAsState()
    val result by model.resultFlow.collectAsState()

    LocationSearchMainScreenView(
        input = input,
        result = result,
        onClickBack = { model.onClickBack() },
        onUpdateInput = { model.updateInput(it) },
        onClickSearch = { model.onClickSearch() },
        onClickAreaItem = { model.onClickItem(it) },
        onClickAdminItem = { model.onClickItem(it) },
        onClickPoiItem = { model.onClickItem(it) }
    )
}

@Composable
private fun LocationSearchMainScreenView(
    input: String,
    result: LocationSearch?,
    onClickBack: () -> Unit = {},
    onUpdateInput: (String) -> Unit = {},
    onClickSearch: () -> Unit = {},
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
        TitleBar(value = "位置搜索", onClickBack = onClickBack)
        SearchBar(input = input, onUpdateInput = onUpdateInput, onClickSearch = onClickSearch)
    }
}

@Composable
private fun SearchBar(
    input: String,
    onUpdateInput: (String) -> Unit = {},
    onClickSearch: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(top = 1.dp)
            .background(AppColor.System.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(end = 16.dp)
                .background(AppColor.System.background, AppShape.RC.v6)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_24),
                contentDescription = "搜索",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp),
                tint = AppColor.Text.hint
            )
            Input(
                value = input,
                onValueChange = onUpdateInput,
                textStyle = AppText.Normal.Title.v16,
                placeholder = "请输入搜索内容",
                placeholderStyle = AppText.Normal.Hint.v16
            )
        }
        MiniButton(text = "搜索", onClick = onClickSearch)
    }
}