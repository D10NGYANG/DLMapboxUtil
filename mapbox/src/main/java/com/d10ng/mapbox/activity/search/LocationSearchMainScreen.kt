package com.d10ng.mapbox.activity.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.base.CellGroup
import com.d10ng.compose.ui.form.Search
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.bean.HistoryInfo
import com.d10ng.mapbox.view.NavBarIconButton
import com.d10ng.tianditu.bean.LocationSearch
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@LocationSearchNavGraph(start = true)
@Destination(style = PageTransitions::class)
@Composable
fun LocationSearchMainScreen(
    nav: DestinationsNavigator,
    model: LocationSearchMainScreenViewModel = viewModel()
) {
    val input by model.inputFlow.collectAsState()
    val isSearching by model.isSearchingFlow.collectAsState()
    val history by model.historyFlow.collectAsState(initial = listOf())
    val result by model.resultFlow.collectAsState()

    LocationSearchMainScreenView(
        input = input,
        isSearching = isSearching,
        history = history,
        result = result,
        onClickBack = { model.onClickBack() },
        onUpdateInput = { model.updateInput(it) },
        onClickSearch = { model.onClickSearch() },
        onClickByLatLng = { model.onClickByLatLng(nav) },
        onClickClearHistory = { model.onClickClearHistory() },
        onClickRemoveHistory = { model.onClickRemoveHistory(it) },
        onClickHistory = { model.onClickHistory(it) },
        onClickAreaItem = { model.onClickItem(nav, it) },
        onClickAdminItem = { model.onClickItem(nav, it) },
        onClickPoiItem = { model.onClickItem(it) }
    )
}

@Composable
private fun LocationSearchMainScreenView(
    input: String,
    isSearching: Boolean,
    history: List<HistoryInfo>,
    result: LocationSearch?,
    onClickBack: () -> Unit = {},
    onUpdateInput: (String) -> Unit = {},
    onClickSearch: () -> Unit = {},
    onClickByLatLng: () -> Unit = {},
    onClickClearHistory: () -> Unit = {},
    onClickRemoveHistory: (HistoryInfo) -> Unit = {},
    onClickHistory: (HistoryInfo) -> Unit = {},
    onClickAreaItem: (LocationSearch.Area) -> Unit = {},
    onClickAdminItem: (LocationSearch.Statistics.AllAdmin) -> Unit = {},
    onClickPoiItem: (LocationSearch.Poi) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
    ) {
        NavBar(
            title = "地图搜索",
            onClickBack = onClickBack,
            titleAlignment = Alignment.CenterStart
        ) {
            NavBarIconButton(
                icon = R.drawable.ic_toolbar_location_by_point_24,
                onClick = onClickByLatLng
            )
        }
        CellGroup {
            Search(
                value = input,
                onValueChange = onUpdateInput,
                placeholder = "请输入搜索内容",
                actionText = "搜索",
                onClickAction = onClickSearch,
                loading = isSearching
            )
        }

        if (input.isEmpty() && history.isNotEmpty()) {
            HistoryGroupView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = history,
                onClickClear = onClickClearHistory,
                onClickRemove = onClickRemoveHistory,
                onClickItem = onClickHistory
            )
        } else {
            LocationSearchView(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                result = result,
                onClickAreaItem = onClickAreaItem,
                onClickAdminItem = onClickAdminItem,
                onClickPoiItem = onClickPoiItem
            )
        }
    }
}

@Composable
private fun HistoryGroupView(
    modifier: Modifier,
    value: List<HistoryInfo>,
    onClickClear: () -> Unit = {},
    onClickRemove: (HistoryInfo) -> Unit = {},
    onClickItem: (HistoryInfo) -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(47.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "历史记录",
                style = AppText.Normal.Tips.default
            )
            Box(modifier = Modifier.weight(1f))
            Text(
                text = "清除全部",
                style = AppText.Normal.Error.default,
                modifier = Modifier
                    .clickable { onClickClear() }
            )
        }
        value.forEachIndexed { index, item ->
            ItemView(
                iconId = R.drawable.ic_search_point_25,
                label = item.name,
                description = item.address,
                border = index < value.size - 1,
                onClickRemove = { onClickRemove(item) },
                onClick = { onClickItem(item) }
            )
        }
    }
}

@Composable
internal fun LocationSearchView(
    modifier: Modifier = Modifier,
    result: LocationSearch?,
    onClickAreaItem: (LocationSearch.Area) -> Unit = {},
    onClickAdminItem: (LocationSearch.Statistics.AllAdmin) -> Unit = {},
    onClickPoiItem: (LocationSearch.Poi) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    if (result == null || (result.count.toIntOrNull() ?: 0) == 0) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_empty_124),
                contentDescription = null,
                tint = AppColor.Neutral.hint
            )
            Text(
                text = "暂无搜索结果",
                style = AppText.Normal.Hint.default,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            when (result.resultType) {
                3 -> {
                    ItemView(
                        iconId = R.drawable.ic_baseline_public_24,
                        label = result.area.name,
                        border = false,
                        onClick = {
                            focusManager.clearFocus()
                            onClickAreaItem(result.area)
                        }
                    )
                }

                2 -> {
                    result.statistics.allAdmins.forEachIndexed { index, item ->
                        ItemView(
                            iconId = R.drawable.ic_baseline_map_24,
                            label = item.adminName,
                            content = item.count.toString(),
                            border = index < result.statistics.allAdmins.size - 1,
                            onClick = {
                                focusManager.clearFocus()
                                onClickAdminItem(item)
                            }
                        )
                    }
                }

                1 -> {
                    result.pois.forEachIndexed { index, item ->
                        ItemView(
                            iconId = R.drawable.ic_search_point_25,
                            label = item.name,
                            description = item.address,
                            border = index < result.pois.size - 1,
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

@Composable
private fun ItemView(
    iconId: Int,
    label: String,
    description: String = "",
    content: String = "",
    border: Boolean,
    onClickRemove: (() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = label,
                style = AppText.Normal.Title.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = AppText.Normal.Tips.mini,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
        if (content.isNotEmpty()) {
            Text(
                text = content,
                style = AppText.Normal.Tips.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        if (onClickRemove != null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cell_remove_22),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(AppShape.RC.Cycle)
                    .clickable { onClickRemove() },
                tint = AppColor.Neutral.hint
            )
        }
    }
    if (border) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(0.5.dp)
                .background(AppColor.Neutral.line)
        )
    }
}