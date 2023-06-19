package com.wgroup.woooo_app.woooo.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginButton() {

     val loginViewModel = viewModel<LoginViewModel>()

    val flowColor by loginViewModel.color.collectAsState()


    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Button(
            onClick = {
                loginViewModel.generateNewColor()
        }) {
            Text(text = "Login Button")

        }

        Box(modifier = Modifier.fillMaxSize().background(color = Color(flowColor)) )
    }
}