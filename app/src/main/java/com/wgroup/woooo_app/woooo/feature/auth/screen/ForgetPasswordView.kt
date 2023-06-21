package com.wgroup.woooo_app.woooo.feature.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ButtonDefaults
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
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomSpacer
import com.wgroup.woooo_app.woooo.shared.components.CustomTextField
import com.wgroup.woooo_app.woooo.theme.CustomColorTheme
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun ForgetPassword() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomSpacer(30)
        // ap logo On top

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        CustomSpacer(30)
        Text(text = Strings.forgotTextNewPassView, style = MaterialTheme.typography.displayMedium)
        CustomSpacer(40)
        CustomTextField(placeholder = {
            Text(
                text = Strings.enterEmailText,
                style = MaterialTheme.typography.titleSmall,
            )
        }, textStyle = MaterialTheme.typography.displayMedium)
        CustomSpacer(30)
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            shape = MaterialTheme.shapes.large,
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
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CustomColorTheme.textBox)
        )
        CustomSpacer(30)

        Text(text = Strings.emailNotFoundDes, style = MaterialTheme.typography.displaySmall, textAlign = TextAlign.Center)

    }
}