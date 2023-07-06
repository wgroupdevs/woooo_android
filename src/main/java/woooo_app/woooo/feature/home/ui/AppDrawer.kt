package com.wgroup.woooo_app.woooo.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.ViewDivider
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import eu.siacs.conversations.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    navigateToSettings: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {

    ModalDrawerSheet(
        modifier = modifier
            .border(
                border = BorderStroke(width = 0.5.dp, color = WooColor.white),
                shape = RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp)
            ),
        drawerContainerColor = Color.Transparent
    ) {
        DrawerHeader(modifier)
//        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))
        Column(modifier = Modifier.padding(Dimension.dimen_10)) {

            ViewDivider()
            VerticalSpacer()
            CustomListTile(
                title = "Settings",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        tint = WooColor.white
                    )
                }, onClick = {
//                    navigator.navigate(SettingsScreenDestination)
                })

            CustomListTile(
                title = "Invite friend",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Invite friend",
                        tint = WooColor.white
                    )
                }, onClick = {
//                    navigator.navigate(SettingsScreenDestination)
                })

            CustomListTile(
                title = "Help & Feedback",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Feedback",
                        tint = WooColor.white
                    )
                }, onClick = {
//                    navigator.navigate(SettingsScreenDestination)
                })
            CustomListTile(
                title = "Share referral code",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Referral",
                        tint = WooColor.white
                    )
                }, onClick = {
//                    navigator.navigate(SettingsScreenDestination)
                })
            CustomListTile(
                title = "Add invitation Code",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Pin,
                        contentDescription = "invitation",
                        tint = WooColor.white
                    )
                }, onClick = {
//                    navigator.navigate(SettingsScreenDestination)
                })
            CustomListTile(
                title = "Logout",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = "Logout",
                        tint = WooColor.white
                    )
                }, onClick = {
//                    navigator.navigate(SettingsScreenDestination)
                })
        }


    }


}


@Composable
fun DrawerHeader(modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {


        Image(
            painterResource(id = R.drawable.woooo_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(Dimension.dimen_100)
                .clip(CircleShape)
        )



        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(
                text = "Muhammad Ehsan",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(
                text = "Edit profile",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable { })

        }
    }
}

