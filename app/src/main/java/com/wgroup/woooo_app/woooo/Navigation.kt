package com.wgroup.woooo_app.woooo

import LoginView
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.auth.screen.SignUpView
import com.wgroup.woooo_app.woooo.feature.home.screen.HomePage
import com.wgroup.woooo_app.woooo.feature.home.viewmodel.HomeViewModel
import com.wgroup.woooo_app.woooo.feature.settings.views.SettingMainView


@Destination
@Composable
fun LoginScreen(navigator: DestinationsNavigator) {
    LoginView()
}

@Destination(start = true)
@Composable
fun HomeScreen(navigator: DestinationsNavigator) {
    HomePage(navigator = navigator)
}

@Destination
@Composable
fun SignUpScreen(navigator: DestinationsNavigator) {
    SignUpView()
}

@Destination
@Composable
fun SettingsScreen(navigator: DestinationsNavigator) {
    SettingMainView(navigator)
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