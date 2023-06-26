package com.wgroup.woooo_app.woooo.feature.settings.screen.application

import android.widget.ToggleButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.woooo.shared.components.CustomListViewForSetting
import com.wgroup.woooo_app.woooo.shared.components.TopBarTextFieldForSetting
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun SoundAndVibrationView() {

    Column(modifier = Modifier.padding(10.dp)) {
        TopBarTextFieldForSetting(onBackPressed = {})
        Text(
            modifier = Modifier.padding(top = Dimension.dimen_5, start = Dimension.dimen_5),
            text = Strings.soundText,
            style = MaterialTheme.typography.headlineMedium
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.MuteNotiText,
            onClick = {},
            supportingContent = {
                Text(
                    fontSize = 10.sp,
                    text = Strings.muteNotiSubTiText,
                    style = MaterialTheme.typography.labelSmall
                )
            },
            trailingContent = {
                val context = LocalContext.current
                ToggleButton(context)
            },
        )
        Text(
            modifier = Modifier.padding(top = Dimension.dimen_5, start = Dimension.dimen_5),
            text = Strings.msgsText,
            style = MaterialTheme.typography.headlineMedium
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.notiTuneText,
            onClick = {},
            trailingContent = {
                Text(text = Strings.onText)
            },
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.vibText,
            onClick = {},
            trailingContent = {
                Text(text = Strings.onText)
            },
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.PopText,
            onClick = {},

            )
        Text(
            modifier = Modifier.padding(top = Dimension.dimen_5, start = Dimension.dimen_5),
            text = Strings.callText,
            style = MaterialTheme.typography.headlineMedium
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.ringText,
            onClick = {},
            trailingContent = {
                Text(text = Strings.onText)
            },
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.vibText,
            onClick = {},
            trailingContent = {
                Text(text = Strings.onText)
            },
        )
        Text(
            modifier = Modifier.padding(top = Dimension.dimen_5, start = Dimension.dimen_5),
            text = Strings.callText,
            style = MaterialTheme.typography.headlineMedium
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.ringText,
            onClick = {},
            trailingContent = {
                Text(text = Strings.onText)
            },
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.grpText,
            onClick = {},
            trailingContent = {
                Text(text = Strings.onText)
            },
        )

    }


}