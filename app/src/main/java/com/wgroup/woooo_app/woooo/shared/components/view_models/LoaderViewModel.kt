package com.wgroup.woooo_app.woooo.shared.components.view_models


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoaderViewModel @Inject constructor() : ViewModel(){

    private val _setLoaderShow = mutableStateOf(false)
    /// for get value from View Model
    val getLoaderShow: State<Boolean> = _setLoaderShow

    /// for set value from View Model
    fun setLoaderValue(isEnabled: Boolean) {
        _setLoaderShow.value = isEnabled
    }
}