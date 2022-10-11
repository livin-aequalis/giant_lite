package com.example.demoewallet.app.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MnemonicWordsView(list: List<String>) {

    Row(modifier = Modifier) {
        ListView(list = list.take(6),1)
        Spacer(modifier = Modifier.width(30.dp))
        ListView(list = list.takeLast(6),6)
    }

}

@Composable
fun ListView(list: List<String>,addNumber:Int =1) {
    LazyColumn(

    ) {
        itemsIndexed(list) { index, data ->

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .padding(top = 4.dp, bottom = 4.dp),
            ) {
                Text(
                    text = (index+addNumber).toString(),
                    Modifier
                        .padding(end = 8.dp),
                    color = Color.Gray,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = data,
                    color = Color.Black,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.caption
                )

            }
        }
    }
}

@Preview
@Composable
fun MnemonicWordsViewPreview() {

    MnemonicWordsView(
        listOf(
            "One",
            "Two",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten",
            "Elven",
            "Twelve"
        )
    )
}