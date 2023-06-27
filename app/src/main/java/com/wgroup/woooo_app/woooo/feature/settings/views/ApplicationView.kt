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
import com.wgroup.woooo_app.woooo.shared.components.CustomListViewForSetting
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun ApplicationMainScreen() {


    Column() {
        TopBarForSetting(){}

    Column(modifier = Modifier.padding(12.dp)) {


        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.appText,
            style = MaterialTheme.typography.headlineMedium
        )


        CustomListViewForSetting(
            leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ),
                    imageVector = Icons.Outlined.VolumeUp, contentDescription = "",
                )
            },
            title = Strings.soundText, onClick = {},
        )
        CustomListViewForSetting(leadingIcon = {
            Icon(
                tint = WooColor.white, modifier = Modifier.size(
                    36.dp
                ), imageVector = Icons.Outlined.WbSunny, contentDescription = ""
            )
        }, title = Strings.displyText, onClick = {})
        CustomListViewForSetting(leadingIcon = {
            Icon(
                tint = WooColor.white,

                modifier = Modifier.size(
                    36.dp
                ), imageVector = Icons.Outlined.Mic, contentDescription = ""
            )
        }, title = Strings.audioVideoText, onClick = {})
        CustomListViewForSetting(leadingIcon = {
            Icon(
                tint = WooColor.white,

                modifier = Modifier.size(
                    36.dp
                ), imageVector = Icons.Outlined.Language, contentDescription = ""
            )
        }, title = Strings.lanText, onClick = {})
    }
}}