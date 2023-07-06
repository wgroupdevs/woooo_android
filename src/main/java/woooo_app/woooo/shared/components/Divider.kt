package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.woooo.theme.WooColor

@Preview
@Composable
fun CustomDivider(withOr: Boolean = true) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .height(0.5.dp)
                .background(WooColor.hintText)
                .weight(1f)
        ) {}
        if (withOr) {
            Row {
                Spacer(modifier = Modifier.size(5.dp))
                Text(text = "Or", fontSize = 14.sp)
                Spacer(modifier = Modifier.size(5.dp))
            }
        }
        Box(
            modifier = Modifier
                .height(0.5.dp)
                .background(WooColor.hintText)
                .weight(1f)
        ) {}
    }
}