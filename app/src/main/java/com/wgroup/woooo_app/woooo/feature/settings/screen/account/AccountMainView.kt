package com.wgroup.woooo_app.woooo.feature.settings.screen.account


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.outlined.Backpack
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Compare
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.NoTransfer
import androidx.compose.material.icons.outlined.Security
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
fun AccountMainView() {
    Column() {
        TopBarForSetting(onBackPressed = {})
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                modifier = Modifier.padding(Dimension.dimen_10),
                text = Strings.accountText,
                style = MaterialTheme.typography.headlineMedium
            )
            CustomListViewForSetting(
                leadingIcon = {
                    Icon(
                        tint = WooColor.white,

                        modifier = Modifier.size(
                            36.dp
                        ),
                        imageVector = Icons.Outlined.Lock, contentDescription = "",
                    )
                },
                title = Strings.privacyText, onClick = {},
            )
            CustomListViewForSetting(leadingIcon = {
                Icon(
                    tint = WooColor.white, modifier = Modifier.size(
                        36.dp
                    ), imageVector = Icons.Outlined.Security, contentDescription = ""
                )
            }, title = Strings.securityText, onClick = {})
            CustomListViewForSetting(leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ), imageVector = Icons.Default.Compare, contentDescription = ""
                )
            }, title = Strings.changeNumberText, onClick = {})
            CustomListViewForSetting(leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ), imageVector = Icons.Outlined.Delete, contentDescription = ""
                )
            }, title = Strings.deleteAccountText, onClick = {})
            CustomListViewForSetting(leadingIcon = {
                Icon(
                    tint = WooColor.white,

                    modifier = Modifier.size(
                        36.dp
                    ), imageVector = Icons.Outlined.Backup, contentDescription = ""
                )
            }, title = Strings.backupText, onClick = {})
        }

    }
}