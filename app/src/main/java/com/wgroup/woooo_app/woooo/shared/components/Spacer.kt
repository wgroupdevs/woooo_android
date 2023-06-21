package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wgroup.woooo_app.woooo.utils.Dimension

@Composable
fun VerticalSpacer(height: Dp = Dimension.dimen_10) {
    Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(size: Int = 10) {
    Spacer(modifier = Modifier.size(size.dp))
}