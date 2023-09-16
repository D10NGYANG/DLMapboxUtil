package com.d10ng.mapbox.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.d10ng.compose.ui.AppShape

/**
 * 导航栏图标按钮
 * @Author d10ng
 * @Date 2023/9/16 13:11
 */

/**
 * 导航栏图标按钮
 * @param icon Int 图标资源
 * @param onClick Function0<Unit> 点击事件
 */
@Composable
internal fun NavBarIconButton(
    icon: Int,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(id = icon),
        contentDescription = "",
        modifier = Modifier
            .padding(end = 4.dp)
            .size(46.dp)
            .clip(AppShape.RC.Cycle)
            .clickable { onClick() },
        contentScale = ContentScale.Inside
    )
}