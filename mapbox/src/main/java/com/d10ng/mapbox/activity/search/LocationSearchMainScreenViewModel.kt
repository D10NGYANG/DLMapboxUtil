package com.d10ng.mapbox.activity.search

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.d10ng.basicjetpackcomposeapp.BaseActivity
import com.d10ng.basicjetpackcomposeapp.BaseComposeScreenObject
import com.d10ng.tianditu.api.TianDiTuApi
import com.d10ng.tianditu.bean.LocationSearch
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

object LocationSearchMainScreenObj: BaseComposeScreenObject("LocationSearchMainScreen") {
    @OptIn(ExperimentalAnimationApi::class)
    override fun composable(
        builder: NavGraphBuilder,
        controller: NavHostController,
        act: BaseActivity
    ) {
        builder.composable(name) {
            LocationSearchMainScreen(controller, act)
        }
    }
}

class LocationSearchMainScreenViewModel(
    private val controller: NavHostController,
    act: BaseActivity
): ViewModel() {
    class Factory(
        private val controller: NavHostController,
        private val act: BaseActivity
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocationSearchMainScreenViewModel(controller, act) as T
        }
    }

    private val weakAct = WeakReference(act)

    /** 输入搜索内容 */
    val inputFlow = MutableStateFlow("")
    /** 结果 */
    val resultFlow = MutableStateFlow<LocationSearch?>(null)

    /** 点击返回 */
    fun onClickBack() {
        weakAct.get()?.finish()
    }

    /** 更新搜索内容 */
    fun updateInput(value: String) {
        inputFlow.value = value
        search(value)
    }

    /** 点击搜索 */
    fun onClickSearch() {
        search(inputFlow.value)
    }

    /** 搜索 */
    private fun search(value: String) {
        if (value.isEmpty()) resultFlow.value = null
        viewModelScope.launch {
            val result = TianDiTuApi.getLocationSearchV2(value)
            if (result != null && result.status.infocode != 1000) {
                weakAct.get()?.app?.showError(result.status.cndesc)
                resultFlow.emit(null)
            } else resultFlow.emit(result)
        }
    }

    /** 点击区域 */
    fun onClickItem(value: LocationSearch.Area) {

    }

    /** 点击区域 */
    fun onClickItem(value: LocationSearch.Statistics.AllAdmin) {

    }

    /** 点击搜索结果 */
    fun onClickItem(value: LocationSearch.Poi) {

    }
}