package com.d10ng.mapbox.view

import com.d10ng.mapbox.R

enum class OtherMapType(val iconId: Int, val text: String) {
    BAIDU(R.drawable.ic_baiduditu_40, "百度地图"),
    GAODE(R.drawable.ic_gaodeditu_40, "高德地图"),
    CANCEL(0, "取消")

    ;

    companion object {
        fun toDialogMap(): Map<String, OtherMapType> {
            val map = mutableMapOf<String, OtherMapType>()
            values().forEach {
                map[it.text] = it
            }
            return map
        }
    }
}

//@Composable
//fun OtherMapRadioDialogItem(
//    info: Pair<String, Any>,
//    onClick: () -> Unit
//) {
//    @Suppress("UNCHECKED_CAST")
//    info as Pair<String, OtherMapType>
//    if (info.second == OtherMapType.CANCEL) {
//        DialogCancelButton(
//            modifier = Modifier
//                .padding(top = 16.dp)
//                .fillMaxWidth(),
//            text = info.second.text,
//            color = AppColor.Text.body,
//            onClick = onClick
//        )
//    } else {
//        ListItem(
//            modifier = Modifier
//                .padding(top = 16.dp)
//                .fillMaxWidth(),
//            title = info.second.text,
//            iconId = if (info.second.iconId != 0) info.second.iconId else null,
//            onClick = onClick
//        )
//    }
//}