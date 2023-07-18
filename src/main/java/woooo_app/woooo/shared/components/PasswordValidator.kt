package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.woooo.shared.components.view_models.PasswordValidatorViewModel
import com.wgroup.woooo_app.woooo.theme.WooColor
import woooo_app.woooo.utils.Dimension

@Composable
fun PasswordValidator(height: Float) {
    val customPasswordValidator: PasswordValidatorViewModel = hiltViewModel()
    val width = LocalConfiguration.current.screenWidthDp
    Box(
        modifier = Modifier
            .fillMaxHeight(height)
            .requiredWidthIn(width.dp,min(a = width.dp - 40.dp,b = width.dp - 40.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(color = WooColor.textFieldBackGround)

    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp,vertical = 10.dp)) {
            Text(text = "Password Hint:",style = MaterialTheme.typography.labelLarge)
            Column(Modifier.padding(10.dp)) {
                // 8 characters
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (customPasswordValidator.eightChar.value) DoneIcon()
                    else CrossIcon()
                    HorizontalSpacer()
                    Text(text = "Minimum 8 Characters",style = MaterialTheme.typography.bodySmall)
                }
                VerticalSpacer(Dimension.dimen_5)
                // upper case
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (customPasswordValidator.upperCase.value) DoneIcon()
                    else CrossIcon()
                    HorizontalSpacer()
                    Text(
                        text = "Atleast 1 upper case letter (A-Z)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                VerticalSpacer(Dimension.dimen_5)
                // lower Case
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (customPasswordValidator.lowerCase.value) DoneIcon()
                    else CrossIcon()
                    HorizontalSpacer()
                    Text(
                        text = "Atleast 1 lower case letter (a-z)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                VerticalSpacer(Dimension.dimen_5)
                //  1 Number
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (customPasswordValidator.oneNumber.value) DoneIcon()
                    else CrossIcon()
                    HorizontalSpacer()
                    Text(
                        text = "Atleast 1 Number (0-9)",style = MaterialTheme.typography.bodySmall
                    )
                }
                // special Character

                VerticalSpacer(Dimension.dimen_5)
                //  1 Number
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (customPasswordValidator.specialChar.value) DoneIcon()
                    else CrossIcon()
                    HorizontalSpacer()
                    Text(
                        text = "Atleast 1 Special character (@,%,?)",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun DoneIcon() {
    Icon(imageVector = Icons.Rounded.Done,contentDescription = "",tint = Color(0xFF00FF00))
}

@Composable
fun CrossIcon() {
    Icon(imageVector = Icons.Rounded.Close,contentDescription = "",tint = Color.Red)
}
