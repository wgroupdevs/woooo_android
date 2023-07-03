package com.wgroup.woooo_app.woooo.feature.wallet.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.wgroup.woooo_app.woooo.feature.wallet.viewmodel.SendCurrencyViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageSendCurrencyView
import com.wgroup.woooo_app.woooo.shared.components.TopBarForSetting
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun SendCurrencyView() {
    val sendCurrencyViewModel: SendCurrencyViewModel = hiltViewModel()
    Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.padding(10.dp)) {
        TopBarForSetting {

        }
        VerticalSpacer(Dimension.dimen_20)
        Image(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape),
            painter = rememberAsyncImagePainter(model = "https://lh3.googleusercontent.com/a/AAcHTtctn9GPUdxTYkvu3P7QyCsQQhuZzxJwVXb6SNAiXe83M5w=s396-c-no"),
            contentDescription = ""
        )
        VerticalSpacer()
        Text(text = "0.0 BTC",fontSize = 13.sp)
        VerticalSpacer()

        Row(
            modifier = Modifier
                .fillMaxWidth(.7f)
                .align(Alignment.CenterHorizontally)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sendCurrencyViewModel.getButtonState.value) WooColor.selectedButtonColor
                    else WooColor.textFieldBackGround
                ),

                onClick = { sendCurrencyViewModel.setButtonStateValue(true) },
                modifier = Modifier
                    .weight(.5f)
                    .border(
                        BorderStroke(
                            // Change the width of the border when the button is touched
                            width = if (sendCurrencyViewModel.getButtonState.value) 2.dp else 1.dp,
                            color = Color.White,
                        ),shape = RoundedCornerShape(10.dp)

                    ),
                shape = RoundedCornerShape(10.dp),
            ) { Text(text = Strings.sndText) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!sendCurrencyViewModel.getButtonState.value) WooColor.selectedButtonColor
                    else WooColor.textFieldBackGround
                ),
                onClick = { sendCurrencyViewModel.setButtonStateValue(false) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(.5f)

                    .border(
                        BorderStroke(
                            // Change the width of the border when the button is touched
                            width = if (!sendCurrencyViewModel.getButtonState.value) 2.dp else 0.5.dp,
                            color = Color.White,
                        ),shape = RoundedCornerShape(10.dp)
                    )
            ) { Text(text = Strings.receiveText) }
        }
//        if (sendCurrencyViewModel.getButtonState.value) SendByAddress() else SendByScan()
        SendByScan()
    }


}

@Composable
fun SendByAddress() {
    val sendCurrencyViewModel: SendCurrencyViewModel = hiltViewModel()
    Column {
        VerticalSpacer(Dimension.dimen_30)
        WooTextField(onValueChange = {
            sendCurrencyViewModel.setAddressControllerValue(it)
            sendCurrencyViewModel.setAddressErrorStateValue(false)
        },
            value = sendCurrencyViewModel.getAddressController.value,
            isError = sendCurrencyViewModel.getAddressErrorState.value,
            supportingText = {
                if (sendCurrencyViewModel.getAddressErrorState.value) {
                    ErrorMessageSendCurrencyView()
                }
            },
            hint = Strings.addressText,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.QrCode,contentDescription = "",tint = Color.White
                )
            })
        WooTextField(
            onValueChange = {
                sendCurrencyViewModel.setAmountControllerValue(it)
                sendCurrencyViewModel.setAmountErrorStateValue(false)
            },
            value = sendCurrencyViewModel.getAmountController.value,
            isError = sendCurrencyViewModel.getAmountErrorState.value,
            supportingText = {
                if (sendCurrencyViewModel.getAmountErrorState.value) {
                    ErrorMessageSendCurrencyView()
                }
            },
            hint = Strings.amountText,
        )
        Box(Modifier.align(Alignment.CenterHorizontally)) {
            CustomButton(onClick = { sendCurrencyViewModel.validateSendCurrencyFields() },
                content = {
                    Text(
                        text = Strings.sndText
                    )
                })

        }
    }
}

@Composable
fun SendByScan() {
    val URL = "https://www.example.com"
    Column {

    }
}

