package com.wgroup.woooo_app.woooo.feature.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
fun SignUpView() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CustomSpacer(30)
        // ap logo On top

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        CustomSpacer(10)
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)
            CustomSpacer(10)
            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)
            CustomSpacer(10)

            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)
            CustomSpacer(10)

            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)
            CustomSpacer(10)

            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)
            CustomSpacer(10)

            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)
            CustomSpacer(10)

            Text(text = Strings.firstNameText, style = MaterialTheme.typography.titleMedium)
            CustomSpacer(10)
            CustomTextField(placeholder = {
                Text(
                    text = Strings.firstNameText,
                    style = MaterialTheme.typography.titleSmall,
                )
            }, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            }, textStyle = MaterialTheme.typography.displayMedium)


        }
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = Strings.dontHaveAcntText,
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
    }


}