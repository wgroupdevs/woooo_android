package com.wgroup.woooo_app.woooo.feature.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.JoinInner
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.SignUpViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageSignUpView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R

@Composable
fun SignUpView() {
    val signUpViewModel: SignUpViewModel = hiltViewModel()

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
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "",
                        tint = Color.White
                    )
                })
            VerticalSpacer()
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
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "",
                        tint = Color.White
                    )
                })
            VerticalSpacer()
            //phone number
            TextLabel(label = Strings.phoneNmbrText)
            VerticalSpacer()
            WooTextField(hint = Strings.enterNumberText,leadingIcon = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+81",
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
            })
            VerticalSpacer()
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
                    Icon(
                        imageVector = Icons.Rounded.Email,contentDescription = "",tint = Color.White
                    )
                })
            VerticalSpacer()
            //Password
            TextLabel(label = Strings.passwordText)
            VerticalSpacer()
            WooTextField(onValueChange = {
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
                    Icon(
                        imageVector = Icons.Rounded.Lock,contentDescription = "",tint = Color.White
                    )
                })
            VerticalSpacer()
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
                    Icon(
                        imageVector = Icons.Rounded.Lock,contentDescription = "",tint = Color.White
                    )
                })
            VerticalSpacer()
            //Referral Code
            TextLabel(label = Strings.referralCodeText)
            VerticalSpacer()
            WooTextField(hint = Strings.referralCodeText,leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.JoinInner,contentDescription = "",tint = Color.White
                )
            })

        }
        VerticalSpacer(Dimension.dimen_40)
        CustomButton(
            border = BorderStroke(1.dp,Color.White),
            onClick = {
                signUpViewModel.validateSignUpFields()
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
            onClick = {},
            content = {
                Text(
                    text = Strings.alreadyAccount,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
        )
    }


}
