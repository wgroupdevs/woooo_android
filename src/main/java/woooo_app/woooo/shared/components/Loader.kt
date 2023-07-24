
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.shared.components.HorizontalSpacer
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun ShowLoader() {
    AlertDialog(modifier = Modifier
//        .alpha(0.8f)
        .background(WooColor.loaderColorBackGround,shape = RoundedCornerShape(20.dp)),
        confirmButton = { Text(text = "") },
        onDismissRequest = {},
        containerColor = WooColor.loaderColorBackGround,
        text = {
            Row(
                modifier = Modifier.padding(top = 20.dp).wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = WooColor.textFieldBackGround)
                HorizontalSpacer()
                Text(text = "Please Wait.......",style = MaterialTheme.typography.headlineSmall)
            }
        })
}
