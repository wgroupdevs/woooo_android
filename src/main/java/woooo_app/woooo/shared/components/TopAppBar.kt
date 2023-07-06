import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

import com.wgroup.woooo_app.woooo.theme.WooColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComposable(
   navigationIcon: @Composable () -> Unit
) {
    TopAppBar(
        colors = topAppBarColors(
        containerColor = WooColor.primary,

        ),

        title = {
            var text by remember { mutableStateOf("500") }

            BasicTextField(
                value = "hifdfdxz",
                onValueChange = {  },
                modifier = Modifier
                    .height(49.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall,
            ) { innerTextField ->
                OutlinedTextField(
                    modifier = Modifier.defaultMinSize(minHeight = 49.dp).padding(0.dp).fillMaxWidth(),
                    textStyle = MaterialTheme.typography.labelSmall.copy(color = WooColor.white),
                    onValueChange = {},
                    leadingIcon = {
                        Icon(imageVector = Icons.Outlined.Search, contentDescription = "")

                    },
                    placeholder = {
                        Text(text = "Search", style = MaterialTheme.typography.labelSmall)
                    },

                    value = "messages",
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = Color.Transparent,
                        focusedContainerColor = WooColor.textFieldBackGround,
                        unfocusedContainerColor = WooColor.textFieldBackGround,
                        disabledContainerColor = WooColor.textFieldBackGround,
                        errorContainerColor = WooColor.textFieldBackGround,
                        cursorColor = WooColor.primary,
                        focusedBorderColor = WooColor.white,
                        unfocusedBorderColor = WooColor.white,
                        disabledLabelColor = Color(0xffd8e6ff),
                        focusedLeadingIconColor = WooColor.white
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(30.dp),
                    singleLine = true,
                )
            }



//            CustomTextField(
//                leadingIcon = {
//                    Icon(
//                        Icons.Filled.Search,
//                        null,
//                    )
//                },
//                trailingIcon = null,
//                modifier = Modifier
//                    .background(
//                     WooColor.textFieldBackGround,
//                        RoundedCornerShape(percent = 50)
//                    )
//                    .padding(4.dp)
//                    .height(30.dp),
//                placeholderText = "Search"
//            )


        },
        navigationIcon = navigationIcon,
        actions = {

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Outlined.Notifications, contentDescription = "",
                    modifier = Modifier.size(size = 36.dp),
                    tint = WooColor.white
                )
            }
        },
    )
}


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String = "Placeholder",
) {
    var text by rememberSaveable { mutableStateOf("") }
    BasicTextField(modifier = modifier
        .background(
            WooColor.textFieldBackGround,
            MaterialTheme.shapes.small,
        )
        .fillMaxWidth(),
        value = text,
        onValueChange = {
            text = it
        },
        singleLine = true,
        cursorBrush = SolidColor( WooColor.textFieldBackGround,),
        textStyle = MaterialTheme.typography.labelSmall,

        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (text.isEmpty()) Text(
                        placeholderText,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}