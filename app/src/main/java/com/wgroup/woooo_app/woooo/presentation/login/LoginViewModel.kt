package com.wgroup.woooo_app.woooo.presentation.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _color = MutableStateFlow(0xFFFBBFFF)
    val color = _color.asStateFlow()
    fun generateNewColor() {
        val newColor = Random.nextLong(0xFFFFFFFF)
        _color.value = newColor
    }


}