package com.d10ng.mapbox.activity.map

import android.os.Bundle
import androidx.activity.compose.setContent
import com.d10ng.compose.model.UiViewModelManager
import com.d10ng.mapbox.activity.BaseMapboxActivity

class MapActivity : BaseMapboxActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MapMainScreen()
            UiViewModelManager.Init(act = this)
        }
    }
}