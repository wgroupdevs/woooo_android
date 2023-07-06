import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor

@Preview
@Composable
fun ShowCircularProgressIndicator() {

    Box(
        modifier = Modifier
            .height(100.dp)
            .width(250.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start, modifier = Modifier
//                .fillMaxWidth()
                .padding(50.dp)
                .background(color = WooColor.primary)
        ) {
            CircularProgressIndicator()
            Text("Please Wait.......", style = TextStyle(color = Color.Blue))
        }
    }
}
