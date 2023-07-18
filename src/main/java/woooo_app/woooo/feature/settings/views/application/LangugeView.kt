package com.wgroup.woooo_app.woooo.feature.settings.views.application

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
fun LanguageView(navigator: DestinationsNavigator) {

    Column() {
        TopBarForSetting(onBackPressed = {navigator.popBackStack()})

    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            modifier = Modifier.padding(Dimension.dimen_10),
            text = Strings.lanText,
            style = MaterialTheme.typography.headlineMedium
        )


        CustomListTile(
            leadingIcon = {},
            title = "Woooo", onClick = {},
            trailingContent = { Text(text = "English") }
        )
        CustomListTile(
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

}}