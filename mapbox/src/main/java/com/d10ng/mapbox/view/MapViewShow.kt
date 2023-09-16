package com.d10ng.mapbox.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape
import com.d10ng.compose.ui.AppText
import com.d10ng.mapbox.R
import com.d10ng.mapbox.stores.LocationStore
import com.d10ng.mapbox.utils.toShowString

/**
 * 在地图上浮现的组件
 * @Author d10ng
 * @Date 2023/9/16 11:22
 */

/**
 * 指南针
 * @receiver BoxScope
 */
@Composable
fun BoxScope.Compass() {
    Image(
        painter = painterResource(id = R.mipmap.ic_map_compass),
        contentDescription = "北向指示",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .padding(16.dp)
            .size(37.dp)
            .align(Alignment.TopEnd)
    )
}

/**
 * 用户位置文本
 * @receiver BoxScope
 */
@Composable
fun BoxScope.UserLocationTextBar() {
    val loc by LocationStore.getValueFlow().collectAsState(initial = null)
    LocationTextBar(text = loc?.toShowString("当前位置:") ?: "正在获取当前位置...")
}

/**
 * 目标位置文本
 * @receiver BoxScope
 * @param text String
 */
@Composable
fun BoxScope.LocationTextBar(
    text: String
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xCCFFFFFF), AppShape.RC.v8)
            .padding(12.dp)
            .align(Alignment.TopStart),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = AppText.Normal.Title.mini)
    }
}