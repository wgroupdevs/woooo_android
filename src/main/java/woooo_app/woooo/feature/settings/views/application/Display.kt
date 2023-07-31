package woooo_app.woooo.feature.settings.views.application

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
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.utils.Dimension

@Composable
fun DisplayView(navigator: DestinationsNavigator) {
   AppBackGround {
       Column(modifier = Modifier.padding(10.dp)) {
           TopBarForSetting(onBackPressed = {navigator.popBackStack()})
           Text(
               modifier = Modifier.padding(Dimension.dimen_10),
               text = Strings.displyText,
               style = MaterialTheme.typography.headlineMedium
           )


           CustomListTile(
               leadingIcon = {},
               title = Strings.wallpaperText, onClick = {},
               trailingContent = { Text(text = "Default") }
           )
           CustomListTile(
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

}