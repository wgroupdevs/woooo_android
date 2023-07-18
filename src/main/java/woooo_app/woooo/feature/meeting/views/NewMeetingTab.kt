package com.wgroup.woooo_app.woooo.feature.meeting.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomIcon
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Composable
fun NewMeetingTabView() {
    Column(Modifier.fillMaxSize()) {
        VerticalSpacer()
        Box(
            modifier = Modifier
                .padding(10.dp)
                .border(
                    width = 1.dp,color = WooColor.white,shape = RoundedCornerShape(15.dp)
                )
                .height(250.dp)
                .fillMaxWidth()
                .background(color = WooColor.textBox,shape = RoundedCornerShape(15.dp))
                .alpha(0.9f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = Strings.camOnText,style = MaterialTheme.typography.bodyLarge)
                VerticalSpacer(Dimension.dimen_10)
                Text(text = Strings.micOnText)
                VerticalSpacer(Dimension.dimen_30)

                Row {
                    CustomIcon(icon = Icons.Outlined.Mic,modifier = Modifier.size(30.dp))
                    HorizontalSpacer(Dimension.dimen_30)
                    CustomIcon(icon = Icons.Outlined.Videocam,modifier = Modifier.size(30.dp))
                }
            }
        }
        VerticalSpacer()
        TextLabel(label = Strings.mtngNmeText)
        VerticalSpacer()
        WooTextField(onValueChange = {
//                    signUpViewModel.setNameControllerValue(it)
//                    signUpViewModel.setNameErrorValue(false)
        },value = "",
//                    isError = signUpViewModel.getNameError.value,
//                    supportingText = {
//                        if (signUpViewModel.getNameError.value) {
//                            ErrorMessageSignUpView()
//                        }
//                    },
            hint = Strings.mtngNmeText,leadingIcon = {
                CustomIcon(icon = Icons.Rounded.Edit,modifier = Modifier)
            })
        VerticalSpacer()
        TextLabel(label = Strings.mtngUrlText)
        VerticalSpacer()
        WooTextField(onValueChange = {
//                    signUpViewModel.setNameControllerValue(it)
//                    signUpViewModel.setNameErrorValue(false)
        },value = "",
//                    isError = signUpViewModel.getNameError.value,
//                    supportingText = {
//                        if (signUpViewModel.getNameError.value) {
//                            ErrorMessageSignUpView()
//                        }
//                    },
            hint = Strings.mtngUrlText,leadingIcon = {
                CustomIcon(icon = Icons.Rounded.Share,modifier = Modifier)
            })

        VerticalSpacer(Dimension.dimen_30)
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly) {
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {},
                content = {
                    Text(
                        text = Strings.strtMtngText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                },
            )

            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {},
                content = {
                    Text(
                        text = Strings.joinMtngText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                },
            )
        }
    }
}