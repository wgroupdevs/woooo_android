package com.wgroup.woooo_app.woooo

import LoginView
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import woooo_app.woooo.feature.auth.screen.SignUpView
import com.wgroup.woooo_app.woooo.feature.home.screen.DashboardView
import com.wgroup.woooo_app.woooo.feature.home.screen.HomePage
import com.wgroup.woooo_app.woooo.feature.mining.views.MiningMainView
import com.wgroup.woooo_app.woooo.feature.profile.views.UpdateProfileView
import com.wgroup.woooo_app.woooo.feature.settings.views.ApplicationMainView
import com.wgroup.woooo_app.woooo.feature.settings.views.SettingMainView
import com.wgroup.woooo_app.woooo.feature.settings.views.account.AccountMainView
import com.wgroup.woooo_app.woooo.feature.settings.views.account.PrivacyMainView
import com.wgroup.woooo_app.woooo.feature.settings.views.account.SecurityMainView
import com.wgroup.woooo_app.woooo.feature.settings.views.application.AudioVideoView
import com.wgroup.woooo_app.woooo.feature.settings.views.application.DisplayView
import com.wgroup.woooo_app.woooo.feature.settings.views.application.LanguageView
import com.wgroup.woooo_app.woooo.feature.settings.views.application.SoundAndVibrationView
import com.wgroup.woooo_app.woooo.feature.wallet.views.SendCurrencyView
import com.wgroup.woooo_app.woooo.feature.wallet.views.TransactionsView
import com.wgroup.woooo_app.woooo.feature.wallet.views.WalletMainView

@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {
    LoginView(navigator)
}

@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    HomePage(navigator = navigator)
}

@Destination
@Composable
fun SignUpScreen(navigator: DestinationsNavigator) {
    SignUpView(navigator)
}

// setting views navigation
@Destination
@Composable
fun SettingsScreen(navigator: DestinationsNavigator) {
    SettingMainView(navigator)
}

@Destination
@Composable
fun DashboardScreen(navigator: DestinationsNavigator) {
    DashboardView(navigator)
}

@Destination
@Composable
fun ApplicationMainScreen(navigator: DestinationsNavigator) {
    ApplicationMainView(navigator)
}

@Destination
@Composable
fun AccountMainScreen(navigator: DestinationsNavigator) {
    AccountMainView(navigator)
}

@Destination
@Composable
fun PrivacyMainScreen(navigator: DestinationsNavigator) {
    PrivacyMainView(navigator)
}

@Destination
@Composable
fun SecurityMainScreen(navigator: DestinationsNavigator) {
    SecurityMainView(navigator)
}

@Destination
@Composable
fun SoundAndVibrationMainScreen(navigator: DestinationsNavigator) {
    SoundAndVibrationView(navigator)
}

@Destination
@Composable
fun DisplayMainScreen(navigator: DestinationsNavigator) {
    DisplayView(navigator)
}

@Destination
@Composable
fun AudioVideoMainScreen(navigator: DestinationsNavigator) {
    AudioVideoView(navigator)
}

@Destination
@Composable
fun LanguageMainScreen(navigator: DestinationsNavigator) {
    LanguageView(navigator)
}

@Destination
@Composable
fun MiningMainScreen(navigator: DestinationsNavigator) {
    MiningMainView(navigator)
}

@Destination
@Composable
fun UpdateProfileMainScreen(navigator: DestinationsNavigator) {
    UpdateProfileView(navigator)
}

@Destination
@Composable
fun SendCurrencyMainScreen(navigator: DestinationsNavigator) {
    SendCurrencyView(navigator)
}

@Destination
@Composable
fun TransactionMainScreen(navigator: DestinationsNavigator) {
    TransactionsView(navigator)
}

@Destination
@Composable
fun WalletMainScreen(navigator: DestinationsNavigator) {
    WalletMainView(navigator)
}

//@Destination
//@Composable
//fun ProfileScreen(
//    navigator: DestinationsNavigator,
//    id: String,
//    fakeData: FakeData,
//    viewModel: ProfileScreenViewModel = hiltViewModel()
//) {
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Column {
//            Text(text = "Profile Screen ${id} ${fakeData.name}  ${fakeData.age} ${fakeData.isAdult}")
//            Spacer(modifier = Modifier.height(14.dp))
//            Button(onClick = {
//                // 1st variant of popBackStack()
////                navigator.popBackStack()
//                navigator.navigate(AccountScreenDestination)
//            }) {
//                Text(text = "Go Back")
//            }
//        }
//    }
//}
//
//@Destination
//@Composable
//fun AccountScreen(navigator: DestinationsNavigator) {
//    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//        Column {
//            Text(text = "Account Screen")
//            Spacer(modifier = Modifier.height(14.dp))
//            Button(onClick = {
//                // 2st variant of popBackStack()
//                navigator.popBackStack(
//                    HomeScreenDestination,
//                    inclusive = false,
//                    saveState = false
//                )
//                // inclusive true means it will pop HomeScreenDestination too
//                // inclusive false means it will not pop HomeScreenDestination
//            }) {
//                Text(text = "Go Back")
//            }
//        }
//    }
//}