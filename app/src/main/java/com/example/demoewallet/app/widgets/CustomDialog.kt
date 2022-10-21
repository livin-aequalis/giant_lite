package com.example.demoewallet.app.widgets

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.demoewallet.app.theme.colorPrimary
import com.example.demoewallet.app.theme.white

@Composable
fun AlertDialogView(openDialog: MutableState<Boolean>,text:MutableState<String>) {
        if (openDialog.value){
            AlertDialog(onDismissRequest = { /*TODO*/ },
                title = {
                    Text(text = "Alert")
                },
                text = {
                    Text(text.value, fontSize = 20.sp)
                },
                confirmButton = {
                    Button(

                        onClick = {
                            openDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorPrimary,
                            contentColor = white
                        )) {
                        Text("OK")
                    }
                })
        }

}

@Preview
@Composable
fun AlertDialogViewPreview(){
}