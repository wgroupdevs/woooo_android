package woooo_app.woooo.feature.home.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor
import eu.siacs.conversations.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.whispersystems.libsignal.logging.Log
import woooo_app.woooo.destinations.SettingsScreenDestination
import woooo_app.woooo.destinations.UpdateProfileMainScreenDestination
import woooo_app.woooo.feature.home.viewmodel.HomeViewModel
import woooo_app.woooo.feature.profile.viewmodels.UpdateProfileViewModel
import woooo_app.woooo.goToWelcomeActivity
import woooo_app.woooo.shared.components.ViewDivider
import woooo_app.woooo.shared.components.view_models.LocalDbViewModel
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.utils.Dimension
import woooo_app.woooo.utils.FIRST_NAME

@Composable
fun AppDrawer(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    homeViewModel: HomeViewModel = hiltViewModel(),
    userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel(),
    localDbViewModel: LocalDbViewModel = hiltViewModel(),
    navigateToSettings: () -> Unit = {},
    closeDrawer: () -> Unit = {},
) {

    val context = LocalContext.current

    val updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()
//    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()

    ModalDrawerSheet(
        modifier = modifier.border(
            border = BorderStroke(width = 0.5.dp,color = WooColor.white),
            shape = RoundedCornerShape(0.dp,15.dp,15.dp,0.dp)
        ),drawerContainerColor = Color.Transparent
    ) {
        DrawerHeader(modifier,navigator)
//        Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_padding)))
        Column(modifier = Modifier.padding(Dimension.dimen_10)) {

            ViewDivider()
            VerticalSpacer()
            CustomListTile(title = "Settings",leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = WooColor.white
                )
            },onClick = {
                Log.d("asdcasdcsadc","dcasdcasdcsadcs")
                fillUserInfo(updateProfileViewModel,userPreferencesViewModel)
                navigator.navigate(SettingsScreenDestination)
            })

            CustomListTile(title = "Invite friend",leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = "Invite friend",
                    tint = WooColor.white
                )
            },onClick = {
                Log.d("aascas","ascascasc")

                navigator.navigate(SettingsScreenDestination)
            })

            CustomListTile(title = "Help & Feedback",leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Feedback",
                    tint = WooColor.white
                )
            },onClick = {
                navigator.navigate(SettingsScreenDestination)
            })
            CustomListTile(title = "Share referral code",leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Referral",
                    tint = WooColor.white
                )
            },onClick = {
                navigator.navigate(SettingsScreenDestination)
            })
            CustomListTile(title = "Add invitation Code",leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Pin,
                    contentDescription = "invitation",
                    tint = WooColor.white
                )
            },onClick = {
                navigator.navigate(SettingsScreenDestination)
            })
            CustomListTile(title = "Logout",leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = "Logout",
                    tint = WooColor.white
                )
            },onClick = {
                runBlocking {

                    localDbViewModel.clearAllSessions()

                    userPreferencesViewModel.clearPreference()

                    goToWelcomeActivity(context)
                }
            })
        }

    }


}

@Composable
fun DrawerHeader(
    modifier: Modifier,navigator: DestinationsNavigator
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.drawable.woooo_logo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(Dimension.dimen_100)
                .clip(CircleShape)
        )



        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(
                text = FIRST_NAME,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
            Text(text = "Edit profile",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.clickable {
                    Log.d("sdcnasnasdoivc","djcnasnasjn")
                    navigator.navigate(
                        UpdateProfileMainScreenDestination
                    )
                })
        }

    }
}

fun fillUserInfo(
    updateProfileViewModel: UpdateProfileViewModel,
    userPreferencesViewModel: UserPreferencesViewModel
) {
    Log.d("Presswededed","aasasd")
updateProfileViewModel.viewModelScope.launch {
    updateProfileViewModel.profileImage.value = userPreferencesViewModel.getProfileImage()
        updateProfileViewModel.setAboutControllerValue(userPreferencesViewModel.getAbout())
        updateProfileViewModel.setNameControllerValue(userPreferencesViewModel.getFirstName())
        updateProfileViewModel.setLastNameControllerValue(userPreferencesViewModel.getLastName())
        updateProfileViewModel.getEmailController = userPreferencesViewModel.getLastName()
        updateProfileViewModel.getPhoneController = userPreferencesViewModel.getPhone()
        updateProfileViewModel.setDOBControllerValue(userPreferencesViewModel.getDOB())
        updateProfileViewModel.setPostalCodeControllerValue(userPreferencesViewModel.getPostalCode())
}
}