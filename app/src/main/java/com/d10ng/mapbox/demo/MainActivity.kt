package com.d10ng.mapbox.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.applib.app.goTo
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.compose.AppColor
import com.d10ng.basicjetpackcomposeapp.compose.AppTheme
import com.d10ng.basicjetpackcomposeapp.view.SolidButtonWithText
import com.d10ng.http.Http
import com.d10ng.mapbox.activity.map.MapActivity
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.activity.show.goToLocationShowActivity
import com.d10ng.mapbox.model.MapboxModel
import com.d10ng.tianditu.TianDiTuApiManager
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.mapbox.geojson.Point

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initColor()
        MapboxModel.instant.init("pk.eyJ1IjoiaXJpbTEwMCIsImEiOiJjazEzOGNrZnIwM3VnM3BsOGlqNmxxaTg2In0.3UJxGXm1bELP37dgMH0VSA")
        Http.init("", true)
        TianDiTuApiManager.init("5bb740ffd3a80fb3963e022454eca6e2")

        setContent {
            var selectPoint by remember {
                mutableStateOf(Point.fromLngLat(113.22, 22.33))
            }
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
                    SolidButtonWithText(text = "搜索", onClick = {
                        LocationSearchManager.instant.startActivity(this@MainActivity) {
                            if (it != null) {
                                selectPoint = it
                            }
                        }
                    })
                    SolidButtonWithText(text = "显示", onClick = {
                        goToLocationShowActivity(selectPoint.latitude(), selectPoint.longitude())
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