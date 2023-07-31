package woooo_app.woooo.feature.settings.views.application

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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.utils.Dimension

@Composable
fun SoundAndVibrationView(navigator: DestinationsNavigator) {

   AppBackGround {
       Column(modifier = Modifier.padding(10.dp)) {
           TopBarForSetting(onBackPressed = {navigator.popBackStack()})
           Text(
               modifier = Modifier.padding(top = Dimension.dimen_5, start = Dimension.dimen_5),
               text = Strings.soundText,
               style = MaterialTheme.typography.headlineMedium
           )
           CustomListTile(
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
           CustomListTile(
               leadingIcon = {},
               title = Strings.notiTuneText,
               onClick = {},
               trailingContent = {
                   Text(text = Strings.onText)
               },
           )
           CustomListTile(
               leadingIcon = {},
               title = Strings.vibText,
               onClick = {},
               trailingContent = {
                   Text(text = Strings.onText)
               },
           )
           CustomListTile(
               leadingIcon = {},
               title = Strings.PopText,
               onClick = {},

               )
           Text(
               modifier = Modifier.padding(top = Dimension.dimen_5, start = Dimension.dimen_5),
               text = Strings.callText,
               style = MaterialTheme.typography.headlineMedium
           )
           CustomListTile(
               leadingIcon = {},
               title = Strings.ringText,
               onClick = {},
               trailingContent = {
                   Text(text = Strings.onText)
               },
           )
           CustomListTile(
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
           CustomListTile(
               leadingIcon = {},
               title = Strings.ringText,
               onClick = {},
               trailingContent = {
                   Text(text = Strings.onText)
               },
           )
           CustomListTile(
               leadingIcon = {},
               title = Strings.grpText,
               onClick = {},
               trailingContent = {
                   Text(text = Strings.onText)
               },
           )

       }

   }

}