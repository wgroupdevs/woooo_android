package com.wgroup.woooo_app.woooo.feature.home.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups3
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun DashboardView(navigator: DestinationsNavigator) {

    Scaffold(bottomBar = {

        NavigationBar(

            containerColor = WooColor.primary,
            tonalElevation = 5.dp
        ) {
            val inner_wheel = painterResource(id = R.drawable.inner_wheel)
            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        Icons.Outlined.Chat,
                        contentDescription = "Chat"
                    )
                },
                label = {
                    Text(
                        text = "Chat",
                        style = MaterialTheme.typography.labelSmall.copy(color = WooColor.white)
                    )
                },
                selected = true,
                onClick = { },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = WooColor.white,
                        indicatorColor = WooColor.primary
                    )
            )

            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        Icons.Outlined.Call,
                        contentDescription = "Chat"
                    )
                },
                label = {
                    Text(
                        text = "Call",
                        style = MaterialTheme.typography.labelSmall.copy(color = WooColor.white)
                    )
                },
                selected = true,
                onClick = { },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = WooColor.white,
                        indicatorColor = WooColor.primary
                    )
            )
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clickable {
                        navigator.popBackStack()
                    },
                painter = inner_wheel,
                contentDescription = "inner circle",
                contentScale = ContentScale.FillBounds,
            )

//            NavigationBarItem(
//                icon = {
//                    androidx.compose.material3.Icon(
//                        Icons.Outlined.Circle,
//                        contentDescription = "Home"
//                    )
//                },
//                label = { Text(text = "Home") },
//                selected = true,
//                onClick = { },
//                colors = androidx.compose.material3.NavigationBarItemDefaults
//                    .colors(
//                        selectedIconColor = WooColor.white,
//                        indicatorColor = WooColor.primary
//                    )
//            )
//

            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        Icons.Outlined.Groups3,
                        contentDescription = "Meeting"
                    )
                },
                label = {
                    Text(
                        text = "Meeting",
                        style = MaterialTheme.typography.labelSmall.copy(color = WooColor.white)
                    )
                },
                selected = true,
                onClick = { },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = WooColor.white,
                        indicatorColor = WooColor.primary
                    )
            )
            NavigationBarItem(
                icon = {
                    androidx.compose.material3.Icon(
                        Icons.Outlined.Wallet,
                        contentDescription = "wallet"
                    )
                },
                label = {
                    Text(
                        text = "Wallet",
                        style = MaterialTheme.typography.labelSmall.copy(color = WooColor.white)
                    )
                },
                selected = true,
                onClick = { },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = WooColor.white,
                        indicatorColor = WooColor.primary
                    )
            )

        }
    }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

        }
    }


}


