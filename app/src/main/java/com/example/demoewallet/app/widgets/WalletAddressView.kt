package com.example.demoewallet.app.widgets

import android.content.Intent
import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.demoewallet.app.ImportWalletActivity
import com.example.demoewallet.app.TempViewModel
import com.example.demoewallet.app.model.CryptoType
import com.example.demoewallet.app.model.MetaAccount
import com.example.demoewallet.app.model.ethereumAddressFromPublicKey
import com.example.demoewallet.app.model.substrateAccountId
import com.example.demoewallet.app.theme.colorPrimary
import com.example.demoewallet.app.theme.white
import com.example.demoewallet.app.utils.*
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.ethereum.EthereumKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.ethereum.EthereumSeedFactory

@Composable
fun WalletAddressView(
    viewModel: TempViewModel = hiltViewModel(),
    assetManager: AssetManager? = null,
    lifecycleOwner: LifecycleCoroutineScope? = null,
    address: String
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val alertText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Log.d("TAG", "ImportWalletView: $address")
        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = address,
            color = Color.Black,
            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                clipboardManager.setText(AnnotatedString((address)))
                Toast.makeText(context, "copied!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorPrimary,
                contentColor = white
            )
        ) {
            Text(text = "Copy Address")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Balance: ",
                modifier = Modifier.padding(end = 8.dp),
                fontWeight = FontWeight.SemiBold,
            )
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                alertText.value = "Under Development!"
                openDialog.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorPrimary,
                contentColor = white
            )
        ) {
            Text(text = "Send Money")
        }

        Button(
            onClick = {
                alertText.value = "Under Development!"
                openDialog.value = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(20.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorPrimary,
                contentColor = white
            )
        ) {
            Text(text = "Receive Money")
        }

        AlertDialogView(openDialog = openDialog, alertText)

    }
}


@Preview
@Composable
fun  WalletAddressViewPreview(){
    WalletAddressView(address = "kdjkdjdkdkkddkdk")
}