package woooo_app.woooo.shared.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextLabel(label: String, modifier: Modifier = Modifier) {
    Text(label, style = MaterialTheme.typography.labelMedium, modifier = modifier)
}