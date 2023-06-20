import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarComposable() {
    var textState by remember { mutableStateOf("") }
    val maxLength = 110
    val lightBlue = Color(0xffd8e6ff)
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Yellow),
//        modifier = Modifier.wrapContentSize(),
        title = {
            TextField(
                modifier = Modifier.padding(10.dp),
                value = textState,
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    disabledLabelColor = lightBlue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(35.dp),
                singleLine = true,
                trailingIcon = {
                    if (textState.isNotEmpty()) {
                        IconButton(onClick = { textState = "" }) {
                            Icon(
                                imageVector = Icons.Outlined.Close, contentDescription = null
                            )
                        }
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search, contentDescription = null
                    )
                },
                onValueChange = {
                    if (it.length <= maxLength) textState = it
                },
                label = { Text(text = "Search") },
            )
        },
        navigationIcon = {

            Image(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(
                        50.dp
                    ),
                painter = painterResource(id = R.drawable.porfile),
                contentDescription = ""
            )

        },
        actions = {

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = ""
                )
            }
        },
        )
}