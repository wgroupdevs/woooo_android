package woooo_app.woooo.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowAlertDialog(
    content: @Composable () -> Unit,onDismissRequest: () -> Unit
) {
    val backColor = Color(0x9940C4FF)
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(backColor)
            .fillMaxWidth(),
        content = content
    )
}