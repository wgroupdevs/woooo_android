package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun CustomIcon(icon: ImageVector,modifier: Modifier = Modifier) {
    Icon(
        imageVector = icon,
        contentDescription = "",
        tint = WooColor.white,
        modifier = modifier.size(24.dp)
    )
}