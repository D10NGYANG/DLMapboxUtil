package com.d10ng.mapbox.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.app.utils.goTo
import com.d10ng.app.view.lockScreenOrientation
import com.d10ng.app.view.setStatusBar
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Button
import com.d10ng.compose.ui.base.ButtonType
import com.d10ng.mapbox.MapboxUtil
import com.d10ng.mapbox.activity.map.MapActivity
import com.d10ng.mapbox.activity.navigation.MapNavigationActivity
import com.d10ng.mapbox.activity.offline.MapOfflineActivity
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.activity.show.goToLocationShowActivity
import com.mapbox.geojson.Point

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lockScreenOrientation()
        setStatusBar()

        initColor()
        MapboxUtil.init(BuildConfig.myMapboxToken, BuildConfig.myTiandituToken, true)

        setContent {

            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                var selectPoint by remember {
                    mutableStateOf(Point.fromLngLat(116.411794, 39.9068))
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(text = "地图", type = ButtonType.PRIMARY, onClick = {
                        goTo(MapActivity::class.java)
                    })
                    Button(text = "离线", type = ButtonType.PRIMARY, onClick = {
                        goTo(MapOfflineActivity::class.java)
                    })
                    Button(text = "搜索", type = ButtonType.PRIMARY, onClick = {
                        LocationSearchManager.start {
                            if (it != null) {
                                selectPoint = it
                            }
                        }
                    })
                    Button(text = "显示", type = ButtonType.PRIMARY, onClick = {
                        goToLocationShowActivity(selectPoint.latitude(), selectPoint.longitude())
                    })
                    Button(text = "导航", type = ButtonType.PRIMARY, onClick = {
                        goTo(MapNavigationActivity::class.java)
                    })
                }
            }
        }
    }
}

fun initColor() {
    AppColor.Main.primary = Color(0xFFFC4107)
}