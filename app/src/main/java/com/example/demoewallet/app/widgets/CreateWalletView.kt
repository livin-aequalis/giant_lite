package com.example.demoewallet.app.widgets

import android.content.res.AssetManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleCoroutineScope
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
import jp.co.soramitsu.fearless_utils.scale.dataType.string
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateWalletView(
    viewModel: TempViewModel = hiltViewModel(),
    assetManager: AssetManager? = null,
    lifecycleOwner: LifecycleCoroutineScope? = null
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(20.dp))
        viewModel.generateMnemonic()

        MnemonicWordsView(list = viewModel.mnemonicWords.collectAsState().value)

    }
}

@Preview
@Composable
fun CreateWalletViewPreview() {
    CreateWalletView()
}
