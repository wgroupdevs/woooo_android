import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.feature.auth.viewmodel.LoginViewModel
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomDivider
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.shared.components.WooTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension
import com.wgroup.woooo_app.woooo.utils.Strings

@Preview
@Composable
fun LoginView() {
    val withEmail by remember { mutableStateOf(true) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),

        ) {

        if (withEmail) {
            LoginWithEmail()
        } else {
            LoginWithPhoneNumber()
        }
    }
}

@Composable
fun LoginWithPhoneNumber() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        // Country picker
        WooTextField("Japan", trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "",
                tint = Color.White
            )
        })
        VerticalSpacer(Dimension.dimen_25)
        // Phone Number
        WooTextField("Enter Number", leadingIcon = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "+81",
                    style = MaterialTheme.typography.titleSmall,
                )
                VerticalSpacer()
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(WooColor.hintText)
                        .padding(5.dp)
                )
            }
        })
        VerticalSpacer(Dimension.dimen_15)
        WooTextField(
            hint = Strings.enterPasswordText,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            },
        )




        VerticalSpacer(Dimension.dimen_30)
        // login button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            content = {
                Text(
                    text = Strings.login,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            },
           )
        VerticalSpacer(Dimension.dimen_25)       // divider
        CustomDivider()
        VerticalSpacer(Dimension.dimen_25)       //  Login With Phone Button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            content = {
                Text(
                    text = Strings.LogWithPhoneText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            },
            )

        // forgot text
        TextButton(onClick = { }) {
            Row {
                Text(text = Strings.forgotText, style = MaterialTheme.typography.displayMedium)
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        //  last Button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            content = {
                Text(
                    text = Strings.dontHaveAcntText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
            },
            )

    }
}

@Composable
fun LoginWithEmail() {

    val loginViewModel: LoginViewModel = hiltViewModel()


    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f, true)
        ) {

            VerticalSpacer(Dimension.dimen_15)
            // ap logo On top

            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "",
                modifier = Modifier.size(200.dp)
            )
            VerticalSpacer(Dimension.dimen_15)


            WooTextField(hint = Strings.enterEmailText)
            VerticalSpacer(Dimension.dimen_25)
            // password
            WooTextField(Strings.enterPasswordText, trailingIcon = {
                Icon(
                    imageVector = Icons.Rounded.VisibilityOff,
                    contentDescription = "",
                    tint = Color.White
                )

            })
            // forgot text
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(start = Dimension.dimen_5)
            ) {
                TextButton(

                    onClick = { },
                    contentPadding = PaddingValues(0.dp)

                ) {
                    Text(
                        text = Strings.forgotText,
                        style = MaterialTheme.typography.labelSmall.copy(color = WooColor.white)
                    )
                }
            }
            // login button
            CustomButton(
                border = BorderStroke(1.dp, Color.White),
                onClick = {
                    loginViewModel.login()

                },
                content = {
                    Text(
                        text = Strings.login,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                },

            )
            // divider
            VerticalSpacer(Dimension.dimen_25)        // divider
            CustomDivider()
            VerticalSpacer(Dimension.dimen_25)       //  Login With Phone Button
            CustomButton(
                border = BorderStroke(1.dp, Color.White),
                onClick = {},
                content = {
                    Text(
                        text = Strings.LogWithPhoneText,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                    )
                },
              )
            VerticalSpacer(Dimension.dimen_130)       //  Login With Phone Button

        }



        TextButton(
            onClick = { },
            contentPadding = PaddingValues(0.dp)

        ) {
            Text(text = Strings.dontHaveAcntText, style = MaterialTheme.typography.labelLarge)
        }

//        //  last Button
//        CustomButton(
//            border = BorderStroke(1.dp, Color.White),
//            onClick = {},
//            shape = MaterialTheme.shapes.large,
//            content = {
//                Text(
//                    text = Strings.dontHaveAcntText,
//                    style = MaterialTheme.typography.labelLarge,
//                    textAlign = TextAlign.Center,
//                )
//            },
//            modifier = Modifier
//                .wrapContentWidth()
//                .height(50.dp),
//            colors = ButtonDefaults.outlinedButtonColors(containerColor = WooColor.textBox)
//        )
    }
}