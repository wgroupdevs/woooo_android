package com.wgroup.woooo_app.woooo.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.R

val shadow = Shadow(
    color = Color.Black,
    offset = Offset(0.0f, 4.0f),
    blurRadius = 4.0f,
)
//
//// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily(Font(R.font.nasalization)),
//        fontWeight = FontWeight.Normal,
//        fontSize = 30.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp,
//        color = Color.White,
//        shadow = shadow
//    ), bodySmall = TextStyle(
//        fontFamily = FontFamily(Font(R.font.nasalization)),
//        fontWeight = FontWeight.Normal,
//        fontSize = 25.sp,
//        lineHeight = 12.sp,
//        letterSpacing = 0.5.sp,
//        color = Color.White
//
//    ), bodyMedium = TextStyle(
//        fontFamily = FontFamily(Font(R.font.nasalization)),
//        fontWeight = FontWeight.Normal,
//        fontSize = 20.sp,
//        lineHeight = 12.sp,
//        letterSpacing = 0.5.sp,
//        color = Color.White,
//        shadow = shadow
//
//    ), headlineSmall = TextStyle(
//        fontFamily = FontFamily(Font(R.font.nasalization)),
//        fontWeight = FontWeight.Normal,
//        fontSize = 15.sp,
//        lineHeight = 12.sp,
//        letterSpacing = 0.5.sp,
//        color = Color.White,
//        shadow = shadow
//    ),
//    // for text Field hint
//    titleSmall = TextStyle(
//        fontFamily = FontFamily(Font(R.font.nasalization)),
//        fontWeight = FontWeight.Light,
//        fontSize = 10.sp,
//        lineHeight = 5.sp,
//        letterSpacing = 0.5.sp,
//        color = colorForTextFieldHint,
//        shadow = shadow
//    ),
//
//    // for text Field input
//    displayMedium = TextStyle(
//        fontFamily = FontFamily(Font(R.font.nasalization)),
//        fontWeight = FontWeight.Normal,
//        fontSize = 14.sp,
//        lineHeight = 5.sp, shadow = shadow,
//        letterSpacing = 0.5.sp, color = Color.White,
//    )
//
//)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = WooColor.white,
        shadow = shadow
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = WooColor.colorForTextFieldHint,
        shadow = shadow,
        textDecoration = TextDecoration.None
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = WooColor.colorForTextFieldHint,
        shadow = shadow
    ),
)
