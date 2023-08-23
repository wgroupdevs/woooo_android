package woooo_app.woooo.feature.settings.views.account

import ShowLoader
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageSecurityView
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.utils.Strings
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.feature.settings.viewmodels.security.SecurityViewModel
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.shared.components.PasswordValidator
import woooo_app.woooo.shared.components.ShowAlertDialog
import woooo_app.woooo.shared.components.TextLabel
import woooo_app.woooo.shared.components.view_models.PasswordValidatorViewModel
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.utils.Dimension

@Composable
fun SecurityMainView(
    navigator: DestinationsNavigator
) {
    val securityViewModel: SecurityViewModel = hiltViewModel()
    val passwordValidatorViewModel: PasswordValidatorViewModel = hiltViewModel()
    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
    val openChangePasswordDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    AppBackGround {
        Column {
            TopBarForSetting(onBackPressed = { navigator.popBackStack() })

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    modifier = Modifier.padding(Dimension.dimen_10),
                    text = Strings.securityText,
                    style = MaterialTheme.typography.headlineMedium
                )
                CustomListTile(
                    leadingIcon = {},
                    title = Strings.setPasscodeLockText,
                    onClick = {},
                )

                CustomListTile(leadingIcon = {},title = Strings.remoteLogOutText,onClick = {})
                CustomListTile(leadingIcon = {},title = Strings.changePasswordText,onClick = {
                    openChangePasswordDialog.value = true
                                    })
                CustomListTile(leadingIcon = {},title = Strings.fingerPrintText,onClick = {})
                CustomListTile(leadingIcon = {},title = Strings.encryptionText,onClick = {})

                if (openChangePasswordDialog.value) {
                    ShowAlertDialog(content = {
                        Column(
                            modifier = Modifier
                                .padding(
                                    horizontal = 20.dp,vertical = 10.dp
                                )
                                .verticalScroll(rememberScrollState())
                        ) {

                            //New Password
                            TextLabel(label = Strings.newPswdText)
                            VerticalSpacer()
                            WooTextField(interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                                LaunchedEffect(interactionSource) {
                                    interactionSource.interactions.collect {
                                        if (it is PressInteraction.Release) {
                                            passwordValidatorViewModel.setPasswordValidatorStateForSecurity(
                                                true
                                            )
                                        }
                                    }
                                }
                            },
                                onValueChange = {
                                    passwordValidatorViewModel.passwordValidator(it,false)
                                    securityViewModel.setNewPasswordControllerValue(it)
                                    securityViewModel.setNewPasswordErrorValue(false)

                                },
                                value = securityViewModel.getNewPassword.value,
                                isError = securityViewModel.getNewPasswordError.value,
                                supportingText = {
                                    if (securityViewModel.getNewPasswordError.value) {
                                        ErrorMessageSecurityView()
                                    }
                                },
                                hint = "Enter New Password",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Lock,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                })
                            if (passwordValidatorViewModel.getPasswordValidatorStateForSecurity.value) {
                                PasswordValidator(height = 0.5f)
                            }

                            VerticalSpacer(Dimension.dimen_5)
                            //Confirm Password
                            TextLabel(label = Strings.confirmpasswordText)
                            VerticalSpacer()
                            WooTextField(onValueChange = {
                                securityViewModel.setConfirmPasswordControllerValue(it)
                                securityViewModel.setConfirmPasswordErrorValue(false)
                            },
                                value = securityViewModel.getConfirmPasswordController.value,
                                isError = securityViewModel.getConfirmPasswordError.value,
                                supportingText = {
                                    if (securityViewModel.getConfirmPasswordError.value) {
                                        ErrorMessageSecurityView()
                                    }
                                },
                                hint = Strings.confirmpasswordText,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Lock,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                })
                            // Current  Password
                            TextLabel(label = "Current Password")
                            VerticalSpacer()
                            WooTextField(onValueChange = {
                                securityViewModel.setCurrentPasswordControllerValue(it)
                                securityViewModel.setCurrentPasswordErrorValue(false)

                            },
                                value = securityViewModel.getCurrentPasswordController.value,
                                isError = securityViewModel.getCurrentPasswordError.value,
                                supportingText = {
                                    if (securityViewModel.getCurrentPasswordError.value) {
                                        ErrorMessageSecurityView()
                                    }
                                },
                                hint = "Current Password",
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Lock,
                                        contentDescription = "",
                                        tint = Color.White
                                    )
                                })
                            VerticalSpacer(Dimension.dimen_5)

                            Box(modifier = Modifier.align(Alignment.End)) {
                                CustomButton(
                                    border = BorderStroke(1.dp,Color.White),
                                    onClick = {
                                        if (securityViewModel.validateFields() && passwordValidatorViewModel.passwordValidator(
                                                securityViewModel.getNewPassword.value,false
                                            )
                                        ) {
                                            runBlocking {
                                                securityViewModel.changeNumber(
                                                    userPreferencesViewModel.getAccountUniqueId(),
                                                    context = context
                                                )

                                            }
                                        }
                                    },
                                    content = {
                                        Text(
                                            text = "Confirm",
                                            style = MaterialTheme.typography.bodyMedium,
                                            textAlign = TextAlign.Center,
                                            fontSize = 16.sp
                                        )
                                    },
                                )
                            }

                            if (securityViewModel.changePasswordState.value.isLoading.value)ShowLoader()
                            if(securityViewModel.changePasswordState.value.isSucceed.value)openChangePasswordDialog.value=false
                        }
                    },onDismissRequest = { openChangePasswordDialog.value = false })

                }
            }
        }
    }
}