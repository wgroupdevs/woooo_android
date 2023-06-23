package com.wgroup.woooo_app.woooo.feature.home.viewmodel

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CircularMenuViewModel @Inject constructor() : ViewModel() {

    val TAG: String = "CircularMenuViewModel"
    val clockWiseRotation = mutableStateOf(Animatable(0f))
    val isOuterRotatingClockWise = mutableStateOf(false)
    val antiClockWiseRotation = mutableStateOf(Animatable(0f))


    suspend fun rotateOuterCircleClockWise() {
        isOuterRotatingClockWise.value = true
        Log.d(TAG, "Outer Circle ClockWise Rotation Started...")
        clockWiseRotation.value.animateTo(
            targetValue = 90f,
            animationSpec = tween(1000, easing = LinearEasing),
        )
        clockWiseRotation.value.snapTo(0f)
    }

    suspend fun rotateMiddleCircleAntiClockWise() {
        antiClockWiseRotation.value.animateTo(
            targetValue = -90f,
            animationSpec = tween(1000, easing = LinearEasing),
        )
        antiClockWiseRotation.value.snapTo(0f)
    }

    suspend fun rotateOuterCircleAntiClockWise() {
        isOuterRotatingClockWise.value = false
        antiClockWiseRotation.value.animateTo(
            targetValue = -90f,
            animationSpec = tween(1000, easing = LinearEasing),
        )
        antiClockWiseRotation.value.snapTo(0f)
    }

    suspend fun rotateMiddleCircleClockWise() {
        clockWiseRotation.value.animateTo(
            targetValue = 90f,
            animationSpec = tween(1000, easing = LinearEasing),
        )
        clockWiseRotation.value.snapTo(0f)
    }


}