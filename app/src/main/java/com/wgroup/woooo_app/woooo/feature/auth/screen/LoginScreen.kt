
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.CustomDivider
import com.wgroup.woooo_app.woooo.shared.components.CustomTextField
import com.wgroup.woooo_app.woooo.theme.CustomColorTheme
import com.wgroup.woooo_app.woooo.utils.Strings

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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomTextField(placeholder = {
            Text(
                text = Strings.enterEmailText,
                style = MaterialTheme.typography.titleSmall,
            )
        }, textStyle = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(25.dp))

        // password
        CustomTextField(placeholder = {
            Text(
                text = Strings.pleaseEnterText,
                style = MaterialTheme.typography.titleSmall,
            )
        }, trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.VisibilityOff,
                contentDescription = "",
                tint = Color.White
            )

        }, textStyle = MaterialTheme.typography.displayMedium)
        Spacer(modifier = Modifier.height(25.dp))
        // login button
        CustomButton(
            border = BorderStroke(1.dp, Color.White),
            onClick = {},
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = Strings.login,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CustomColorTheme.textBox)
        )
        Spacer(modifier = Modifier.height(25.dp))
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
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CustomColorTheme.textBox)
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
            shape = MaterialTheme.shapes.large,
            content = {
                Text(
                    text = Strings.dontHaveAcntText,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .wrapContentWidth()
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = CustomColorTheme.textBox)
        )
    }
}