package woooo_app.woooo.feature.auth.screen

import ShowLoader
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageForgetPasswordView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R
import woooo_app.MainActivity
import woooo_app.woooo.destinations.VerifyOTPScreenDestination
import woooo_app.woooo.feature.auth.EmailForAuthModule
import woooo_app.woooo.feature.auth.viewmodel.ForgotPasswordViewModel
import woooo_app.woooo.shared.components.CustomIcon
import woooo_app.woooo.utils.Dimension

@Composable
fun ForgotPasswordView(navigator: DestinationsNavigator) {

    val context = LocalContext.current as MainActivity
    val forgotPassViewModel: ForgotPasswordViewModel = hiltViewModel()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(onClick = { context.finish() }) {
                CustomIcon(
                    icon = Icons.Rounded.ArrowBack, modifier = Modifier
                        .size(26.dp)
                )
            }



            HorizontalSpacer(Dimension.dimen_5)
            Text(text = Strings.forgotTextNewPassView, style = MaterialTheme.typography.bodyLarge)
        }
        VerticalSpacer(Dimension.dimen_30)
        // ap logo On top

        Image(
            painter = painterResource(id = R.drawable.woooo_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        VerticalSpacer(Dimension.dimen_30)
        // forgot text
        Text(text = Strings.forgotTextNewPassView, style = MaterialTheme.typography.bodyLarge)
        VerticalSpacer(Dimension.dimen_40)
        // enter email
        WooTextField(
            onValueChange = {
                EmailForAuthModule.setEmailValue(it)
                forgotPassViewModel.setErrorValueForEmail(false)
            },
            value = EmailForAuthModule.getEmail.value,
            isError = forgotPassViewModel.getErrorEmailController.value,
            supportingText = {
                if (forgotPassViewModel.getErrorEmailController.value) {
                    ErrorMessageForgetPasswordView()
                }
            },
            hint = Strings.enterEmailText
        )
        VerticalSpacer(Dimension.dimen_30)
        // recover button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {

                if (forgotPassViewModel.validateEmail()) {
                    forgotPassViewModel.forgotPassword(context)
                }
            },
            content = {
                Text(
                    text = Strings.recoverText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
        )
        VerticalSpacer(Dimension.dimen_30)
        //Last text
        Text(
            text = Strings.emailNotFoundDes,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        // enable Loader when Api Hit
        if (forgotPassViewModel.forgotPasswordState.value.isLoading.value) ShowLoader()
        // enabled success dialogue when api hit successfully
        if (forgotPassViewModel.forgotPasswordState.value.isSucceed.value) {

            SuccessDialogAuth(
                title = Strings.forgotTextNewPassView,
                message = forgotPassViewModel.forgotPasswordState.value.message,
                onClick = {
                    clickOnSuccessDialogForgotPassword(
                        forgotPasswordViewModel = forgotPassViewModel, navigator = navigator
                    )
                })
        }
    }
}

private fun clickOnSuccessDialogForgotPassword(
    forgotPasswordViewModel: ForgotPasswordViewModel, navigator: DestinationsNavigator
) {
    forgotPasswordViewModel.forgotPasswordState.value.apply {
        isSucceed.value = false
    }
    navigator.navigate(VerifyOTPScreenDestination)

}