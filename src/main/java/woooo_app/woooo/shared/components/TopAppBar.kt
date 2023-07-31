import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor
import woooo_app.woooo.shared.components.CustomIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComposable(
    navigationIcon: @Composable () -> Unit,onActionClick: () -> Unit
) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = WooColor.primary,

            ),

        title = {
            var text by remember { mutableStateOf("500") }

            BasicTextField(
                value = "hifdfdxz",
                onValueChange = { },
                modifier = Modifier.height(49.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.labelSmall,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 49.dp)
                        .padding(0.dp)
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.labelSmall.copy(color = WooColor.white),
                    onValueChange = {},
                    leadingIcon = {
                        CustomIcon(icon = Icons.Outlined.Search)

                    },
                    placeholder = {
                        Text(text = "Search",style = MaterialTheme.typography.labelSmall)
                    },

                    value = "messages",
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
            }
        },
        navigationIcon = navigationIcon,
        actions = {

            IconButton(onClick = {}) {
                CustomIcon(
                    icon = Icons.Outlined.Notifications,
                    modifier = Modifier
                        .size(size = 36.dp)
                        .clickable(onClick = onActionClick),

                    )
            }
        },
    )
}