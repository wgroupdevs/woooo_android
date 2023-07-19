package woooo_app.woooo.feature.auth.screen

import ShowLoader
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.JoinInner
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CountryPicker
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomIcon
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageSignUpView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.PasswordValidator
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.shared.components.view_models.PasswordValidatorViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R
import woooo_app.woooo.feature.auth.viewmodel.SignUpViewModel
import woooo_app.woooo.shared.components.view_models.CountryPickerViewModel
import woooo_app.woooo.utils.Dimension

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpView(navigator: DestinationsNavigator) {
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val countryPickerViewModel: CountryPickerViewModel = hiltViewModel()
    val context = LocalContext.current
    val customPasswordValidator: PasswordValidatorViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimension.dimen_10)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpacer(Dimension.dimen_20)
        // ap logo On top

        Image(
            painter = painterResource(id = R.drawable.woooo_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        VerticalSpacer()
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        {
            // first name
            TextLabel(label = Strings.firstNameText)
            VerticalSpacer()
            WooTextField(onValueChange = {
                signUpViewModel.setNameControllerValue(it)
                signUpViewModel.setNameErrorValue(false)
            },
                value = signUpViewModel.getNameController.value,
                isError = signUpViewModel.getNameError.value,
                supportingText = {
                    if (signUpViewModel.getNameError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.firstNameText,
                leadingIcon = {
                    CustomIcon(icon = Icons.Rounded.Person,modifier = Modifier)
                })
            VerticalSpacer(Dimension.dimen_5)
            // last name
            TextLabel(label = Strings.lastNameText)
            VerticalSpacer()
            WooTextField(onValueChange = {
                signUpViewModel.setLastNameControllerValue(it)
                signUpViewModel.setLastNameErrorValue(false)
            },
                value = signUpViewModel.getLastNameController.value,
                isError = signUpViewModel.getLastNameError.value,
                supportingText = {
                    if (signUpViewModel.getLastNameError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.lastNameText,
                leadingIcon = {
                    CustomIcon(icon = Icons.Rounded.Person,modifier = Modifier)
                })
            VerticalSpacer(Dimension.dimen_5)
            //phone number
            TextLabel(label = Strings.phoneNmbrText)
            VerticalSpacer()
            WooTextField(

//                readOnly = true,
                hint = Strings.enterNumberText,
                value = signUpViewModel.getPhoneNumberController.value,
                leadingIcon = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(onClick = {
//                            First read Json File and Then Enabled Country Picker Dialog
                            countryPickerViewModel.readJsonFileFromAssets(
                                context = context
                            )
                            signUpViewModel.setShowCountryPickerValue(true)
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
                    signUpViewModel.setPhoneNumberControllerValue(it)
                })
            VerticalSpacer(Dimension.dimen_5)
            //email
            TextLabel(label = Strings.emailText)
            VerticalSpacer()
            WooTextField(onValueChange = {
                signUpViewModel.setEmailControllerValue(it)
                signUpViewModel.setEmailErrorValue(false)
            },
                value = signUpViewModel.getEmailController.value,
                isError = signUpViewModel.getEmailError.value,
                supportingText = {
                    if (signUpViewModel.getEmailError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.emailText,
                leadingIcon = {
                    CustomIcon(icon = Icons.Rounded.Email,modifier = Modifier)
                })
            VerticalSpacer(Dimension.dimen_5)

            // Password Validator

            if (customPasswordValidator.getPasswordValidatorStateForSignUp.value) Box(
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            ) {
                PasswordValidator(0.6f)
            }
            //Password
            TextLabel(label = Strings.passwordText)
            VerticalSpacer()
            WooTextField(interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            customPasswordValidator.setPasswordValidatorStateForSignUp(true)
                        }
                    }
                }
            },
                onValueChange = {
                    customPasswordValidator.passwordValidator(it,true)
                    signUpViewModel.setPasswordControllerValue(it)
                    signUpViewModel.setPasswordErrorValue(false)

                },
                value = signUpViewModel.getPasswordController.value,
                isError = signUpViewModel.getPasswordError.value,
                supportingText = {
                    if (signUpViewModel.getPasswordError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.passwordText,
                leadingIcon = {
                    CustomIcon(icon = Icons.Rounded.Lock)
                })
            VerticalSpacer(Dimension.dimen_5)
            //Confirm Password
            TextLabel(label = Strings.confirmpasswordText)
            VerticalSpacer()
            WooTextField(onValueChange = {
                signUpViewModel.setConfirmPasswordControllerValue(it)
                signUpViewModel.setConfirmPasswordErrorValue(false)
            },
                value = signUpViewModel.getConfirmPasswordController.value,
                isError = signUpViewModel.getConfirmPasswordError.value,
                supportingText = {
                    if (signUpViewModel.getConfirmPasswordError.value) {
                        ErrorMessageSignUpView()
                    }
                },
                hint = Strings.confirmpasswordText,
                leadingIcon = {
                    CustomIcon(icon = Icons.Rounded.Lock)
                })
            VerticalSpacer(Dimension.dimen_5)
            //Referral Code
            TextLabel(label = Strings.referralCodeText)
            VerticalSpacer()
            WooTextField(hint = Strings.referralCodeText,leadingIcon = {
                CustomIcon(icon = Icons.Rounded.JoinInner)
            })

        }
        VerticalSpacer(Dimension.dimen_40)
        CustomButton(
            border = BorderStroke(1.dp,Color.White),
            onClick = {

                if (signUpViewModel.validateSignUpFields()) {
                    signUpViewModel.signUp(context)
                }
            },
            content = {
                Text(
                    text = Strings.signUpText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
        )


        VerticalSpacer(Dimension.dimen_40)
        CustomButton(
            border = BorderStroke(1.dp,Color.White),
            onClick = {

            },
            content = {
                Text(
                    text = Strings.alreadyAccount,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
        )
        // enable Loader when Api Hit
        if (signUpViewModel.signUpResponseState.value.isLoading) ShowLoader()
        // Enabled Country Country When User Click On PhoneNumber TextField

        if (signUpViewModel.setShowCountryPicker.value) CountryPicker(onDismissRequest = {
            signUpViewModel.setShowCountryPickerValue(
                false
            )
        },viewModel = signUpViewModel)

        // enabled success dialogue
        if (signUpViewModel.signUpResponseState.value.isLoading) {
            AlertDialog(
                onDismissRequest = {
                    signUpViewModel._signUpResponseState.value.isLoading = false
//                    signUpViewModel.signUpResponseState.value.isLoading = false
                },modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(WooColor.textBox)
            ) { Text(text = signUpViewModel.signUpResponseState.value.data.message.toString()) }
        }
    }
}
