package woooo_app.woooo.feature.auth.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.woooo.shared.components.CustomButton
import com.wgroup.woooo_app.woooo.shared.components.VerticalSpacer
import com.wgroup.woooo_app.woooo.utils.Strings
import woooo_app.woooo.shared.components.ShowAlertDialog
import woooo_app.woooo.shared.components.ViewDivider
import woooo_app.woooo.utils.Dimension

@Composable
fun SuccessDialogAuth(title : String,message : String, onClick: () -> Unit) {
    ShowAlertDialog(content = {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(10.dp)
        ) {
            Box(modifier = Modifier.align(Alignment.Start)) {
                Text(text = title,style = MaterialTheme.typography.bodyLarge)
            }
            VerticalSpacer()
            ViewDivider()
            VerticalSpacer()

            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = message,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge
            )
            VerticalSpacer(Dimension.dimen_40)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp)
            ) {
                CustomButton(
                    border = BorderStroke(1.dp,Color.White),
                    onClick = onClick,
                    content = {
                        Text(
                            text = Strings.okText,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                    },
                )
            }
        }

    },onDismissRequest = onClick)
}