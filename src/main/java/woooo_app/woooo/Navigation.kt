package woooo_app.woooo

import LoginView
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.home.screen.DashboardView
import woooo_app.woooo.feature.home.screen.HomePage
import com.wgroup.woooo_app.woooo.feature.mining.views.MiningMainView
import com.wgroup.woooo_app.woooo.feature.settings.views.ApplicationMainView
import com.wgroup.woooo_app.woooo.feature.wallet.views.TransactionsView
import eu.siacs.conversations.ui.WelcomeActivity
import woooo_app.woooo.feature.auth.screen.ConfirmAccountScreen
import woooo_app.woooo.feature.auth.screen.ForgotPasswordView
import woooo_app.woooo.feature.auth.screen.SignUpView
import woooo_app.woooo.feature.auth.screen.VerifyOtpView
import woooo_app.woooo.feature.meeting.views.MeetingMainView
import woooo_app.woooo.feature.profile.views.UpdateProfileView
import woooo_app.woooo.feature.settings.views.SettingMainView
import woooo_app.woooo.feature.settings.views.account.AccountMainView
import woooo_app.woooo.feature.settings.views.account.PrivacyMainView
import woooo_app.woooo.feature.settings.views.account.SecurityMainView
import woooo_app.woooo.feature.settings.views.application.AudioVideoView
import woooo_app.woooo.feature.settings.views.application.DisplayView
import woooo_app.woooo.feature.settings.views.application.LanguageView
import woooo_app.woooo.feature.settings.views.application.SoundAndVibrationView
import woooo_app.woooo.feature.wallet.views.SendCurrencyView
import woooo_app.woooo.feature.wallet.views.WalletMainView

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
    SecurityMainView(navigator = navigator)
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

@RequiresApi(Build.VERSION_CODES.O)
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

@Destination
@Composable
fun ForgotPasswordScreen(navigator: DestinationsNavigator) {
    ForgotPasswordView(navigator)
}

@Destination
@Composable
fun VerifyOTPScreen(navigator: DestinationsNavigator) {
    VerifyOtpView(navigator)
}

@Destination
@Composable
fun ConfirmAccountMainScreen(navigator: DestinationsNavigator) {
    ConfirmAccountScreen(navigator)
}

@Destination
@Composable
fun MeetingMainViewScreen(navigator: DestinationsNavigator) {
    MeetingMainView(navigator)
}

fun goToWelcomeActivity(context: Context) {
    val intent = Intent(context, WelcomeActivity::class.java)
    intent.flags =
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION
    context.startActivity(intent)
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