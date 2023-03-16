package com.d10ng.mapbox.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.d10ng.applib.resource.makeBitmapFromDrawable
import com.d10ng.compose.dialog.builder.BaseDialogBuilder
import com.d10ng.compose.dialog.builder.DialogBuilder
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.R
import com.d10ng.mapbox.model.MapModel
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions

data class LocationSureDialogBuilder(
    var title: String = "提示",
    var titleAlign: Alignment.Horizontal = Alignment.Start,
    var titleColor: Color = AppColor.Text.title,
    var message: String = "",
    var messageAlign: Alignment.Horizontal = Alignment.Start,
    var messageColor: Color = AppColor.Text.body,
    var sureButton: String = "确定",
    var sureButtonTextColor: Color = AppColor.On.secondary,
    var sureButtonBackgroundColor: Color = AppColor.System.secondary,
    var cancelButton: String = "取消",
    var cancelButtonTextColor: Color = AppColor.Text.body,
    var onClickSure: (() -> Unit)? = null,
    var onClickCancel: (() -> Unit)? = null,
    val target: Point
) : DialogBuilder() {

    companion object {
        private const val TARGET = "TARGET"
    }

    @Composable
    override fun Build() {
        val context = LocalContext.current
        val pointOptions = remember(target) {
            mapOf(
                1 to PointAnnotationOptions()
                    .withGeometry(target)
                    .withIconImage(TARGET)
                    .withIconSize(1.0)
            )
        }
        BaseDialogBuilder(
            title = title,
            titleAlign = titleAlign,
            titleColor = titleColor,
            message = message,
            messageAlign = messageAlign,
            messageColor = messageColor,
            sureButton = sureButton,
            sureButtonTextColor = sureButtonTextColor,
            sureButtonBackgroundColor = sureButtonBackgroundColor,
            cancelButton = cancelButton,
            cancelButtonTextColor = cancelButtonTextColor,
            onClickSure = onClickSure,
            onClickCancel = onClickCancel
        ) {
            var zoom by remember {
                mutableStateOf(MAP_BOX_ZOOM_DEFAULT)
            }
            var center by remember(target) {
                mutableStateOf(target)
            }
            val height = LocalConfiguration.current.screenHeightDp.dp / 4
            MapboxView(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(height),
                layer = MapModel.instant.layerTypeFlow.value,
                cameraZoom = zoom,
                cameraTarget = center,
                onCameraZoomChange = { zoom = it },
                onCameraCenterChange = { center = it },
                onStyleLoad = { style ->
                    context.makeBitmapFromDrawable(R.drawable.ic_map_location_target_25)?.apply {
                        style.addImage(TARGET, this)
                    }
                },
                pointOptions = pointOptions
            )
        }.Build()
    }
}