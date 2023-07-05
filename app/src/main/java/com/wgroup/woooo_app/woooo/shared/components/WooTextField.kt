package com.wgroup.woooo_app.woooo.shared.components

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.wgroup.woooo_app.woooo.theme.Shapes
import com.wgroup.woooo_app.woooo.theme.WooColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WooTextField(
    modifier: Modifier = Modifier,
    value: String = "",
//    text: String,
    onValueChange: (String) -> Unit = {},
//    enabled: Boolean = true,
    readOnly: Boolean = false,
//    textStyle: TextStyle = LocalTextStyle.current,
//    label: @Composable (() -> Unit)? = null,
//    placeholder: @Composable (() -> Unit)? = null,

    hint: String = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    singleLine: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = Shapes.extraLarge,
//    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
    leadingIcon: @Composable (() -> Unit)? = null,
    obscusePass: Boolean = true,
    interactionSource: MutableInteractionSource = MutableInteractionSource()
) {
    OutlinedTextField(
        visualTransformation = if (obscusePass) VisualTransformation.None else PasswordVisualTransformation(),
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.labelMedium.copy(color = WooColor.white),
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = hint,
                style = MaterialTheme.typography.labelSmall,
            )
        },
        interactionSource = interactionSource,
        leadingIcon = leadingIcon,
        modifier = modifier.fillMaxWidth(),
        value = value,
        isError = isError,
        supportingText = supportingText,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = WooColor.primary,
            disabledLabelColor = Color(0xffd8e6ff),
            unfocusedBorderColor = Color.Black,
            focusedBorderColor = WooColor.white,
            containerColor = WooColor.textFieldBackGround,
            disabledTextColor = Color.Transparent,
            errorContainerColor = WooColor.textFieldBackGround

        ),
        shape = shape,
        singleLine = true,
        trailingIcon = trailingIcon,
    )
}