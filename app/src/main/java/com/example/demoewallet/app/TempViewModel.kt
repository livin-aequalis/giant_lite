package com.example.demoewallet.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.demoewallet.app.mappers.CryptoType
import com.example.demoewallet.app.mappers.mapCryptoTypeToEncryption
import com.example.demoewallet.app.model.Chain
import com.example.demoewallet.app.model.MetaAccount
import com.example.demoewallet.app.model.address
import com.example.demoewallet.app.network.CoingeckoApiImpl
import com.example.demoewallet.app.utils.Resource
import com.example.demoewallet.app.utils.deriveSeed32
import com.example.demoewallet.app.utils.nullIfEmpty
import com.google.gson.Gson
import jp.co.soramitsu.fearless_utils.encrypt.junction.SubstrateJunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.substrate.SubstrateKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.MnemonicCreator
import jp.co.soramitsu.fearless_utils.encrypt.seed.substrate.SubstrateSeedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class TempViewModel
@Inject constructor(
    private val coingeckoApiImpl: CoingeckoApiImpl
) : ViewModel() {

    val TAG = javaClass.name
    private val _mnemonicWords = MutableStateFlow<List<String>>(emptyList())
    val mnemonicWords = _mnemonicWords.asStateFlow()

    fun generateMnemonic() {
        viewModelScope.launch {
            val generationResult = MnemonicCreator.randomMnemonic(Mnemonic.Length.TWELVE)
            _mnemonicWords.value = generationResult.wordList
        }
    }

    private val _keyPairs = MutableStateFlow<Resource<Keypair>>(Resource.Empty())
    val keyPairs = _keyPairs.asStateFlow()

    /**
     * Generate private and public keys with mnemonic words
     */
    fun generateKeys(
        cryptoType: CryptoType = CryptoType.SR25519,
        substrateDerivationPath: String = "",
        mnemonicWords: String,
    ) {

        val substrateDerivationPathOrNull = substrateDerivationPath.nullIfEmpty()
        val decodedDerivationPath = substrateDerivationPathOrNull?.let {
            SubstrateJunctionDecoder.decode(it)
        }
        val derivationResult =
            SubstrateSeedFactory.deriveSeed32(mnemonicWords, decodedDerivationPath?.password)
        val keys = SubstrateKeypairFactory.generate(
            encryptionType = mapCryptoTypeToEncryption(cryptoType),
            seed = derivationResult.seed,
            junctions = decodedDerivationPath?.junctions.orEmpty()
        )

        _keyPairs.value = Resource.Success(keys)

    }

    private val _address = MutableStateFlow<String>("")
    val address = _address.asStateFlow()

    /**
     * Get wallet address by chain id and metaAccount data
     */
    fun getWalletAddress(chain: String, metaAccount: String,metaAccountObj: MetaAccount) {
        val chainData = Gson().fromJson(chain, Chain::class.java)
//        val metaAccountData = Gson().fromJson(metaAccount, MetaAccount::class.java)
        Log.d(TAG, "getWalletAddress:chainData: $chainData")
//        Log.d(TAG, "getWalletAddress:metaAccountData: $metaAccountData")
        val addressStr = metaAccountObj.address(chainData)

        Log.d(TAG, "getWalletAddress:address: $addressStr")

        _address.value = addressStr?:"--"

    }


    private val _coinApi = MutableStateFlow<Resource<Map<String, Map<String, BigDecimal>>>>(Resource.Empty())
    val coinApi = _coinApi.asStateFlow()

    /**
     * Get giant coin market price
     */
    fun getFiatSymbol(){
        viewModelScope.launch {
          val data=  coingeckoApiImpl.getAssetPriceCoingecko("","")
            Log.d(TAG, "getFiatSymbol: $data")
            _coinApi.value = Resource.Success(data)
        }
    }
}