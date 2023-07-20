package woooo_app.woooo.feature.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomIcon
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R
import woooo_app.woooo.feature.auth.viewmodel.ConfirmAccountViewModel
import woooo_app.woooo.utils.Dimension

@Composable
fun ConfirmAccountScreen() {
    val confirmAccountViewModel : ConfirmAccountViewModel = hiltViewModel()
    Column(
        Modifier
            .padding(10.dp)
            .fillMaxSize()
    ) {

        CustomIcon(icon = Icons.Rounded.ArrowBack)
        VerticalSpacer()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.woooo_logo),
                contentDescription = "",
                modifier = Modifier.size(200.dp)
            )
            VerticalSpacer(Dimension.dimen_20)
            //OTP
            TextLabel(label = Strings.pleaseEnterOtp)
            VerticalSpacer(Dimension.dimen_15)
            WooTextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    confirmAccountViewModel.setOTPValue(it)
//                    confirmAccountViewModel.setOtpError(false)
                },
                value = confirmAccountViewModel.getOTP.value,
//                isError = verifyOtpViewModel.getOtpError.value,
                supportingText = {
//                    if (verifyOtpViewModel.getOtpError.value) {
//                        ErrorMessageVerifyOtp()
//                    }
                },hint = Strings.verifyCode
            )
            VerticalSpacer(Dimension.dimen_40)
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {

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
    }
}