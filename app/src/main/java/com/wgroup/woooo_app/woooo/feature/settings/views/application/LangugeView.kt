package com.wgroup.woooo_app.woooo.feature.settings.views.application

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.shared.components.CustomListViewForSetting
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun LanguageView() {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.lanText,
            style = MaterialTheme.typography.headlineMedium
        )


        CustomListViewForSetting(
            leadingIcon = {},
            title = "Woooo", onClick = {},
            trailingContent = { Text(text = "English") }
        )
        CustomListViewForSetting(
            leadingIcon = {},
            title = Strings.lanText,
            onClick = {},
            trailingContent = {
                Text(
                    text = "French",
                    style = MaterialTheme.typography.labelSmall
                )
            })

    }

}