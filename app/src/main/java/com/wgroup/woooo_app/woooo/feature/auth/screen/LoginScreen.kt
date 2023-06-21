
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.wgroup.woooo_app.woooo.shared.components.CustomTextField
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Strings
import com.wgroup.woooo_app.woooo.utils.Dimension

@Preview
@Composable
fun LoginView() {
//    val withEmail by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // ap logo On top

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))

//        if (withEmail) {
/// Login with Email
        LoginWithEmail()
//        } else {
//            LoginWithPhone()
//        }
    }
}

@Composable
fun LoginWithPhone() {
    TODO("Not yet implemented")
}

@Composable
fun LoginWithEmail() {

    val loginViewModel:LoginViewModel = hiltViewModel()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(placeholder = {
            Text(
                text = Strings.enterEmailText,
                style = MaterialTheme.typography.labelMedium,
            )
        }, textStyle = MaterialTheme.typography.labelMedium.copy(color = WooColor.white))
        Spacer(modifier = Modifier.height(Dimension.dimen_25))
        // password
        CustomTextField(placeholder = {
            Text(
                text = Strings.pleaseEnterText,
                style = MaterialTheme.typography.labelMedium,
            )
        }, trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.VisibilityOff,
                contentDescription = "",
                tint = Color.White
            )

        }, textStyle = MaterialTheme.typography.labelMedium.copy(color = WooColor.white))
        Spacer(modifier = Modifier.height(25.dp))
        // login button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {
                loginViewModel.login()

            },
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = Strings.login,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(Dimension.dimen_50),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = WooColor.textBox)
        )
        Spacer(modifier = Modifier.height(Dimension.dimen_25))
        // divider
        CustomDivider()
        Spacer(modifier = Modifier.height(25.dp))
        //  Login With Phone Button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = Strings.LogWithPhoneText,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = WooColor.textBox)
        )

        // forgot text
        TextButton(onClick = { }) {
            Row {
                Text(text = Strings.forgotText, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        //  last Button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = Strings.dontHaveAcntText,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center,
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = WooColor.textBox)
        )
    }
}