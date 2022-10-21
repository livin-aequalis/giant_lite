package com.example.demoewallet.app.widgets

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.demoewallet.app.theme.colorPrimary
import com.example.demoewallet.app.theme.white

@Composable
fun ButtonView(onClick: () -> Unit, text: String) {
    Button(
        onClick = onClick, Modifier.background(colorPrimary),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorPrimary,
            contentColor = white
        )
    ) {
        Text(text = text)
    }
}