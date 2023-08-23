package woooo_app.woooo.feature.auth.screen

import ShowLoader
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageConfirmAccountView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R
import woooo_app.woooo.feature.auth.viewmodel.ConfirmAccountViewModel
import woooo_app.woooo.goToWelcomeActivity
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.shared.components.CustomIcon
import woooo_app.woooo.utils.Dimension

@Composable
fun ConfirmAccountScreen(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    val confirmAccountViewModel: ConfirmAccountViewModel = hiltViewModel()
    AppBackGround {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxSize()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth()
            ) {
                CustomIcon(
                    icon = Icons.Rounded.ArrowBack,modifier = Modifier.clickable(onClick = {
                        goToWelcomeActivity(context)
                    })
                )
                HorizontalSpacer()
                Text(text = Strings.confirmAccount,style = MaterialTheme.typography.bodyLarge)
            }
            VerticalSpacer(Dimension.dimen_20)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.woooo_logo),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp)
                )
                VerticalSpacer(Dimension.dimen_20)
                //OTP text field
                TextLabel(label = Strings.pleaseEnterOtp)
                VerticalSpacer(Dimension.dimen_15)
                WooTextField(
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        confirmAccountViewModel.setOTPValue(it)
                        confirmAccountViewModel.setOTPStateValue(false)
                    },
                    value = confirmAccountViewModel.getOTP.value,
                    isError = confirmAccountViewModel.getOTPError.value,
                    supportingText = {
                        if (confirmAccountViewModel.getOTPError.value) {
                            ErrorMessageConfirmAccountView()
                        }
                    },
                    hint = Strings.verifyCode
                )
                // resent OTP Button
                TextButton(modifier = Modifier.align(Alignment.End),
                    onClick = { confirmAccountViewModel.resentCodeForConfirmAccount(context) },
                    content = {
                        Text(
                            text = Strings.resentOTP,style = MaterialTheme.typography.bodyMedium
                        )
                    })
                // verify Button
                VerticalSpacer(Dimension.dimen_40)
                CustomButton(
                    border = BorderStroke(1.dp,Color.White),
                    onClick = {
                        if (confirmAccountViewModel.validateConfirmAccountFields()) {
                            confirmAccountViewModel.confirmAccount(context)
                        }
                    },
                    content = {
                        Text(
                            text = Strings.vrfyText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    },
                )
            }
            // enable Loader when Api Hit
            if (confirmAccountViewModel.confirmAccountState.value.isLoading.value) ShowLoader()
            // enable success dialog when verify OTp Api hit
            if (confirmAccountViewModel.confirmAccountState.value.isSucceed.value) {
                SuccessDialogAuth(title = Strings.regPass,
                    message = confirmAccountViewModel.confirmAccountState.value.message,
                    onClick = {
                        clickOnSuccessDialogVerifyAccount(
                            context,confirmAccountViewModel = confirmAccountViewModel
                        )
                    })
            }
            // enable Loader when Api Hit
            if (confirmAccountViewModel.resentCodeState.value.isLoading.value) ShowLoader()
            // enable success dialog when reSent OTp Api hit
            if (confirmAccountViewModel.resentCodeState.value.isSucceed.value) {

                SuccessDialogAuth(
                    title = Strings.reSendSuccess,
                    message = confirmAccountViewModel.resentCodeState.value.message,
                    onClick = {
                        clickOnSuccessDialogReSentOTP(
                            confirmAccountViewModel = confirmAccountViewModel
                        )
                    },
                )
            }
        }
    }
}

private fun clickOnSuccessDialogVerifyAccount(
    context: Context,confirmAccountViewModel: ConfirmAccountViewModel
) {
    confirmAccountViewModel.confirmAccountState.value.apply {
        isSucceed.value = false
    }

    goToWelcomeActivity(context)
    confirmAccountViewModel.clearSignUpFields()


}

private fun clickOnSuccessDialogReSentOTP(
    confirmAccountViewModel: ConfirmAccountViewModel
) {
    confirmAccountViewModel.resentCodeState.value.apply {
        isSucceed.value = false
    }

}