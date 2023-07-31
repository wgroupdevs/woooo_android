package woooo_app.woooo.feature.settings.views.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.settings.viewmodels.account.SecurityViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomListTile
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageSignUpView
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.shared.components.PasswordValidator
import woooo_app.woooo.shared.components.view_models.PasswordValidatorViewModel
import woooo_app.woooo.utils.Dimension

@Composable
fun SecurityMainView(
    navigator: DestinationsNavigator
) {
    val passwordValidatorViewModel: PasswordValidatorViewModel = hiltViewModel()
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
                  passwordValidatorViewModel.setValueOfDialoge(true)
              })
              CustomListTile(leadingIcon = {},title = Strings.fingerPrintText,onClick = {})
              CustomListTile(leadingIcon = {},title = Strings.encryptionText,onClick = {})

              if (passwordValidatorViewModel.getChangePasswordDialoge.value) {
                  PassChangeDialgue(
                      onDismissRequest = { passwordValidatorViewModel.setValueOfDialoge(false) },
                      passwordValidatorViewModel = passwordValidatorViewModel
                  )
              }
          }

      }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PassChangeDialgue(
    onDismissRequest: () -> Unit,passwordValidatorViewModel: PasswordValidatorViewModel
) {

    val securityViewModel: SecurityViewModel = hiltViewModel()
    val width = LocalConfiguration.current.screenWidthDp
    AlertDialog(
        onDismissRequest = onDismissRequest,modifier = Modifier
//            .padding(vertical = 50.dp)
            .fillMaxHeight(0.8f)
            .requiredWidthIn(width.dp,min(a = width.dp - 40.dp,b = width.dp - 40.dp))
            .clip(RoundedCornerShape(10.dp))
            .alpha(0.8f)
            .background(color = WooColor.textBox)
            .border(
                border = BorderStroke(1.dp,color = WooColor.white),shape = RoundedCornerShape(10.dp)
            )

    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp,vertical = 10.dp)) {

            //Current Password
            TextLabel(label = Strings.passwordText)
            VerticalSpacer()
            WooTextField(interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            passwordValidatorViewModel.setPasswordValidatorStateForSecurity(true)
                        }
                    }
                }
            },
                onValueChange = {
                    passwordValidatorViewModel.passwordValidator(it,true)
                    securityViewModel.setPasswordControllerValue(it)
                    securityViewModel.setPasswordErrorValue(false)

                },
                value = securityViewModel.getPasswordController.value,
                isError = securityViewModel.getPasswordError.value,
                supportingText = {
                    if (securityViewModel.getPasswordError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.passwordText,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,contentDescription = "",tint = Color.White
                    )
                })
//            open dialogue
            if (passwordValidatorViewModel.getPasswordValidatorStateForSecurity.value) {
                Box(
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
                ) {
                    PasswordValidator(height = 0.4f)
                }
            }
            VerticalSpacer(Dimension.dimen_5) //Password
            TextLabel(label = Strings.passwordText)
            VerticalSpacer()
            WooTextField(interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            passwordValidatorViewModel.setPasswordValidatorStateForSecurity(true)
                        }
                    }
                }
            },
                onValueChange = {
                    passwordValidatorViewModel.passwordValidator(it,true)
                    securityViewModel.setPasswordControllerValue(it)
                    securityViewModel.setPasswordErrorValue(false)

                },
                value = securityViewModel.getPasswordController.value,
                isError = securityViewModel.getPasswordError.value,
                supportingText = {
                    if (securityViewModel.getPasswordError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.passwordText,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,contentDescription = "",tint = Color.White
                    )
                })
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
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.confirmpasswordText,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,contentDescription = "",tint = Color.White
                    )
                })
            Box(modifier = Modifier.align(Alignment.End)) {
                CustomButton(
                    border = BorderStroke(1.dp,Color.White),
                    onClick = {},
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
        }
    }
}