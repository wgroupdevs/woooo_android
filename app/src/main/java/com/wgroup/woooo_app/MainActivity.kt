package com.wgroup.woooo_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.DestinationsNavHost
import com.wgroup.woooo_app.woooo.NavGraphs
import com.wgroup.woooo_app.woooo.feature.settings.views.account.SecurityMainView
import com.wgroup.woooo_app.woooo.theme.Woooo_androidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    Woooo_androidTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background
        ) {
//            DestinationsNavHost(navGraph = NavGraphs.root)
            SecurityMainView()
        }
    }
}
