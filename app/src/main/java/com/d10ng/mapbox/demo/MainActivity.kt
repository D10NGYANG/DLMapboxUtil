package com.d10ng.mapbox.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.applib.app.goTo
import com.d10ng.compose.BaseActivity
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.AppTheme
import com.d10ng.compose.view.SolidButtonWithText
import com.d10ng.http.Http
import com.d10ng.mapbox.activity.map.MapActivity
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.activity.show.goToLocationShowActivity
import com.d10ng.mapbox.model.MapboxModel
import com.d10ng.tianditu.TianDiTuApiManager
import com.mapbox.geojson.Point

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initColor()
        MapboxModel.instant.init(BuildConfig.myMapboxToken)
        Http.init("", true)
        TianDiTuApiManager.init(BuildConfig.myTiandituToken)

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