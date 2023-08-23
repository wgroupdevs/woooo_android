package woooo_app.woooo.shared.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun CustomButton(
    onClick: () -> Unit,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
    border: BorderStroke? = BorderStroke(0.dp,Color.Transparent)
) {
    Button(
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 10.dp,
            pressedElevation = 10.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 10.dp,
            focusedElevation = 10.dp
        ),
        content = content,
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = WooColor.selectedButtonColor),
        contentPadding = contentPadding,
        modifier = Modifier
            .wrapContentWidth().wrapContentHeight(),
//            .height(Dimension.dimen_50),
        border = border,
    )
}