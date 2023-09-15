package com.d10ng.mapbox.activity.map

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.PageTransitions
import com.d10ng.compose.ui.navigation.NavBar
import com.d10ng.mapbox.R
import com.d10ng.mapbox.bean.OfflineMapInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@MapNavGraph
@Destination(style = PageTransitions::class)
@Composable
fun MapOfflineListScreen(
    nav: DestinationsNavigator,
    model: MapOfflineListScreenViewModel = viewModel()
) {
    val infoList by model.offlineMapInfoListFlow.collectAsState()
    val snapshotMap by model.offlineMapSnapshotFlow.collectAsState()

    MapOfflineListScreenView(
        infoList = infoList,
        snapshotMap = snapshotMap,
        onClickBack = nav::navigateUp,
        onClickAdd = { model.onClickAdd(nav) },
        onClickItem = { model.onClickItem(nav, it) }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MapOfflineListScreenView(
    infoList: List<OfflineMapInfo>,
    snapshotMap: Map<String, Bitmap>,
    onClickBack: () -> Unit = {},
    onClickAdd: () -> Unit = {},
    onClickItem: (OfflineMapInfo) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.Neutral.bg)
            .navigationBarsPadding()
    ) {
        NavBar(title = "离线地图", onClickBack = onClickBack) {
            Image(
                painter = painterResource(id = R.drawable.ic_toolbar_add_24),
                contentDescription = "添加",
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(46.dp)
                    .clip(AppShape.RC.Cycle)
                    .clickable { onClickAdd() },
                contentScale = ContentScale.Inside
            )
        }
        if (infoList.isEmpty()) {
            BoxDataEmpty()
        } else {
            FlowRow(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                infoList.forEach { item ->
                    OfflineMapItem(value = item, bitmap = snapshotMap[item.region.id]) {
                        onClickItem(item)
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.BoxDataEmpty() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_offline_map_empty_100),
                contentDescription = "还未下载离线地图"
            )
            Text(
                text = "还未下载离线地图",
                style = AppText.Normal.Hint.v16
            )
        }
    }
}

@Composable
private fun OfflineMapItem(
    value: OfflineMapInfo,
    bitmap: Bitmap?,
    onClick: () -> Unit = {}
) {
    val progress = (value.region.completedResourceCount * 100) / value.region.requiredResourceCount
    Box(
        modifier = Modifier
            .size(160.dp)
            .background(AppColor.Neutral.line, AppShape.RC.v6)
            .clip(AppShape.RC.v6)
            .clickable { onClick() }
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(AppColor.Neutral.card)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value.title,
                style = AppText.Normal.Title.v14,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (progress < 100) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$progress%",
                    style = AppText.Bold.Title.v24,
                    color = Color.White
                )
            }
        }
    }
}