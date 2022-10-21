package com.example.demoewallet.app.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ButtonColors
import androidx.compose.material.Colors
import androidx.compose.material.RadioButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color

val colorPrimary = Color(0xFF45b549)
val colorSelected = Color(0x6645b549)
val colorPrimaryDark = Color(0x8045b549)
val colorAccent = Color(0xFF45b549)
val colorHint = Color(0xFFCCCCCC)
val gray1 = Color(0xFFCCCCCC)
val gray2 = Color(0xFF888888)
val gray3 = Color(0xFF444444)
val gray4 = Color(0xFF181818)
val grayDisabled = Color(0xFFD0D0D0)
val colorGreyText = Color(0xFF888888)

val green = Color(0xFF07bc0c)
val greenLite = Color(0xFF5BD15F)
val greenText = Color(0xFF09C8A1)
val dividerGray = Color(0xFF444444)
val bottomSheetBackground = Color(0x80838383)
val red = Color(0xFFF00004)
val blurColorDark = Color(0xB3000000)
val blurColor = Color(0x8C000000)
val blurColorLight = Color(0x66000000)



val white04 = Color(0x0A121212)
val white08 = Color(0x14121212)
val white10 = Color(0x1A121212)
val white16 = Color(0x29121212)
val white20 = Color(0x33121212)
val white24 = Color(0x3D121212)
val white30 = Color(0x4D121212)
val white40 = Color(0x66121212)
val white50 = Color(0x80121212)
val white60 = Color(0xA6121212)
val white64 = Color(0xA3121212)

val black1 = gray1
val black2 = gray2
val black3 = gray3
val black4 = gray4
val black05 = Color(0xff1D1F21)

val purple = Color(0xFF7700EE)
val backgroundBlack = Color(0xFFFFFFFF)
val grayButtonBackground = Color(0xFFE0DFDF)
val shimmerColor = Color(0x80DBDBDB)

val accountIconLight = Color(0xFFEEEEEE)
val accountIconDark = Color(0xFF000000)
val errorRed = Color(0xFFFF3B30)

val transparent = Color(0xffffff)

val colorAccentDark = Color(0xFFEE0077)


/**
 * Add extra colors
 */

val backgroundBlurColorLight = Color(0xAECECEC)
val backgroundBlurColorDark = Color(0xAFFFFFF)

val Colors.backgroundBlurColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) backgroundBlurColorLight else backgroundBlurColorDark

val white = Color(0xFFFFFFFF)
val black = Color(0xFF000000)

val Colors.whiteOrBlack: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) white else black

val textColorLight =  Color(0xFF121212)
val textColorDark =  Color(0xFFf2f2f2)

val Colors.textColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) textColorLight else textColorDark

val buyCreditsBgLight = Color(0xFFF7FAFF)
val buyCreditsBgDark = Color(0xFF131313)

val Colors.buyCreditsBg: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) buyCreditsBgLight else buyCreditsBgDark

val iconColorLight = Color(0xFF000000)
val iconColorDark = Color(0xFFFFFFFF)

val Colors.iconColor: Color
    @Composable
    get() = if (!isSystemInDarkTheme()) iconColorLight else iconColorDark




//-------------
val accentButtonColors = object : ButtonColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) colorAccent else colorAccentDark)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) white else white64)
    }
}

fun customButtonColors(backgroundColor: Color) = object : ButtonColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f))
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) white else white64)
    }
}

val accentRadioButtonColors = object : RadioButtonColors {
    @Composable
    override fun radioColor(enabled: Boolean, selected: Boolean): State<Color> {
        val target = when {
            !enabled -> grayDisabled
            !selected -> white16
            else -> colorAccentDark
        }

        return if (enabled) {
            animateColorAsState(target, tween(durationMillis = 100))
        } else {
            rememberUpdatedState(target)
        }
    }
}
