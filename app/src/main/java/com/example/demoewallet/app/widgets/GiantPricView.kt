package com.example.demoewallet.app.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demoewallet.app.theme.colorPrimary

@Composable
fun GiantPrice(price:String?){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Giant Market Price")
        Text(text = "$$price",
        fontSize = 20.sp,
            color = colorPrimary,
        fontWeight = FontWeight.SemiBold
        , modifier = Modifier.padding(start = 8.dp))
    }
}

@Preview
@Composable
fun GiantPricePreview(){
    GiantPrice(price = "0.004")
}