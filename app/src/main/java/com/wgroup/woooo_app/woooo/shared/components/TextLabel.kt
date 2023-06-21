package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TextLabel(label:String) {
    Text(label, style= MaterialTheme.typography.labelMedium)
}