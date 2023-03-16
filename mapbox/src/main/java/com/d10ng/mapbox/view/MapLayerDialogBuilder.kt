package com.d10ng.mapbox.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.dialog.builder.BaseDialogBuilder
import com.d10ng.compose.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.mapbox.constant.MapLayerType
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow

data class MapLayerDialogBuilder(
    val value: MapLayerType,
    val isOnlyShowCanDown: Boolean = false,
    val onChange: (MapLayerType) -> Unit,
) : DialogBuilder() {

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
                    contentDescription = stringResource(id = type.nameSrcId),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(AppShape.RC.v8)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (isSelect) AppColor.System.secondary else AppColor.Text.hint)
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = stringResource(id = type.nameSrcId),
                        style = AppText.Normal.OnSecondary.v14,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }

    @Composable
    override fun Build() {
        BaseDialogBuilder(
            title = "请选择地图"
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                mainAxisAlignment = FlowMainAxisAlignment.SpaceEvenly,
                crossAxisSpacing = 16.dp
            ) {
                MapLayerType.values().filter {
                    if (isOnlyShowCanDown) {
                        it.isCanDown
                    } else true
                }.forEach { type ->
                    ItemView(type = type, isSelect = value == type) {
                        onChange.invoke(type)
                    }
                }
            }
        }.Build()
    }
}
