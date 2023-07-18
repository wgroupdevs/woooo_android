package com.wgroup.woooo_app.woooo.feature.auth.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.VerifyOtpViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageVerifyOtp
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun VerifyOtpView() {
    val verifyOtpViewModel: VerifyOtpViewModel = hiltViewModel()
    Column(Modifier.padding(Dimension.dimen_10)) {
        Icon(
            imageVector = Icons.Rounded.ArrowBackIos,
            contentDescription = "",
        )
        VerticalSpacer(Dimension.dimen_30)
        Column(Modifier.padding(Dimension.dimen_10)) {
            val context = LocalContext.current

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
                },hint = Strings.verifyCode
            )
            VerticalSpacer(Dimension.dimen_15)
            //New Pass
            TextLabel(label = Strings.newPswdText)
            VerticalSpacer(Dimension.dimen_15)
            WooTextField(onValueChange = {
                verifyOtpViewModel.setNewPassText(it)
                verifyOtpViewModel.setNewPassError(false)
            },
                value = verifyOtpViewModel.getNewPassText.value,
                isError = verifyOtpViewModel.getNewPassError.value,
                supportingText = {
                    if (verifyOtpViewModel.getNewPassError.value) {
                        ErrorMessageVerifyOtp()
                    }
                },hint = Strings.newPswdText
            )
            VerticalSpacer(Dimension.dimen_15)
            //Confirm Pass
            TextLabel(label = Strings.confirmpasswordText)
            VerticalSpacer(Dimension.dimen_15)
            WooTextField(onValueChange = {
                verifyOtpViewModel.setConfirmPassText(it)
                verifyOtpViewModel.setConfirmPassError(false)
            },
                value = verifyOtpViewModel.getConfirmPassText.value,
                isError = verifyOtpViewModel.getConfirmPassError.value,
                supportingText = {
                    if (verifyOtpViewModel.getConfirmPassError.value) {
                        ErrorMessageVerifyOtp()
                    }
                },hint = Strings.reTypePswdText
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
                    Toast.makeText(context,"This is a toast",Toast.LENGTH_SHORT).show()
                })
            }
            VerticalSpacer(Dimension.dimen_50)
            Row(horizontalArrangement = Arrangement.Center,modifier = Modifier.fillMaxWidth()) {
                CustomButton(
                    border = BorderStroke(1.dp,Color.White),
                    onClick = { verifyOtpViewModel.validateOTPFields() },
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
        }
    }
}