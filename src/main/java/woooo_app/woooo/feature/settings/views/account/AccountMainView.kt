package woooo_app.woooo.feature.settings.views.account

import ShowLoader
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.outlined.Backup
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CountryPicker
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageAccountMainView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.destinations.PrivacyMainScreenDestination
import woooo_app.woooo.destinations.SecurityMainScreenDestination
import woooo_app.woooo.feature.settings.viewmodels.account.AccountViewModel
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.shared.components.CustomIcon
import woooo_app.woooo.shared.components.ShowAlertDialog
import woooo_app.woooo.shared.components.view_models.CountryPickerViewModel
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.utils.Dimension

@Composable
fun AccountMainView(navigator: DestinationsNavigator) {
    var openChangeNumberDialog = remember {
        mutableStateOf(false)
    }
    val accountViewModel: AccountViewModel = hiltViewModel()
    val countryPickerViewModel: CountryPickerViewModel = hiltViewModel()
    val context = LocalContext.current
    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()

    AppBackGround {
        Column {
            TopBarForSetting(onBackPressed = { navigator.popBackStack() })
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    modifier = Modifier.padding(Dimension.dimen_10),
                    text = Strings.accountText,
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomListTile(
                    leadingIcon = {
                        Icon(
                            tint = WooColor.white,

                            modifier = Modifier.size(
                                36.dp
                            ),
                            imageVector = Icons.Outlined.Lock,contentDescription = "",
                        )
                    },
                    title = Strings.privacyText,
                    onClick = {
                        navigator.navigate(
                            PrivacyMainScreenDestination
                        )
                    },
                )
                CustomListTile(leadingIcon = {
                    Icon(
                        tint = WooColor.white,modifier = Modifier.size(
                            36.dp
                        ),imageVector = Icons.Outlined.Security,contentDescription = ""
                    )
                },title = Strings.securityText,onClick = {
                    navigator.navigate(
                        SecurityMainScreenDestination
                    )
                })
                CustomListTile(leadingIcon = {
                    Icon(
                        tint = WooColor.white,

                        modifier = Modifier.size(
                            36.dp
                        ),imageVector = Icons.Default.Compare,contentDescription = ""
                    )
                },title = Strings.changeNumberText,onClick = {
                    openChangeNumberDialog.value = true
                })
                CustomListTile(leadingIcon = {
                    Icon(
                        tint = WooColor.white,

                        modifier = Modifier.size(
                            36.dp
                        ),imageVector = Icons.Outlined.Delete,contentDescription = ""
                    )
                },title = Strings.deleteAccountText,onClick = {
                    runBlocking {
                        accountViewModel.deleteAccount(
                            userPreferencesViewModel.getAccountUniqueId(),context
                        )
                    }
                })
                CustomListTile(leadingIcon = {
                    Icon(
                        tint = WooColor.white,

                        modifier = Modifier.size(
                            36.dp
                        ),imageVector = Icons.Outlined.Backup,contentDescription = ""
                    )
                },title = Strings.backupText,onClick = {})
                // loader when delete account api hit
                if (accountViewModel.deleteAccountState.value.isLoading.value) ShowLoader()
                // change password dialog
                if (openChangeNumberDialog.value) ShowAlertDialog(content = {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.6f)
                            .padding(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = Strings.changePhoneText,fontSize = 15.sp)
                            CustomIcon(
                                icon = Icons.Outlined.Clear,
                                modifier = Modifier.clickable(onClick = {
                                    openChangeNumberDialog.value = false
                                })
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxSize()
                        ) {
                            //phone number
                            VerticalSpacer()
                            Box(modifier = Modifier.height(55.dp)) {
                                WooTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    hint = Strings.enterNumberText,
                                    value = accountViewModel.getPhoneNumberController.value,
                                    isError = accountViewModel.getPhoneError.value,
                                    supportingText = {
                                        if (accountViewModel.getPhoneError.value) {
                                            ErrorMessageAccountMainView()
                                        }
                                    },
                                    leadingIcon = {
                                        Row(
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.clickable(onClick = {
                                                //  First read Json File and Then Enabled Country Picker Dialog
                                                countryPickerViewModel.readJsonFileFromAssets(
                                                    context = context
                                                )
                                                accountViewModel.setShowCountryPickerValue(true)
                                            })
                                        ) {
                                            Text(
                                                text = countryPickerViewModel.getSelectedCountryDialCode.value,
                                                style = MaterialTheme.typography.labelMedium,
                                            )
                                            HorizontalSpacer()
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .width(1.dp)
                                                    .background(WooColor.hintText)
                                                    .padding(5.dp)
                                            )
                                        }
                                    },
                                    onValueChange = {
                                        if (it.length <= 12) {
                                            accountViewModel.setPhoneError(false)
                                            accountViewModel.setPhoneNumberControllerValue(it)
                                        }
                                    })
                            }
                            VerticalSpacer()
                            WooTextField(onValueChange = {
                                accountViewModel.setPasswordControllerValue(it)
                                accountViewModel.setPasswordErrorValue(false)

                            },
                                value = accountViewModel.getPasswordController.value,
                                isError = accountViewModel.getPasswordError.value,
                                supportingText = {
                                    if (accountViewModel.getPasswordError.value) {
                                        ErrorMessageAccountMainView()
                                    }
                                },
                                hint = "Verify Password",
                                leadingIcon = {
                                    CustomIcon(icon = Icons.Rounded.Lock)
                                })

                            VerticalSpacer()
                            VerticalSpacer()
                            CustomButton(
                                border = BorderStroke(1.dp,Color.White),
                                onClick = {
                                    runBlocking {
                                        accountViewModel.getNumberWithCode.value =
                                            countryPickerViewModel.getSelectedCountryDialCode.value + accountViewModel.getPhoneNumberController.value
                                        if (accountViewModel.validateFields()) accountViewModel.chaneNumber(
                                            userPreferencesViewModel.getAccountUniqueId(),context
                                        )
                                    }
                                },
                                content = {
                                    Text(
                                        text = "Change Number",
                                        textAlign = TextAlign.Center,
                                        fontSize = 14.sp
                                    )
                                },
                            )
                        }
                    }

                },onDismissRequest = {})
                // loader when change number api hit
                if (accountViewModel.changeNumberState.value.isLoading.value) {
                    ShowLoader()
                }
                // on success of number change set new number to preferences
                if (accountViewModel.changeNumberState.value.isSucceed.value) {
                    runBlocking {
                        userPreferencesViewModel.setPhone(accountViewModel.getNumberWithCode.value)
                    }
                    openChangeNumberDialog.value = false
                }
            }
            // country picker when changing number
            if (accountViewModel.setShowCountryPicker.value) CountryPicker(onDismissRequest = {
                accountViewModel.setShowCountryPickerValue(
                    false
                )
            },viewModel = accountViewModel)
        }
    }
}


