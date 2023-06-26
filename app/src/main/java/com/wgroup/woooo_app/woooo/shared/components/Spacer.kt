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
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.ForgotViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginViewModelWithEmail
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginWithPhoneViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.SignUpViewModel
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.VerifyOtpViewModel
import com.wgroup.woooo_app.woooo.utils.Dimension

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
    val loginWithEmailViewModel: LoginViewModelWithEmail = hiltViewModel()
    Text(
        text = loginWithEmailViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ErrorMessageForgetPasswordView() {
    val forgotViewModel: ForgotViewModel = hiltViewModel()
    Text(
        text = forgotViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
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
        text = loginWithPhoneViewModel.getErrorText.value,style = MaterialTheme.typography.labelSmall
    )
}