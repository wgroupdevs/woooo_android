package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
//    value: String? = "",
//    text: String,
//    onValueChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    readOnly: Boolean = false,
//    textStyle: TextStyle = LocalTextStyle.current,
//    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
//    supportingText: @Composable (() -> Unit)? = null,
//    isError: Boolean = false,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    singleLine: Boolean = false,
//    maxLines: Int = Int.MAX_VALUE,
    shape: Shape = Shapes.extraLarge,
    textStyle: TextStyle,
//    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    var textState by remember { mutableStateOf("") }
    val maxLength = 110
    val lightBlue = Color(0xffd8e6ff)


    TextField(
        textStyle =textStyle ,placeholder = placeholder,
        leadingIcon = leadingIcon,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        value = textState,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = WooColor.primary,
            disabledLabelColor = lightBlue,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = WooColor.textFieldBackGround,
        ),
        onValueChange = {
            if (it.length <= maxLength) textState = it
        },
        shape = shape,
        singleLine = true,
        trailingIcon = trailingIcon)
}