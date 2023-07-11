package com.wgroup.woooo_app.woooo.feature.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageForgetPasswordView
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.ForgotViewModel
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R

@Composable
fun ForgetPassword() {

//    val loaderViewModel: LoaderViewModel = hiltViewModel()
    val forgotPassViewModel: ForgotViewModel = hiltViewModel()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        VerticalSpacer(Dimension.dimen_30)
        // ap logo On top

        Image(
            painter = painterResource(id = R.drawable.woooo_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        VerticalSpacer(Dimension.dimen_30)
        // forgot text
        Text(text = Strings.forgotTextNewPassView,style = MaterialTheme.typography.bodyLarge)
        VerticalSpacer(Dimension.dimen_40)
        // enter email
        WooTextField(
            onValueChange = {
                forgotPassViewModel.setEmailControllerValue(it)
                forgotPassViewModel.setErrorValueForEmail(false)
            },
            value = forgotPassViewModel.getEmailController.value,
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
            border = BorderStroke(1.dp,Color.White),
            onClick = {

                if (forgotPassViewModel.validateEmail()) {

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

//        if (loaderViewModel.getLoaderShow.value)
////            ShowLoader(onClick = {
////                loaderViewModel.setLoaderValue(!loaderViewModel.getLoaderShow.value)
////            })
//            CustomDialog("Hi Ervry ",{
//                loaderViewModel.setLoaderValue(!loaderViewModel.getLoaderShow.value)

//            },{ })
    }
}