package com.wgroup.woooo_app.woooo.feature.auth.screen

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginViewModel

@Composable
fun LoginScreen(){

    val loginViewModel:LoginViewModel = hiltViewModel()

    val response = loginViewModel.loginResponse.value
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        
        Button(onClick = {

            loginViewModel.login()

        }) {

            Text(text = "Login")


        }

        if(response.error.isNotEmpty())
            Box(modifier = Modifier, contentAlignment = Alignment.Center){
                Text(text = response.error)
            }

        if(response.isLoading)
            Box(modifier = Modifier, contentAlignment = Alignment.Center){
                CircularProgressIndicator()
            }
    }
    
}