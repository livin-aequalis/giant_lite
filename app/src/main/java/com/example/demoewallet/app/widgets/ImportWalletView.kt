package com.example.demoewallet.app.widgets

import android.app.Activity
import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.demoewallet.app.TempViewModel
import com.example.demoewallet.app.model.CryptoType
import com.example.demoewallet.app.model.MetaAccount
import com.example.demoewallet.app.model.ethereumAddressFromPublicKey
import com.example.demoewallet.app.model.substrateAccountId
import com.example.demoewallet.app.utils.*
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.ethereum.EthereumKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.ethereum.EthereumSeedFactory
import kotlinx.coroutines.flow.collectLatest
import org.bouncycastle.util.encoders.Hex

@Composable
fun ImportWalletView(
    viewModel: TempViewModel = hiltViewModel(),
    assetManager: AssetManager? = null,
    lifecycleOwner: LifecycleCoroutineScope?=null
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(30.dp))

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
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    viewModel.generateKeys(mnemonicWords = mnemonic)
                    //               Log.d("TAG", "GetMnemonicWordsButton: ${viewModel.mnemoicWords.collectAsState().value} ")
                }
            ) {
                Text(text = "Import Wallet")
            }

            Spacer(modifier = Modifier.height(30.dp))
            val address = viewModel.address.collectAsState().value
            Text(
                text = "$address",
                color = Color.Black,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString((address)))
                    Toast.makeText(context, "copied!", Toast.LENGTH_SHORT).show()
                },
            ) {
                Text(text = "Copy Address")
            }
            Spacer(modifier = Modifier.height(20.dp))
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
                else -> {
                    /*Text(
                        text = "No data fond",
                        color = Color.Black
                    )*/
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

    }
}

@Preview
@Composable
fun ImportWalletViewPreview() {
    ImportWalletView()
}
