import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor

@Preview
@Composable
fun ShowAlertDialog() {


    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(confirmButton = { Text(text = "") },
            onDismissRequest = { openDialog.value = false },
            containerColor = WooColor.textBox,
            text = {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Please Wait.......", style = MaterialTheme.typography.headlineSmall)
                }
            })
    }
}
