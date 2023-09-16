package com.d10ng.mapbox.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.d10ng.app.base.goTo
import com.d10ng.compose.ui.AppColor
import com.d10ng.compose.ui.base.Button
import com.d10ng.http.Http
import com.d10ng.mapbox.activity.map.MapActivity
import com.d10ng.mapbox.activity.search.LocationSearchManager
import com.d10ng.mapbox.activity.show.goToLocationShowActivity
import com.d10ng.mapbox.stores.MapboxStore
import com.d10ng.tianditu.TianDiTuApiManager
import com.mapbox.geojson.Point

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initColor()
        MapboxStore.init(BuildConfig.myMapboxToken)
        Http.init("", true)
        TianDiTuApiManager.init(BuildConfig.myTiandituToken)

        setContent {
            var selectPoint by remember {
                mutableStateOf(Point.fromLngLat(116.411794, 39.9068))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColor.Neutral.bg)
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(text = "地图", onClick = {
                    goTo(MapActivity::class.java)
                })
                Button(text = "搜索", onClick = {
                    LocationSearchManager.instant.startActivity(this@MainActivity) {
                        if (it != null) {
                            selectPoint = it
                        }
                    }
                })
                Button(text = "显示", onClick = {
                    goToLocationShowActivity(selectPoint.latitude(), selectPoint.longitude())
                })
            }
        }
    }
}

fun initColor() {
    AppColor.Main.primary = Color(0xFFFC4107)
}