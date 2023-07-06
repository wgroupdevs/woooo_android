package com.wgroup.woooo_app.woooo.shared.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.wgroup.woooo_app.R

@Composable
fun ViewDivider() {
    val divider_ic = painterResource(id = R.drawable.divider_ic)
    Image(painter = divider_ic, contentDescription = "")

}