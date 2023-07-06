import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginViewModelWithEmail
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginWithPhoneViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomDivider
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageForLoginWithEmail
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageLoginWithPhone
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
//import com.wgroup.woooo_app.woooo.shared.components.view_models.DateTimerPickerViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R


@Preview
@Composable
fun LoginView() {
    val withEmail by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),

        ) {

        if (withEmail) {
            LoginWithEmail()
        } else {
            LoginWithPhoneNumber()
        }
    }
}


@Composable
fun LoginWithPhoneNumber() {

    val loginWithEmailViewModel: LoginViewModelWithEmail = hiltViewModel()
//    val dateTimeViewModel: DateTimerPickerViewModel = hiltViewModel()
    val loginWithPhoneViewModel: LoginWithPhoneViewModel = hiltViewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f,true)
        ) {
            VerticalSpacer(Dimension.dimen_15)
            // ap logo On top

            Image(
                painter = painterResource(id = R.drawable.woooo_logo),
                contentDescription = "",
                modifier = Modifier.size(200.dp)
            )
            VerticalSpacer(Dimension.dimen_25)

            // Country picker
            WooTextField(
                hint = "Japan",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "",
                        tint = Color.White
                    )
                },
                onValueChange = {
                    loginWithPhoneViewModel.setCountryText(it)
                    loginWithPhoneViewModel.setPhoneError(false)
                },
                value = loginWithPhoneViewModel.getCountryText.value,
                isError = loginWithPhoneViewModel.getCountryError.value,
                supportingText = {
                    if (loginWithPhoneViewModel.getCountryError.value) {
                        ErrorMessageLoginWithPhone()
                    }
                },
            )
            VerticalSpacer(Dimension.dimen_15)
            // Phone Number
            WooTextField(
                hint = Strings.enterNumberText,
                leadingIcon = {

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(Dimension.dimen_50)
                    ) {
                        HorizontalSpacer()
                        Text(
                            text = "+81",
                            style = MaterialTheme.typography.titleSmall,
                        )
                        HorizontalSpacer()
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(WooColor.hintText)
                                .padding(10.dp)

                        )
                        HorizontalSpacer(Dimension.dimen_5)

                    }
                },
                onValueChange = {
                    loginWithPhoneViewModel.setPhoneText(it)
                    loginWithPhoneViewModel.setPhoneError(false)
                },
                value = loginWithPhoneViewModel.getPhoneText.value,
                isError = loginWithPhoneViewModel.getPhoneError.value,
                supportingText = {
                    if (loginWithPhoneViewModel.getPhoneError.value) {
                        ErrorMessageForLoginWithEmail()
                    }
                },
            )
            VerticalSpacer(Dimension.dimen_15)
            WooTextField(
                onValueChange = {
                    loginWithEmailViewModel.setPasswordControllerValue(it)
                    loginWithEmailViewModel.setErrorValueForPassword(false)
                },
                value = loginWithEmailViewModel.getPasswordController.value,
                isError = loginWithEmailViewModel.getErrorPasswordController.value,
                supportingText = {
                    if (loginWithEmailViewModel.getErrorPasswordController.value) {
                        ErrorMessageForLoginWithEmail()
                    }
                },
                hint = Strings.enterPasswordText,
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.VisibilityOff,

                        contentDescription = "",tint = Color.White,modifier = Modifier.clickable {
                            loginWithEmailViewModel.setEyeValueForPassword(!loginWithEmailViewModel.getEyeForPassword.value)
                        })

                },
                obscusePass = loginWithEmailViewModel.getEyeForPassword.value
            )
            VerticalSpacer(Dimension.dimen_30)
            // login button
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {
                    loginWithPhoneViewModel.validateEmailWithPhoneFields()
                },
                content = {
                    Text(
                        text = Strings.login,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )
            VerticalSpacer(Dimension.dimen_15)       // divider
            CustomDivider()
            VerticalSpacer(Dimension.dimen_15)       //  Login With Phone Button
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {
             //       dateTimeViewModel.setDatePickerShowValue(value = UseCaseState())
                },
                content = {
                    Text(
                        text = Strings.LogWithPhoneText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )

            // forgot text
            TextButton(onClick = { }) {
                Text(text = Strings.forgotText,style = MaterialTheme.typography.displaySmall)
            }
            Spacer(modifier = Modifier.weight(1f))

            //  last Button
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {
//                    print("${dateTimeViewModel.getDatePickerShow.show()}" + "uhwslckalksnciodc")
                },
                content = {
                    Text(
                        text = Strings.dontHaveAcntText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )

        }
    }
}

@Composable
fun LoginWithEmail() {
    val loginWithEmailViewModel: LoginViewModelWithEmail = hiltViewModel()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f,true)
        ) {

            VerticalSpacer(Dimension.dimen_15)
            // ap logo On top

            Image(
                painter = painterResource(id = R.drawable.woooo_logo),
                contentDescription = "",
                modifier = Modifier.size(200.dp)
            )
            VerticalSpacer(Dimension.dimen_15)
            // email
            WooTextField(
                onValueChange = {
                    loginWithEmailViewModel.setEmailControllerValue(it)
                    loginWithEmailViewModel.setErrorValueForEmail(false)
                },
                value = loginWithEmailViewModel.getEmailController.value,
                isError = loginWithEmailViewModel.getErrorEmailController.value,
                supportingText = {
                    if (loginWithEmailViewModel.getErrorEmailController.value) {
                        ErrorMessageForLoginWithEmail()
                    }
                },
                hint = Strings.enterEmailText
            )
            VerticalSpacer(Dimension.dimen_15)
            // password
            WooTextField(
                onValueChange = {
                    loginWithEmailViewModel.setPasswordControllerValue(it)
                    loginWithEmailViewModel.setErrorValueForPassword(false)
                },
                value = loginWithEmailViewModel.getPasswordController.value,
                isError = loginWithEmailViewModel.getErrorPasswordController.value,
                supportingText = {
                    if (loginWithEmailViewModel.getErrorPasswordController.value) {
                        ErrorMessageForLoginWithEmail()
                    }
                },
                hint = Strings.enterPasswordText,
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.VisibilityOff,

                        contentDescription = "",tint = Color.White,modifier = Modifier.clickable {
                            loginWithEmailViewModel.setEyeValueForPassword(!loginWithEmailViewModel.getEyeForPassword.value)
                        })

                },
                obscusePass = loginWithEmailViewModel.getEyeForPassword.value
            )
            // forgot text
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(start = Dimension.dimen_5)
            ) {
                TextButton(

                    onClick = { },contentPadding = PaddingValues(0.dp)

                ) {
                    Text(
                        text = Strings.forgotText,
                        style = MaterialTheme.typography.labelSmall.copy(color = WooColor.white)
                    )
                }
            }

            VerticalSpacer(Dimension.dimen_10)
            // login button
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {

                    if (loginWithEmailViewModel.validateEmailPass()) {
                        loginWithEmailViewModel.login()
                    }

                },
                content = {
                    Text(
                        text = Strings.login,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                },

                )
            // divider
            VerticalSpacer(Dimension.dimen_15)        // divider
            CustomDivider()
            VerticalSpacer(Dimension.dimen_15)       //  Login With Phone Button
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {},
                content = {
                    Text(
                        text = Strings.LogWithPhoneText,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                },
            )
            VerticalSpacer(Dimension.dimen_130)       //  Login With Phone Button

        }
        TextButton(
            onClick = {
//                datePickerDialog.show()
            },contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = Strings.dontHaveAcntText,style = MaterialTheme.typography.labelLarge)
        }
    }


}