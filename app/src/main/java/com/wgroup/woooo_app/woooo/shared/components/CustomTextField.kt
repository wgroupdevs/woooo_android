package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.CustomColorTheme
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
        textStyle = textStyle, placeholder = placeholder,
        leadingIcon = leadingIcon,
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .fillMaxWidth()
            .border(1.dp, Color.Black)
            .height(52.dp),
        value = textState,
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = CustomColorTheme.primary,
            disabledLabelColor = lightBlue,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            containerColor = CustomColorTheme.textFieldBackGround
        ),
        onValueChange = {
            if (it.length <= maxLength) textState = it
        },
        shape = shape,
        singleLine = true,
        trailingIcon = trailingIcon,
    )
}