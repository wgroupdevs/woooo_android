package com.wgroup.woooo_app.woooo.feature.settings.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.VolumeUp
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.destinations.AudioVideoMainScreenDestination
import com.wgroup.woooo_app.woooo.destinations.DisplayMainScreenDestination
import com.wgroup.woooo_app.woooo.destinations.LanguageMainScreenDestination
import com.wgroup.woooo_app.woooo.destinations.SoundAndVibrationMainScreenDestination
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun ApplicationMainView(navigator: DestinationsNavigator) {

    Column {
        TopBarForSetting(onBackPressed = { navigator.popBackStack() })

        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                modifier = Modifier.padding(Dimension.dimen_10),
                text = Strings.appText,
                style = MaterialTheme.typography.headlineMedium
            )


            CustomListTile(
                leadingIcon = {
                    Icon(
                        tint = WooColor.white,

                        modifier = Modifier.size(
                            36.dp
                        ),
                        imageVector = Icons.Outlined.VolumeUp,contentDescription = "",
                    )
                },
                title = Strings.soundText,
                onClick = {
                    navigator.navigate(SoundAndVibrationMainScreenDestination)
                },
            )
            CustomListTile(leadingIcon = {
                Icon(
                    tint = WooColor.white,modifier = Modifier.size(
                        36.dp
                    ),imageVector = Icons.Outlined.WbSunny,contentDescription = ""
                )
            },title = Strings.displyText,onClick = {
                navigator.navigate(DisplayMainScreenDestination)
            })
            CustomListTile(leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ),imageVector = Icons.Outlined.Mic,contentDescription = ""
                )
            },title = Strings.audioVideoText,onClick = {
                navigator.navigate(
                    AudioVideoMainScreenDestination
                )
            })
            CustomListTile(leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ),imageVector = Icons.Outlined.Language,contentDescription = ""
                )
            },
                title = Strings.lanText,
                onClick = { navigator.navigate(LanguageMainScreenDestination) })
        }
    }
}