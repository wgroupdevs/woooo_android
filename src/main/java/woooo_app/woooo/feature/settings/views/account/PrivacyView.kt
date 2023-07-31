package woooo_app.woooo.feature.settings.views.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.utils.Dimension

@Composable
fun PrivacyMainView(navigator: DestinationsNavigator) {
AppBackGround {

    Column(modifier = Modifier.padding(10.dp)) {
        TopBarForSetting(onBackPressed = {navigator.popBackStack()})
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.privacyText,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.permissionsText,
            style = MaterialTheme.typography.headlineMedium,

            )
        CustomListTile(leadingIcon = {},
            title = Strings.profilePhotoText,
            onClick = {},
            trailingContent = { Text(text = Strings.myContactText,fontSize = 10.sp) },
            fontSize = 16
        )
        CustomListTile(leadingIcon = {},
            title = Strings.abtText,
            fontSize = 16,
            onClick = {},
            trailingContent = {
                Text(
                    fontSize = 10.sp,text = Strings.myContactText
                )
            })
        CustomListTile(leadingIcon = {},
            title = Strings.lastSeenText,
            fontSize = 16,
            onClick = {},
            trailingContent = {
                Text(
                    fontSize = 10.sp,text = Strings.myContactText
                )
            })
        CustomListTile(leadingIcon = {},
            title = Strings.connectContactsText,
            fontSize = 16,
            onClick = {},
            supportingContent = {
                Text(
                    text = Strings.connectLongString,style = MaterialTheme.typography.labelSmall
                )
            },
            trailingContent = {
                val checkedState = remember { mutableStateOf(true) }
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it },
                    colors = SwitchDefaults.colors(
                        checkedBorderColor = Color.Transparent,
                        checkedIconColor = Color.Transparent,
                        checkedThumbColor = WooColor.white,
                        checkedTrackColor = WooColor.textFieldBackGround,
                        uncheckedBorderColor = Color.Transparent,
                        uncheckedIconColor = WooColor.white,
                        uncheckedThumbColor = WooColor.switchTrackColor,
                        uncheckedTrackColor = WooColor.white,
                        disabledCheckedThumbColor = WooColor.switchTrackColor,
                    )
                )
            })
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.videoText,
            style = MaterialTheme.typography.headlineMedium,

            )
        CustomListTile(leadingIcon = {},
            title = Strings.clearAllChatsText,
            onClick = {},
            trailingContent = { Icon(imageVector = Icons.Outlined.RemoveCircleOutline,contentDescription = "")},
            fontSize = 16
        )
        CustomListTile(leadingIcon = {},
            title = Strings.deleteAllChatsText,
            onClick = {},
            trailingContent = { Icon(imageVector = Icons.Outlined.Delete,contentDescription = "") },
            fontSize = 16
        )
    }
}
}
