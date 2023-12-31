package com.wgroup.woooo_app.woooo.feature.settings.views.application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun AudioVideoView(navigator: DestinationsNavigator) {

    Column(modifier = Modifier.padding(10.dp)) {
        TopBarForSetting(onBackPressed = { navigator.popBackStack() })
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.audioAndVideoText,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.audioText,
            style = MaterialTheme.typography.headlineMedium,

            )
        CustomListTile(leadingIcon = {},
            title = Strings.MicroPhoneText,
            onClick = {},
            trailingContent = { Text(text = Strings.communicationDeviceText,fontSize = 10.sp) },
            fontSize = 16
        )
        CustomListTile(leadingIcon = {},
            title = Strings.themeText,
            fontSize = 16,
            onClick = {},
            trailingContent = {
                Text(
                    fontSize = 10.sp,text = "Default",style = MaterialTheme.typography.labelSmall
                )
            })
        CustomListTile(leadingIcon = {},
            title = Strings.speakerText,
            fontSize = 16,
            onClick = {},
            trailingContent = {
                Text(
                    fontSize = 10.sp,
                    text = Strings.speakerText,
                    style = MaterialTheme.typography.labelSmall
                )
            })
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.videoText,
            style = MaterialTheme.typography.headlineMedium,

            )
        CustomListTile(leadingIcon = {},
            title = Strings.cameraSettingText,
            onClick = {},
            trailingContent = { Text(text = Strings.communicationDeviceText,fontSize = 10.sp) },
            fontSize = 16
        )
    }
}