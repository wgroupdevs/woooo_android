package com.wgroup.woooo_app.woooo.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    ), bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    ), bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp
    ), headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp, color = Color.White
    ),

)