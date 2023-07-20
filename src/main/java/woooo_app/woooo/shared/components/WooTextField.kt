package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    singleLine: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = Shapes.extraLarge,
//    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
    leadingIcon: @Composable (() -> Unit)? = null,
    obscusePass: Boolean = true,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    unfocusedColor: Color = Color.Black
) {
    OutlinedTextField(
        keyboardOptions = keyboardOptions,
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
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedContainerColor = WooColor.textFieldBackGround,
            unfocusedContainerColor = WooColor.textFieldBackGround,
            disabledContainerColor = WooColor.textFieldBackGround,
            errorContainerColor = WooColor.textFieldBackGround,
            cursorColor = WooColor.primary,
            focusedBorderColor = WooColor.white,
            unfocusedBorderColor = unfocusedColor,
            disabledLabelColor = Color(0xffd8e6ff),
        ),
        shape = shape,
        singleLine = true,
        trailingIcon = trailingIcon,
    )
}