package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomSpacer(size: Int = 10) {
    Spacer(modifier = Modifier.size(size.dp))
}