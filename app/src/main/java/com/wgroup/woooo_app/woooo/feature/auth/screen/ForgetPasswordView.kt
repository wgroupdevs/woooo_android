package com.wgroup.woooo_app.woooo.feature.auth.screen

import ShowAlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.ForgotPasswordViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun ForgetPassword() {

    val forgotViewModel: ForgotPasswordViewModel = hiltViewModel()

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
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        VerticalSpacer(Dimension.dimen_30)
        // forgot text
        Text(text = Strings.forgotTextNewPassView, style = MaterialTheme.typography.bodyLarge)
        VerticalSpacer(Dimension.dimen_40)
        // enter email
        WooTextField(hint = Strings.enterEmailText)
        VerticalSpacer(Dimension.dimen_30)
        // recover button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {

                    forgotViewModel.setEnabledState(!forgotViewModel.enabledState.value)
            },
            content = {
                Text(
                    text = Strings.recoverText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = WooColor.textBox)
        )
        VerticalSpacer(Dimension.dimen_30)
        //Last text

        if (forgotViewModel.enabledState.value)
        Text(
            text = Strings.emailNotFoundDes,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        if (forgotViewModel.enabledState.value)
          ShowAlertDialog(onClick ={
              forgotViewModel.setEnabledState(!forgotViewModel.enabledState.value)

          } )

    }
}