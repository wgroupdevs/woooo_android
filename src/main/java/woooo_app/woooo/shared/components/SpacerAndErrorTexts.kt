package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import woooo_app.woooo.feature.auth.viewmodel.ForgotPasswordViewModel
import woooo_app.woooo.feature.auth.viewmodel.VerifyOtpViewModel
import com.wgroup.woooo_app.woooo.feature.profile.viewmodels.UpdateProfileViewModel
import com.wgroup.woooo_app.woooo.feature.wallet.viewmodel.SendCurrencyViewModel
import woooo_app.woooo.feature.auth.viewmodel.ConfirmAccountViewModel
import woooo_app.woooo.feature.auth.viewmodel.LoginWithEmailViewModel
import woooo_app.woooo.feature.auth.viewmodel.LoginWithPhoneViewModel
import woooo_app.woooo.feature.auth.viewmodel.SignUpViewModel
import woooo_app.woooo.utils.Dimension

@Composable
fun VerticalSpacer(height: Dp = Dimension.dimen_10) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(width: Dp = Dimension.dimen_10) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun ErrorMessageForLoginWithEmail() {
    val loginWithEmailViewModel: LoginWithEmailViewModel = hiltViewModel()
    Text(
        text = loginWithEmailViewModel.getErrorText.value,
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageForgetPasswordView() {
    val forgotPasswordViewModel: ForgotPasswordViewModel = hiltViewModel()
    Text(
        text = forgotPasswordViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageSignUpView() {
    val signUpViewModel: SignUpViewModel = hiltViewModel()
    Text(
        text = signUpViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageVerifyOtp() {
    val otpViewModel: VerifyOtpViewModel = hiltViewModel()
    Text(
        text = otpViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageLoginWithPhone() {
    val loginWithPhoneViewModel: LoginWithPhoneViewModel = hiltViewModel()
    Text(
        text = loginWithPhoneViewModel.getErrorText.value,
        style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageSendCurrencyView() {
    val sendCurrencyViewModel: SendCurrencyViewModel = hiltViewModel()
    Text(
        text = sendCurrencyViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageUpdateProfileView() {
    val updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()
    Text(
        text = updateProfileViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageConfirmAccountView() {
    val confirmAccountViewModel: ConfirmAccountViewModel = hiltViewModel()
    Text(
        text = confirmAccountViewModel.getOtpErrorText.value,
        style = MaterialTheme.typography.labelSmall
    )
}