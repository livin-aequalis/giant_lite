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
import com.example.demoewallet.app.WalletAddressActivity
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImportWalletView(
    viewModel: TempViewModel = hiltViewModel(),
    assetManager: AssetManager? = null,
    lifecycleOwner: LifecycleCoroutineScope? = null
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val openDialog = remember { mutableStateOf(false) }
    val alertText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(50.dp))

        var mnemonic by remember { mutableStateOf("") }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(start = 15.dp, top = 10.dp, end = 15.dp)
                .background(Color.White, RoundedCornerShape(5.dp)),
            shape = RoundedCornerShape(5.dp),
            value = mnemonic,
            onValueChange = { mnemonic = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorPrimary,
                cursorColor = colorPrimary
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.generateKeys(mnemonicWords = mnemonic)
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
            Text(text = "Import Wallet")
        }

        when (val result = viewModel.keyPairs.collectAsState().value) {
            is Resource.Success -> {

                val (ethereumKeypair: Keypair?, ethereumDerivationPathOrDefault: String?) = if (true) {
                    val ethereumDerivationPathOrDefault =
                        "".nullIfEmpty() ?: BIP32JunctionDecoder.DEFAULT_DERIVATION_PATH
                    val decodedEthereumDerivationPath =
                        BIP32JunctionDecoder.decode(ethereumDerivationPathOrDefault)
                    val ethereumSeed = EthereumSeedFactory.deriveSeed32(
                        Const.mnemonicWords,
                        password = decodedEthereumDerivationPath.password
                    ).seed
                    val ethereumKeypair = EthereumKeypairFactory.generate(
                        ethereumSeed,
                        junctions = decodedEthereumDerivationPath.junctions
                    )

                    ethereumKeypair to ethereumDerivationPathOrDefault
                } else null to null

                result.data?.let { keys ->
                    val metaAccount = MetaAccount(
                        substratePublicKey = keys.publicKey,
                        substrateAccountId = keys.publicKey.substrateAccountId(),
                        substrateCryptoType = CryptoType.SR25519,
                        chainAccounts = emptyMap(),
                        name = "Test Name",
                        id = 1,
                        isSelected = true,
                        ethereumPublicKey = ethereumKeypair?.publicKey,
                        ethereumAddress = ethereumKeypair?.publicKey?.ethereumAddressFromPublicKey()
                    )

                    assetManager?.let { assetManager ->
                        viewModel.getWalletAddress(
                            assetManager.readAssetsFile("chain_data.json"),
                            assetManager.readAssetsFile("meta_account.json"),
                            metaAccount
                        )
                    }

                }

                /* Text(
                     text = "Private Key: ${result.data?.publicKey?.let { String(it) }}",
                     color = Color.Black
                 )*/
            }
            is Resource.Error -> {
                openDialog.value = true
                alertText.value = "Enter valid mnemonic words"
            }
            else -> {
                /*Text(
                    text = "No data fond",
                    color = Color.Black
                )*/
            }
        }

        val address = viewModel.address.collectAsState().value

        if (address.isNotEmpty()) {
            context.startActivity(Intent(context, WalletAddressActivity::class.java).apply {
                putExtra("address", address)
            })
        }


        AlertDialogView(openDialog = openDialog, alertText)

    }
}

@Preview
@Composable
fun ImportWalletViewPreview() {
    ImportWalletView()
}
