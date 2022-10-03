package com.example.demoewallet.app

import android.content.res.AssetManager
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demoewallet.app.model.CryptoType
import com.example.demoewallet.app.model.MetaAccount
import com.example.demoewallet.app.model.ethereumAddressFromPublicKey
import com.example.demoewallet.app.model.substrateAccountId
import com.example.demoewallet.app.utils.*
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.ethereum.EthereumKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.ethereum.EthereumSeedFactory
import org.bouncycastle.util.encoders.Hex

@Composable
fun GetMnemonicWordsButton(
    viewModel: TempViewModel = hiltViewModel(),
    assetManager: AssetManager? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                viewModel.generateMnemonic()
//               Log.d("TAG", "GetMnemonicWordsButton: ${viewModel.mnemoicWords.collectAsState().value} ")
            }
        ) {
            Text(text = "GET MNEMONIC WORDS")
        }

        Text(
            text = "Mnemonic Words: ${viewModel.mnemonicWords.collectAsState().value}",
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        viewModel.generateKeys(mnemonicWords = Const.mnemonicWords)

        /*assetManager?.let { assetManager ->
            viewModel.getWalletAddress(
                assetManager.readAssetsFile("chain_data.json"),
                assetManager.readAssetsFile("meta_account.json")
            )
        }
*/
        Text(
            text = "Address: ${viewModel.address.collectAsState().value}",
            color = Color.Black,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        )
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


                Text(
                    text = "Private Key: ${result.data?.publicKey?.let { String(it) }}",
                    color = Color.Black
                )
            }
            else -> {
                Text(
                    text = "No data fond",
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        viewModel.getFiatSymbol()

        Text(
            text = "Private Key: }",
            color = Color.Black
        )

    }
}

@Preview
@Composable
fun GetMnemonicWordsButtonPreview() {
    GetMnemonicWordsButton()
}
