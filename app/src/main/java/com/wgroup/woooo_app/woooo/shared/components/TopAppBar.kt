import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor
import com.wgroup.woooo_app.woooo.utils.Dimension


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComposable() {
    TopAppBar(
        colors = topAppBarColors(
        containerColor = WooColor.primary,

        ),

        title = {
            OutlinedTextField(
                textStyle = MaterialTheme.typography.labelMedium.copy(color = WooColor.white),
                onValueChange = {},
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = "")

                },
                placeholder = {
                    Text(text = "Search", style = MaterialTheme.typography.labelSmall)
                },
                modifier = Modifier.fillMaxWidth(),
                value = "",
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
        },
        navigationIcon = {
            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(
                        50.dp
                    ),
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = ""
            )

        },
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