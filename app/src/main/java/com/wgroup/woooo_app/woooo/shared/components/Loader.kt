import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun ShowLoader(onClick:()->Unit) {

    Log.d("AlertDialog","showing......")
    AlertDialog(confirmButton = { Text(text = "") },
        onDismissRequest = onClick,
        containerColor = WooColor.loaderColorBackGround,
        text = {
            Row(
                modifier = Modifier.padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(color = WooColor.backgroundColor)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Please Wait.......", style = MaterialTheme.typography.headlineSmall)
            }
        })
}
