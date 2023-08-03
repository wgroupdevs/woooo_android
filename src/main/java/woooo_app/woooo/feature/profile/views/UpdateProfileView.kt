package woooo_app.woooo.feature.profile.views

import ShowLoader
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.ErrorMessageUpdateProfileView
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.Shapes
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import eu.siacs.conversations.http.model.UserBasicInfo
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.destinations.HomeScreenDestination
import woooo_app.woooo.feature.auth.screen.SuccessDialogAuth
import woooo_app.woooo.feature.profile.viewmodels.UpdateProfileViewModel
import woooo_app.woooo.shared.components.CustomDateTimePicker
import woooo_app.woooo.shared.components.ViewDivider
import woooo_app.woooo.shared.components.view_models.DateTimerPickerViewModel
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.utils.Dimension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateProfileView(navigator: DestinationsNavigator) {
    val dateTimerPickerViewModel: DateTimerPickerViewModel = hiltViewModel()
    val updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()
    val context = LocalContext.current
    val userPreferencesViewModel: UserPreferencesViewModel = hiltViewModel()
    val gg = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = "To_Call_fillUserInfo()_Only_One_Time ", block = {
        fillUserInfo(updateProfileViewModel, userPreferencesViewModel)
    })
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it.let {
                if (gg.value) updateProfileViewModel.profileImage.value = it.toString()
                if (it != null) {
                    updateProfileViewModel.uploadProfile(
                        "id",
                        context,
                        it
                    )
                }
            }


        })

    Column(
        Modifier
            .fillMaxSize()
            .background(color = WooColor.backgroundColor)
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
                modifier = Modifier.clickable(onClick = { navigator.popBackStack() })
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
                Modifier
                    .clip(CircleShape)
                    .clickable(onClick = {
                        gg.value = true
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    })
            ) {
                AsyncImage(
                    model = updateProfileViewModel.profileImage.value,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .background(color = Color.Transparent, shape = CircleShape)
                        .border(2.dp, Color.White, shape = CircleShape),
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
                .background(WooColor.textFieldBackGround, shape = RoundedCornerShape(10.dp))
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
                    imageVector = Icons.Rounded.Person, contentDescription = "", tint = Color.White
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
                    imageVector = Icons.Rounded.Person, contentDescription = "", tint = Color.White
                )
            })
        //email
        TextLabel(label = Strings.emailText)
        VerticalSpacer()
        WooTextField(
            onValueChange = {
//                updateProfileViewModel.setEmailControllerValue(it)
//                updateProfileViewModel.setEmailErrorValue(false)
            }, value = updateProfileViewModel.getEmailController,
//            isError = signUpViewModel.getEmailError.value,
//            supportingText = {
//                if (signUpViewModel.getEmailError.value) {
//                    ErrorMessageUpdateProfileView()
//                }
//            },
            hint = Strings.emailText, leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Email, contentDescription = "", tint = Color.White
                )
            }, readOnly = true
        )
        //phone number
        TextLabel(label = Strings.phoneNmbrText)
        VerticalSpacer()
        WooTextField(
            hint = Strings.enterNumberText, value = updateProfileViewModel.getPhoneController,
//            leadingIcon = {
//                Row(
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "+81",
//                        style = MaterialTheme.typography.labelMedium,
//
//                        )
//                    HorizontalSpacer()
//                    Box(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .width(1.dp)
//                            .background(WooColor.hintText)
//                            .padding(5.dp)
//                    )
//                }
//            },
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
//                updateProfileViewModel.setDOBControllerValue(it)
                updateProfileViewModel.setDOBErrorValue(false)
            },
            readOnly = true,
            value = updateProfileViewModel.getDOBController.value,
            isError = updateProfileViewModel.getDOBError.value,
            supportingText = {
                if (updateProfileViewModel.getDOBError.value) {
                    ErrorMessageUpdateProfileView()
                }
            },
            hint = "2010-05-15",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Cake, contentDescription = "", tint = Color.White
                )
            },

            )

        // address
        TextLabel(label = Strings.addressText)
        VerticalSpacer()
        WooTextField(onValueChange = {
            updateProfileViewModel.setAddressControllerValue(it)
            updateProfileViewModel.setAddressErrorValue(false)
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
                    imageVector = Icons.Rounded.Pin, contentDescription = "", tint = Color.White
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
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    if (updateProfileViewModel.validateUpdateProfileFields()) {

                        updateProfileViewModel.updateProfile(context)
                    }
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
        Box(modifier = Modifier.height(400.dp))

        // enable Loader when Api Hit
        if (updateProfileViewModel.updateProfileStates.value.isLoading.value) ShowLoader()

        // enabled success dialgue
        if (updateProfileViewModel.updateProfileStates.value.isSucceed.value) {

            var basicInfo: UserBasicInfo =
                updateProfileViewModel.updateProfileStates.value.data.Data!!

            runBlocking {
                updateProfileViewModel.saveUserInfoToPreferencesOnUpdateProfile(
                    userPreferencesViewModel,basicInfo
                )
            }

            SuccessDialogAuth(title = Strings.updateSuccess,
                message = updateProfileViewModel.updateProfileStates.value.message,
                onClick = {
                    updateProfileViewModel.updateProfileStates.value.apply {
                        isSucceed.value = false
                    }
                    navigator.popBackStack(HomeScreenDestination, false)
                })
        }
        // enable date of birth picker
        if (dateTimerPickerViewModel.getDateDialogueShowForUpdateProfile.value) CustomDateTimePicker(
            onDateChange = {
//            pass value to controller
                updateProfileViewModel.setDOBControllerValue(it.toString())
                updateProfileViewModel.setDOBErrorValue(false)

                Log.d("date of birth ...", updateProfileViewModel.getDOBController.value)
                // convert it to local to show in text field
                val date = LocalDate.parse(it.toString())
                dateTimerPickerViewModel.setDateTextValueForUpdateProfile(date)
                dateTimerPickerViewModel.setDateDialogueValueForUpdateProfile(false)
//            updateProfileViewModel.setDOBControllerValue(dateTimerPickerViewModel.getDatePickerTextForUpdateProfile.value)

            },
            onDismissRequest = {
                dateTimerPickerViewModel.setDateDialogueValueForUpdateProfile(false)

            })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun fillUserInfo(
    updateProfileViewModel: UpdateProfileViewModel,
    userPreferencesViewModel: UserPreferencesViewModel
) {

    runBlocking {

        updateProfileViewModel.profileImage.value = userPreferencesViewModel.getProfileImage()
        updateProfileViewModel.setAboutControllerValue(userPreferencesViewModel.getAbout())
        updateProfileViewModel.setNameControllerValue(userPreferencesViewModel.getFirstName())
        updateProfileViewModel.setLastNameControllerValue(userPreferencesViewModel.getLastName())
        updateProfileViewModel.getEmailController = userPreferencesViewModel.getEmail()
        updateProfileViewModel.getPhoneController = userPreferencesViewModel.getPhone()
        updateProfileViewModel.setDOBControllerValue(userPreferencesViewModel.getDOB())
//        updateProfileViewModel.setDOBControllerValue(convertDOBinLocal(userPreferencesViewModel))
//        if(userPreferencesViewModel.getDOB().isNotEmpty()){
//
//            updateProfileViewModel.setDOBControllerValue(convertDOBinLocal(userPreferencesViewModel))
//        }

        updateProfileViewModel.setDOBControllerValue("")
        updateProfileViewModel.setPostalCodeControllerValue(userPreferencesViewModel.getPostalCode())
        updateProfileViewModel.setAddressControllerValue(userPreferencesViewModel.getAddress())
        updateProfileViewModel.language.value = userPreferencesViewModel.getLanguage()
        updateProfileViewModel.languageCode.value = userPreferencesViewModel.getLanguageCode()

    }
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun convertDOBinLocal(userPreferencesViewModel: UserPreferencesViewModel): String {
    val serverDateTimeString = userPreferencesViewModel.getDOB()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
    return if (serverDateTimeString.equals(formatter)) {
        val dateTime = LocalDateTime.parse(serverDateTimeString,formatter)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val dateString = dateTime.format(dateFormatter)
        Log.d("yes Coming ","")
        dateString
    } else {
        userPreferencesViewModel.getDOB()
    }

}