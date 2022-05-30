package com.d10ng.mapbox.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.applib.app.goTo
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppTheme
import com.d10ng.basicjetpackcomposeapp.view.SolidButtonWithText
import com.d10ng.mapbox.activity.map.MapActivity
import com.d10ng.mapbox.model.MapboxModel
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initColor()
        MapboxModel.instant.init("pk.eyJ1IjoiaXJpbTEwMCIsImEiOiJjazEzOGNrZnIwM3VnM3BsOGlqNmxxaTg2In0.3UJxGXm1bELP37dgMH0VSA")
        setContent {
            AppTheme(app = app) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppColor.System.background)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SolidButtonWithText(text = "地图", onClick = {
                        goTo(MapActivity::class.java)
                    })
                }
            }
        }
    }
}

fun initColor() {
    AppColor.System.primary = Color(0xFFFFFFFF)
    AppColor.System.primaryVariant = Color(0xFFFFFFFF)
    AppColor.System.secondary = Color(0xFFFC4107)
    AppColor.System.secondaryVariant = Color(0xFFE83802)
    AppColor.System.background = Color(0xFFF8F8F8)
    AppColor.System.divider = Color(0xFFE2E2E2)
    AppColor.On.primary = Color(0xFF333333)
    AppColor.On.secondary = Color(0xFFFFFFFF)
    AppColor.On.background = Color(0xFF333333)
    AppColor.Text.title = Color(0xFF333333)
    AppColor.Text.body = Color(0xFF666666)
    AppColor.Text.hint = Color(0xFF999999)
    AppColor.Text.error = Color(0xFFDA5A5A)
}