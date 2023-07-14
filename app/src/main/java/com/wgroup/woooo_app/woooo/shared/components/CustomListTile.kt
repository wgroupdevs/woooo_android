package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension

@Composable
fun CustomListTile(
    headlineContent: @Composable (() -> Unit)? = null,
//    modifier: Modifier,
//    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
//    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(containerColor = WooColor.primary),
//    tonalElevation: Dp = ListItemDefaults.Elevation,
//    shadowElevation: Dp = ListItemDefaults.Elevation,

    leadingIcon: @Composable() (() -> Unit)?,
    title: String = "",
    onClick: () -> Unit,
    fontSize: Int = 16
) {
    Column {
        ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
//                .padding(5.dp),
            ,
            colors = colors,
            headlineContent = {
                Text(
                    text = title,style = MaterialTheme.typography.titleSmall
                )
            },
            leadingContent = leadingIcon,
            trailingContent = trailingContent,
            supportingContent = supportingContent
        )
        VerticalSpacer(Dimension.dimen_8)
        ViewDivider()
    }
}

@Composable

fun TopBarForSetting(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = RoundedCornerShape(30.dp),
    onBackPressed: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "",
                tint = WooColor.white,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable(onClick = onBackPressed)
            )
//            HorizontalSpacer(Dimension.dimen_5)
            OutlinedTextField(
                textStyle = MaterialTheme.typography.labelMedium.copy(color = WooColor.white),
                onValueChange = onValueChange,
                placeholder = {
                    Row {
                        Icon(imageVector = Icons.Outlined.Search,contentDescription = "")
                        HorizontalSpacer(Dimension.dimen_5)
                        Text(text = "Search",style = MaterialTheme.typography.labelSmall)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
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
                    unfocusedBorderColor = WooColor.white,
                    disabledLabelColor = Color(0xffd8e6ff),
//            focusedIndicatorColor = Color.Transparent,
//            unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = shape,
                singleLine = true,
                trailingIcon = trailingIcon,
            )
        }

    }

    VerticalSpacer()
    ViewDivider()
}