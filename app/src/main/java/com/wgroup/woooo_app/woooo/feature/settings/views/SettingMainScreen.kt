package com.wgroup.woooo_app.woooo.feature.settings.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContactPhone
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.shared.components.CustomListViewForSetting
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun SettingMainView() {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.stngText,
            style = MaterialTheme.typography.headlineMedium
        )


        CustomListViewForSetting(
            leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ),
                    imageVector = Icons.Outlined.Settings, contentDescription = "",
                )
            },
            title = Strings.appText, onClick = {},
        )
        CustomListViewForSetting(leadingIcon = {
            Icon(
                tint = WooColor.white, modifier = Modifier.size(
                    36.dp
                ), imageVector = Icons.Outlined.ContactPhone, contentDescription = ""
            )
        }, title = Strings.accountText, onClick = {})
        CustomListViewForSetting(leadingIcon = {
            Icon(
                tint = WooColor.white,

                modifier = Modifier.size(
                    36.dp
                ), imageVector = Icons.Outlined.Key, contentDescription = ""
            )
        }, title = Strings.walletPinText, onClick = {})
        CustomListViewForSetting(leadingIcon = {
            Icon(
                tint = WooColor.white,

                modifier = Modifier.size(
                    36.dp
                ), imageVector = Icons.Outlined.Report, contentDescription = ""
            )
        }, title = Strings.rptText, onClick = {})
    }
}