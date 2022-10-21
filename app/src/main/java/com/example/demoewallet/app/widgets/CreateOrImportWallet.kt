package com.example.demoewallet.app.widgets

import android.content.Intent
import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.demoewallet.app.CreateWalletActivity
import com.example.demoewallet.app.ImportWalletActivity
import com.example.demoewallet.app.MainActivity
import com.example.demoewallet.app.TempViewModel
import com.example.demoewallet.app.theme.colorPrimary
import com.example.demoewallet.app.theme.white
import com.example.demoewallet.app.utils.readAssetsFile
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CreateOrImportWallet(
    viewModel: TempViewModel = hiltViewModel(),
    assetManager: AssetManager? = null,
    lifecycleOwner: LifecycleCoroutineScope? = null
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        var wordsList by remember { mutableStateOf(emptyList<String>()) }

        MnemonicWordsView(list = wordsList)

        lifecycleOwner?.launchWhenStarted {
            viewModel.mnemonicWords.collectLatest {
                wordsList = it
                Log.d("TAG", "CreateOrImportWallet: $wordsList")
            }
        }

        val words =wordsList.toString().replace("[", "")
            .replace("]", "")
            .replace(",", "")


        if (wordsList.isNotEmpty()){
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString((words)))
                    Toast.makeText(context, "copied!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorPrimary,
                    contentColor = white)
            ) {
                Text(text = "Copy Mnemonic")
            }
        }
        OutlinedButton(
            onClick = {
//                context.startActivity(Intent(context, CreateWalletActivity::class.java))
                viewModel.generateMnemonic()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, colorPrimary),
            colors = ButtonDefaults.outlinedButtonColors(contentColor =  colorPrimary)
        ) {
            Text(text = "Create Wallet")
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, ImportWalletActivity::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorPrimary,
                contentColor = white)
        ) {
            Text(text = "Import Wallet")
        }
        viewModel.getFiatSymbol()


        var giantCoinPrice by remember { mutableStateOf("") }

        lifecycleOwner?.launchWhenStarted {
//            giantCoinPrice = viewModel.coinApi.value.data?.get("giant")?.get("usd").toString()
           viewModel.coinApi.collectLatest {data->
               giantCoinPrice  =if (data==null) "" else data.data?.get("giant")?.get("usd").toString()
            }
            Log.d("TAG", "GetMnemonicWordsButton: $giantCoinPrice")
        }
        if (giantCoinPrice.isNotEmpty()){
            GiantPrice(price = giantCoinPrice)
        }else{
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp)
            )
        }
    }
}


@Preview
@Composable
fun CreateOrImportWalletPreview() {
    CreateOrImportWallet()
}