package com.wgroup.woooo_app.woooo.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wgroup.woooo_app.R
import com.wgroup.woooo_app.woooo.theme.CustomColorTheme.colorForTextFieldHint

val shadow = Shadow(
    color = Color.Black,
    offset = Offset(0.0f, 4.0f),
    blurRadius = 4.0f,
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = Color.White,
        shadow = shadow
    ), bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 25.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        color = Color.White

    ), bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        color = Color.White,
        shadow = shadow

    ), headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        color = Color.White,
        shadow = shadow
    ),
    // for text Field hint
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        lineHeight = 5.sp,
        letterSpacing = 0.5.sp,
        color = colorForTextFieldHint,
        shadow = shadow
    ),

    // for text Field input
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 5.sp, shadow = shadow,
        letterSpacing = 0.5.sp, color = Color.White,
    )
, displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.nasalization)),
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 10.sp, shadow = shadow,
        letterSpacing = 0.5.sp, color = Color.White,
    )

)
