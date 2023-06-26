package com.wgroup.woooo_app.woooo.feature.settings.screen.application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.shared.components.CustomListViewForSetting
import com.wgroup.woooo_app.woooo.shared.components.TopBarTextFieldForSetting
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun DisplayView() {
    Column(modifier = Modifier.padding(10.dp)) {
        TopBarTextFieldForSetting(onBackPressed = {})
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.displyText,
            style = MaterialTheme.typography.headlineMedium
        )


        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.wallpaperText, onClick = {},
            trailingContent = { Text(text = "Default") }
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.themeText,
            onClick = {},
            trailingContent = {
                Text(
                    text = "Default",
                    style = MaterialTheme.typography.labelSmall
                )
            })

    }

}