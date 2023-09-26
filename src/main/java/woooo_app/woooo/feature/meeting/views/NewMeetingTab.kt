package woooo_app.woooo.feature.meeting.views

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import woooo_app.woooo.shared.components.TextLabel
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import com.woooapp.meeting.impl.views.MeetingActivity
import eu.siacs.conversations.R
import kotlinx.coroutines.runBlocking
import woooo_app.woooo.shared.components.CustomIcon
import woooo_app.woooo.shared.components.view_models.UserPreferencesViewModel
import woooo_app.woooo.utils.Dimension

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@Composable
fun NewMeetingTabView() {
    val context = LocalContext.current;6

    val userPreferences: UserPreferencesViewModel = hiltViewModel()
    var meetingId by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }

    // DIALOG FOR MEETING ID
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    runBlocking {
                        val email = userPreferences.getEmail()
                        val accountUniqueId = userPreferences.getAccountUniqueId()
                        val picture = userPreferences.getProfileImage()
                        val username =
                            userPreferences.getFirstName() + userPreferences.getLastName()

                        val intent =
                            Intent(context.findActivity(), MeetingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("email", email)
                        intent.putExtra("accountUniqueId", accountUniqueId)
                        intent.putExtra("picture", picture)
                        intent.putExtra(
                            "username",
                            username.toString().toLowerCase(Locale.current)
                        )
                        intent.putExtra("joining", true)
                        intent.putExtra("joinMeetingId", meetingId)
                        context.startActivity(intent)

                    }
                }) {
                    Text(text = "Join")
                }
            },
            title = { Text(text = "Join Meeting") },
            text = {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(WooColor.backgroundColor)
                ) {
                    Text("Please Enter the Meeting ID to join")
                    Spacer(modifier = Modifier.height(3.dp))
                    TextField(
                        value = meetingId,
                        label = { Text("Meeting ID") },
                        onValueChange = { meetingId = it },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = colorResource(id = R.color.blue_primary300),
                            unfocusedContainerColor = WooColor.backgroundColor,
                            disabledContainerColor = WooColor.backgroundColor,
                        )

                    )
                }
            },
            containerColor = WooColor.backgroundColor,
        )
    }

    Column(Modifier.fillMaxSize()) {
        VerticalSpacer()
        Box(
            modifier = Modifier
                .padding(10.dp)
                .border(
                    width = 1.dp, color = WooColor.white, shape = RoundedCornerShape(15.dp)
                )
                .height(250.dp)
                .fillMaxWidth()
                .background(color = WooColor.textBox, shape = RoundedCornerShape(15.dp))
                .alpha(0.9f)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(text = Strings.camOnText, style = MaterialTheme.typography.bodyLarge)
                VerticalSpacer(Dimension.dimen_10)
                Text(text = Strings.micOnText)
                VerticalSpacer(Dimension.dimen_30)

                Row {
                    CustomIcon(icon = Icons.Outlined.Mic, modifier = Modifier.size(30.dp))
                    HorizontalSpacer(Dimension.dimen_30)
                    CustomIcon(icon = Icons.Outlined.Videocam, modifier = Modifier.size(30.dp))
                }
            }
        }
        VerticalSpacer()
        TextLabel(label = Strings.mtngNmeText)
        VerticalSpacer()
        WooTextField(onValueChange = {
//                    signUpViewModel.setNameControllerValue(it)
//                    signUpViewModel.setNameErrorValue(false)
        }, value = "",
//                    isError = signUpViewModel.getNameError.value,
//                    supportingText = {
//                        if (signUpViewModel.getNameError.value) {
//                            ErrorMessageSignUpView()
//                        }
//                    },
            hint = Strings.mtngNmeText, leadingIcon = {
                CustomIcon(icon = Icons.Rounded.Edit, modifier = Modifier)
            })
        VerticalSpacer()
        TextLabel(label = Strings.mtngUrlText)
        VerticalSpacer()
        WooTextField(onValueChange = {
//                    signUpViewModel.setNameControllerValue(it)
//                    signUpViewModel.setNameErrorValue(false)
        }, value = "",
//                    isError = signUpViewModel.getNameError.value,
//                    supportingText = {
//                        if (signUpViewModel.getNameError.value) {
//                            ErrorMessageSignUpView()
//                        }
//                    },
            hint = Strings.mtngUrlText, leadingIcon = {
                CustomIcon(icon = Icons.Rounded.Share, modifier = Modifier)
            })

        VerticalSpacer(Dimension.dimen_30)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CustomButton(
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    // New meeting
                    runBlocking {
                        val email = userPreferences.getEmail()
                        val accountUniqueId = userPreferences.getAccountUniqueId()
                        val picture = userPreferences.getProfileImage()
                        val username =
                            userPreferences.getFirstName() + userPreferences.getLastName()

                        val intent =
                            Intent(context.findActivity(), MeetingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.putExtra("email", email)
                        intent.putExtra("accountUniqueId", accountUniqueId)
                        intent.putExtra("picture", picture)
                        intent.putExtra("username", username.toString().toLowerCase(Locale.current))
                        context.startActivity(intent)
                    }
                },
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
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    // Join Meeting
                    openDialog.value = true
                },
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