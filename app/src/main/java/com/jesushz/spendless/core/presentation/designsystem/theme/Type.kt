package com.jesushz.spendless.core.presentation.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.jesushz.spendless.R

// Custom font family
val FigTreeTypography = FontFamily(
    Font(R.font.figtree_regular),
    Font(R.font.figtree_medium, FontWeight.Medium),
    Font(R.font.figtree_semi_bold, FontWeight.SemiBold),
    Font(R.font.figtree_bold, FontWeight.Bold),
    Font(R.font.figtree_extra_bold, FontWeight.ExtraBold),
    Font(R.font.figtree_light, FontWeight.Light),
    Font(R.font.figtree_black, FontWeight.Black),
    Font(R.font.figtree_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.figtree_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.figtree_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.figtree_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.figtree_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.figtree_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.figtree_black_italic, FontWeight.Black, FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.SemiBold,
        fontSize = 45.sp,
        lineHeight = 52.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FigTreeTypography,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)
