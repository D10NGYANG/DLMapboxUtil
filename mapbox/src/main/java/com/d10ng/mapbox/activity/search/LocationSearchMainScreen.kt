package com.d10ng.mapbox.activity.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppShape
import com.d10ng.basicjetpackcomposeapp.compose.AppText
import com.d10ng.basicjetpackcomposeapp.view.Input
import com.d10ng.basicjetpackcomposeapp.view.ListItem
import com.d10ng.basicjetpackcomposeapp.view.MiniButton
import com.d10ng.basicjetpackcomposeapp.view.TitleBar
import com.d10ng.mapbox.R
import com.d10ng.tianditu.bean.LocationSearch

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
        onClickByLatLng = { model.onClickByLatLng() },
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
    onClickByLatLng: () -> Unit = {},
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
        LocationByLatLngBar(onClick = onClickByLatLng)
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
                .padding(horizontal = 12.dp, vertical = 8.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
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

@Composable
private fun LocationByLatLngBar(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(top = 1.dp)
            .fillMaxWidth()
            .background(AppColor.System.primary)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = "通过经纬度获取位置", style = AppText.Normal.Title.v14)
    }
}

@Composable
fun LocationSearchView(
    modifier: Modifier = Modifier,
    result: LocationSearch?,
    onClickAreaItem: (LocationSearch.Area) -> Unit = {},
    onClickAdminItem: (LocationSearch.Statistics.AllAdmin) -> Unit = {},
    onClickPoiItem: (LocationSearch.Poi) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    if (result == null || (result.count.toIntOrNull() ?: 0) == 0) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "未找到相关信息",
                style = AppText.Normal.Body.v14
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
        ) {
            when(result.resultType) {
                3 -> {
                    item {
                        ListItem(
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(AppColor.System.primary),
                            iconId = R.drawable.ic_baseline_public_24,
                            iconSizeDp = 24.dp,
                            title = result.area.name,
                            isShowArrow = true,
                            onClick = {
                                focusManager.clearFocus()
                                onClickAreaItem(result.area)
                            }
                        )
                    }
                }
                2 -> {
                    items(result.statistics.allAdmins) { item ->
                        ListItem(
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(AppColor.System.primary),
                            iconId = R.drawable.ic_baseline_map_24,
                            title = item.adminName,
                            right = item.count.toString(),
                            isShowArrow = true,
                            onClick = {
                                focusManager.clearFocus()
                                onClickAdminItem(item)
                            }
                        )
                    }
                }
                1 -> {
                    items(result.pois) { item ->
                        ListItem(
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(AppColor.System.primary),
                            title = item.name,
                            iconId = R.drawable.ic_baseline_room_24,
                            note = item.address,
                            onClick = {
                                focusManager.clearFocus()
                                onClickPoiItem(item)
                            }
                        )
                    }
                }
            }
        }
    }
}