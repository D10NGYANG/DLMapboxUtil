package com.d10ng.mapbox.activity.map

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.compose.ui.AppColor
import com.d10ng.mapbox.activity.BaseMapboxActivity

class MapActivity : BaseMapboxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = AppColor.toColorScheme()
            ) {
                MapMainScreen()
                UiViewModelManager.Init()
            }
        }
    }
}