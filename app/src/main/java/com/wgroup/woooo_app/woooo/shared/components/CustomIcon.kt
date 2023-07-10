package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.wgroup.woooo_app.woooo.theme.WooColor

@Composable
fun CustomIcon(icon: ImageVector,modifier: Modifier) {
    Icon(
        imageVector = icon,contentDescription = "",tint = WooColor.white,modifier = modifier
    )
}