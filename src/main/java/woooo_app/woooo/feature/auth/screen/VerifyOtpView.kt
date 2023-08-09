package woooo_app.woooo.feature.auth.screen

import ShowLoader
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageVerifyOtp
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.feature.auth.GV
import woooo_app.woooo.feature.auth.viewmodel.VerifyOtpViewModel
import woooo_app.woooo.goToWelcomeActivity
import woooo_app.woooo.shared.base.AppBackGround
import woooo_app.woooo.shared.components.CustomIcon
import woooo_app.woooo.utils.Dimension

@Composable
fun VerifyOtpView(navigator: DestinationsNavigator) {
    val context = LocalContext.current

    val verifyOtpViewModel: VerifyOtpViewModel = hiltViewModel()
    AppBackGround {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = WooColor.backgroundColor)
                .padding(Dimension.dimen_10),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth()
            ) {
                CustomIcon(
                    icon = Icons.Rounded.ArrowBack,modifier = Modifier.clickable(onClick = {
                        navigator.popBackStack()
                    })
                )
                HorizontalSpacer()
                Text(text = Strings.confirmAccount,style = MaterialTheme.typography.bodyLarge)
            }
            VerticalSpacer(Dimension.dimen_30)
            Column(Modifier.padding(Dimension.dimen_10)) {

                VerticalSpacer(Dimension.dimen_15)
                //OTP
                TextLabel(label = Strings.pleaseEnterOtp)
                VerticalSpacer(Dimension.dimen_15)
                WooTextField(
                    onValueChange = {
                        verifyOtpViewModel.setOtpText(it)
                        verifyOtpViewModel.setOtpError(false)
                    },
                    value = verifyOtpViewModel.getOtpCodeText.value,
                    isError = verifyOtpViewModel.getOtpError.value,
                    supportingText = {
                        if (verifyOtpViewModel.getOtpError.value) {
                            ErrorMessageVerifyOtp()
                        }
                    },
                    hint = Strings.verifyCode
                )
                VerticalSpacer(Dimension.dimen_15)
                //New Pass
                TextLabel(label = Strings.newPswdText)
                VerticalSpacer(Dimension.dimen_15)
                WooTextField(
                    onValueChange = {
                        verifyOtpViewModel.setNewPassText(it)
                        verifyOtpViewModel.setNewPassError(false)
                    },
                    value = verifyOtpViewModel.getNewPassText.value,
                    isError = verifyOtpViewModel.getNewPassError.value,
                    supportingText = {
                        if (verifyOtpViewModel.getNewPassError.value) {
                            ErrorMessageVerifyOtp()
                        }
                    },
                    hint = Strings.newPswdText
                )
                VerticalSpacer(Dimension.dimen_15)
                //Confirm Pass
                TextLabel(label = Strings.confirmpasswordText)
                VerticalSpacer(Dimension.dimen_15)
                WooTextField(
                    onValueChange = {
                        verifyOtpViewModel.setConfirmPassText(it)
                        verifyOtpViewModel.setConfirmPassError(false)
                    },
                    value = verifyOtpViewModel.getConfirmPassText.value,
                    isError = verifyOtpViewModel.getConfirmPassError.value,
                    supportingText = {
                        if (verifyOtpViewModel.getConfirmPassError.value) {
                            ErrorMessageVerifyOtp()
                        }
                    },
                    hint = Strings.reTypePswdText
                )
                VerticalSpacer(Dimension.dimen_20)
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(Dimension.dimen_10)
                        .fillMaxWidth()
                ) {
                    TextLabel(label = Strings.reTypePswdDes)
                    TextLabel(label = Strings.resentOTP,modifier = Modifier.clickable {
                        verifyOtpViewModel.resentCode(context)
                    })
                }
                VerticalSpacer(Dimension.dimen_50)
                Row(horizontalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth()) {
                    CustomButton(
                        border = BorderStroke(1.dp,Color.White),
                        onClick = {
                            if (verifyOtpViewModel.validateOTPFields()) {
                                verifyOtpViewModel.resetPassword(context)
                            }
                        },
                        content = {
                            Text(
                                text = Strings.resetText,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        },

                        )
                }

                // enable Loader when Api Hit
                if (verifyOtpViewModel.resentCodeState.value.isLoading.value) ShowLoader()
                // enable success dialog when reSent OTp Api hit
                if (verifyOtpViewModel.resentCodeState.value.isSucceed.value) {
                    SuccessDialogAuth(title = "",
                        message = verifyOtpViewModel.resentCodeState.value.message,
                        onClick = {
                            clickOnSuccessResentOtp(
                                verifyOtpViewModel = verifyOtpViewModel
                            )
                        })
                }

                // enable Loader when Api Hit
                if (verifyOtpViewModel.resetPasswordState.value.isLoading.value) ShowLoader()
                // enable success dialog when reSent OTp Api hit
                if (verifyOtpViewModel.resetPasswordState.value.isSucceed.value) {

                    SuccessDialogAuth(title = Strings.resetSuccess,
                        message = verifyOtpViewModel.resetPasswordState.value.message,
                        onClick = {
                            clickOnSuccessResetPass(
                                context,
                                verifyOtpViewModel = verifyOtpViewModel
                            )
                        })
                }
            }
        }
    }
}

fun clickOnSuccessResentOtp(verifyOtpViewModel: VerifyOtpViewModel) {
    verifyOtpViewModel.resentCodeState.value.apply {
        isSucceed.value = false
    }
}

fun clickOnSuccessResetPass(
    context: Context,verifyOtpViewModel: VerifyOtpViewModel
) {
    verifyOtpViewModel.resetPasswordState.value.apply {
        isSucceed.value = false
    }
    GV.clearEmailField()

    goToWelcomeActivity(context)
}
