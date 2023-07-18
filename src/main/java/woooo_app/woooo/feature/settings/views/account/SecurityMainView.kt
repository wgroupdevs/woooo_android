package com.wgroup.woooo_app.woooo.feature.settings.views.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun SecurityMainView(navigator: DestinationsNavigator) {
    Column {
        TopBarForSetting(onBackPressed = { navigator.popBackStack() })

        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                modifier = Modifier.padding(Dimension.dimen_10),
                text = Strings.securityText,
                style = MaterialTheme.typography.headlineMedium
            )
            CustomListTile(
                leadingIcon = {},
                title = Strings.setPasscodeLockText,
                onClick = {},
            )
            CustomListTile(leadingIcon = {},title = Strings.remoteLogOutText,onClick = {})
            CustomListTile(leadingIcon = {},title = Strings.changePasswordText,onClick = {})
            CustomListTile(leadingIcon = {},title = Strings.fingerPrintText,onClick = {})
            CustomListTile(leadingIcon = {},title = Strings.encryptionText,onClick = {})
        }

    }
}