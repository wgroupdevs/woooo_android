package com.wgroup.woooo_app.woooo.feature.profile.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.rounded.AddLocation
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.feature.profile.viewmodels.UpdateProfileViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomDateTimePicker
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageUpdateProfileView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import woooo_app.woooo.shared.components.ViewDivider
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.shared.components.view_models.DateTimerPickerViewModel
import com.wgroup.woooo_app.woooo.theme.Shapes
import com.wgroup.woooo_app.woooo.theme.WooColor
import woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.R
import java.time.LocalDate

@Composable
fun UpdateProfileView(navigator: DestinationsNavigator) {
    val dateTimerPickerViewModel: DateTimerPickerViewModel = hiltViewModel()
    val updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()

    Column(
        Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = { navigator.popBackStack() })
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = "",
                tint = WooColor.white,
                modifier = Modifier.clickable(onClick = {})
            )
            HorizontalSpacer()
            Text(text = "Profile")
        }
        VerticalSpacer()
        ViewDivider()
        VerticalSpacer()
        BoxWithConstraints(
            Modifier.align(Alignment.CenterHorizontally)
        ) {
            Box(
                Modifier.clip(CircleShape)
            ) {
                Image(
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp),
                    painter = painterResource(R.drawable.woooo_logo),
                    contentDescription = "This is a circular image",
                    contentScale = ContentScale.FillBounds
                )
            }
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = "",
                tint = WooColor.white,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
            )
        }

        // about
        Text(
            text = Strings.abtText,
            style = MaterialTheme.typography.labelMedium.copy(color = WooColor.white)
        )
        VerticalSpacer(Dimension.dimen_5)
        Box(
            modifier = Modifier
                .height(150.dp)
                .background(WooColor.textFieldBackGround,shape = RoundedCornerShape(10.dp))
        ) {
            OutlinedTextField(

                textStyle = MaterialTheme.typography.labelMedium.copy(color = WooColor.white),
                onValueChange = {
                    if (it.length <= 300) updateProfileViewModel.setAboutControllerValue(it)
                    updateProfileViewModel.setAboutErrorValue(false)
                },
                value = updateProfileViewModel.getAboutController.value,
                placeholder = {
                    Text(
                        text = "Enter About",
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    cursorColor = WooColor.primary,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledLabelColor = Color.Transparent,
                ),
                shape = Shapes.extraLarge,
                maxLines = 5,
                isError = updateProfileViewModel.getAboutError.value,
                supportingText = {
                    if (updateProfileViewModel.getAboutError.value) {
                        ErrorMessageUpdateProfileView()
                    }
                },

                )
        }
        // first name
        TextLabel(label = Strings.firstNameText)
        VerticalSpacer()
        WooTextField(onValueChange = {
            updateProfileViewModel.setNameControllerValue(it)
            updateProfileViewModel.setNameErrorValue(false)
        },
            value = updateProfileViewModel.getNameController.value,
            isError = updateProfileViewModel.getNameError.value,
            supportingText = {
                if (updateProfileViewModel.getNameError.value) {
                    ErrorMessageUpdateProfileView()
                }
            },
            hint = Strings.firstNameText,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Person,contentDescription = "",tint = Color.White
                )
            })
        // last name
        TextLabel(label = Strings.lastNameText)
        VerticalSpacer()
        WooTextField(onValueChange = {
            updateProfileViewModel.setLastNameControllerValue(it)
            updateProfileViewModel.setLastNameErrorValue(false)
        },
            value = updateProfileViewModel.getLastNameController.value,
            isError = updateProfileViewModel.getLastNameError.value,
            supportingText = {
                if (updateProfileViewModel.getLastNameError.value) {
                    ErrorMessageUpdateProfileView()
                }
            },
            hint = Strings.lastNameText,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Person,contentDescription = "",tint = Color.White
                )
            })
        //email
        TextLabel(label = Strings.emailText)
        VerticalSpacer()
        WooTextField(
            onValueChange = {
//                updateProfileViewModel.setEmailControllerValue(it)
//                updateProfileViewModel.setEmailErrorValue(false)
            },
//            value = signUpViewModel.getEmailController.value,
//            isError = signUpViewModel.getEmailError.value,
//            supportingText = {
//                if (signUpViewModel.getEmailError.value) {
//                    ErrorMessageUpdateProfileView()
//                }
//            },
            hint = Strings.emailText,leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email,contentDescription = "",tint = Color.White
                )
            },readOnly = true
        )
        //phone number
        TextLabel(label = Strings.phoneNmbrText)
        VerticalSpacer()
        WooTextField(hint = Strings.enterNumberText,
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            // open date picker
                            dateTimerPickerViewModel.setDateDialogueValueForUpdateProfile(
                                true
                            )
                        }
                    }
                }
            },
            leadingIcon = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "+81",
                        style = MaterialTheme.typography.labelMedium,

                        )
                    HorizontalSpacer()
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(WooColor.hintText)
                            .padding(5.dp)
                    )
                }
            },
            readOnly = true
        )
        VerticalSpacer()
        // date of birth
        TextLabel(label = Strings.dobTex)
        VerticalSpacer()
        WooTextField(
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            // open date picker
                            dateTimerPickerViewModel.setDateDialogueValueForUpdateProfile(
                                true
                            )
                        }
                    }
                }
            },
            onValueChange = {
                updateProfileViewModel.setDOBControllerValue(it)
                updateProfileViewModel.setDOBErrorValue(false)
            },
            readOnly = true,
            value = dateTimerPickerViewModel.getDatePickerTextForUpdateProfile.value,
            isError = updateProfileViewModel.getDOBError.value,
            supportingText = {
                if (updateProfileViewModel.getDOBError.value) {
                    ErrorMessageUpdateProfileView()
                }
            },
            hint = "2010-05-15",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Cake,contentDescription = "",tint = Color.White
                )
            },

            )

        if (dateTimerPickerViewModel.getDateDialogueShowForUpdateProfile.value) CustomDateTimePicker(
            onDateChange = {
                val date = LocalDate.parse(it.toString())
                dateTimerPickerViewModel.setDateTextValueForUpdateProfile(date)
                dateTimerPickerViewModel.setDateDialogueValueForUpdateProfile(false)
            })
        // address
        TextLabel(label = Strings.addressText)
        VerticalSpacer()
        WooTextField(onValueChange = {
            updateProfileViewModel.setNameControllerValue(it)
            updateProfileViewModel.setNameErrorValue(false)
        },
            value = updateProfileViewModel.getAddressController.value,
            isError = updateProfileViewModel.getAddressError.value,
            supportingText = {
                if (updateProfileViewModel.getAddressError.value) {
                    ErrorMessageUpdateProfileView()
                }
            },
            hint = Strings.addressText,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.AddLocation,
                    contentDescription = "",
                    tint = Color.White
                )
            })
        // postal code
        TextLabel(label = Strings.pstlCodeText)
        VerticalSpacer()
        WooTextField(onValueChange = {
            updateProfileViewModel.setPostalCodeControllerValue(it)
            updateProfileViewModel.setPostalCodeErrorValue(false)
        },
            value = updateProfileViewModel.getPostalCodeController.value,
            isError = updateProfileViewModel.getPostalCodeError.value,
            supportingText = {
                if (updateProfileViewModel.getPostalCodeError.value) {
                    ErrorMessageUpdateProfileView()
                }
            },
            hint = Strings.pstlCodeText,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Pin,contentDescription = "",tint = Color.White
                )
            })
        VerticalSpacer()
        VerticalSpacer(Dimension.dimen_20)
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(50.dp)
                .wrapContentWidth()
        ) {
            CustomButton(
                border = BorderStroke(1.dp,Color.White),
                onClick = {
                    updateProfileViewModel.validateSignUpFields()
                },
                content = {
                    Text(
                        text = Strings.saveText,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp
                    )
                },
            )
        }
    }
}