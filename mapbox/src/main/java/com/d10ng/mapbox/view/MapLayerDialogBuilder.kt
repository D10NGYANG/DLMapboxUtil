package com.d10ng.mapbox.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.compose.ui.dialog.DialogColumn
import com.d10ng.compose.ui.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.dialog.builder.TipsDialogBuilder
import com.d10ng.mapbox.constant.MapLayerType

/**
 * 地图图层选择弹窗构建器
 * @property value MapLayerType
 * @property isOnlyShowCanDown Boolean
 * @property onChange Function1<MapLayerType, Unit>
 * @constructor
 */
data class MapLayerDialogBuilder(
    // 当前选中的图层
    private val value: MapLayerType,
    // 是否只显示可下载的图层
    private val isOnlyShowCanDown: Boolean = false,
    // 选中图层回调
    private val onChange: (MapLayerType) -> Unit,
) : DialogBuilder(clickOutsideDismiss = true) {

    companion object {
        @Composable
        fun ItemView(
            type: MapLayerType,
            isSelect: Boolean,
            onClick: () -> Unit
        ) {
            Box(
                modifier = Modifier
                    .size(130.dp, 105.dp)
                    .clip(AppShape.RC.v8)
                    .clickable { onClick.invoke() }
            ) {
                Image(
                    painter = painterResource(id = type.imgSrcId),
                    contentDescription = type.label,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(AppShape.RC.v8)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelect) AppColor.Main.primary else AppColor.Neutral.hint)
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = type.label,
                        style = AppText.Normal.Surface.small,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Build(id: String) {
        DialogColumn {
            // 标题
            TipsDialogBuilder.TitleText(text = "请选择地图")
            // 列表
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MapLayerType.entries.filter {
                    if (isOnlyShowCanDown) {
                        it.isCanDown
                    } else true
                }.forEach { type ->
                    ItemView(type = type, isSelect = value == type) {
                        onChange.invoke(type)
                        dismiss(id)
                    }
                }
            }
        }
    }
}
