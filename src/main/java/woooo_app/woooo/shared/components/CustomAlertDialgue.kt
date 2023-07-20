package woooo_app.woooo.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(content: @Composable () -> Unit) {
    AlertDialog(
        onDismissRequest = { /* do something when dialog is dismissed */ },
        content = content,modifier = Modifier
////                    .wrapContentHeight()
////                    .fillMaxWidth()
//            .height(100.dp)
//            .width(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(WooColor.textBox),
    )
}