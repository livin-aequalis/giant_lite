package com.example.demoewallet.app

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.demoewallet.app.mappers.CryptoType
import com.example.demoewallet.app.mappers.mapCryptoTypeToEncryption
import com.example.demoewallet.app.model.Chain
import com.example.demoewallet.app.model.ChainId
import com.example.demoewallet.app.model.MetaAccount
import com.example.demoewallet.app.model.address
import com.example.demoewallet.app.network.CoingeckoApiImpl
import com.example.demoewallet.app.runtime.SubstrateRemoteSource
import com.example.demoewallet.app.runtime.Token
import com.example.demoewallet.app.runtime.Transfer
import com.example.demoewallet.app.utils.Resource
import com.example.demoewallet.app.utils.deriveSeed32
import com.example.demoewallet.app.utils.nullIfEmpty
import com.google.gson.Gson
import jp.co.soramitsu.common.data.secrets.v2.MetaAccountSecrets
import jp.co.soramitsu.common.data.secrets.v2.SecretStoreV2
import jp.co.soramitsu.common.utils.*
import jp.co.soramitsu.coredb.dao.AccountDao
import jp.co.soramitsu.coredb.dao.MetaAccountDao
import jp.co.soramitsu.coredb.model.chain.ChainAccountLocal
import jp.co.soramitsu.coredb.model.chain.MetaAccountLocal
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.SubstrateJunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.keypair.Keypair
import jp.co.soramitsu.fearless_utils.encrypt.keypair.ethereum.EthereumKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.keypair.substrate.SubstrateKeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.Mnemonic
import jp.co.soramitsu.fearless_utils.encrypt.mnemonic.MnemonicCreator
import jp.co.soramitsu.fearless_utils.encrypt.seed.ethereum.EthereumSeedFactory
import jp.co.soramitsu.fearless_utils.encrypt.seed.substrate.SubstrateSeedFactory
import jp.co.soramitsu.fearless_utils.extensions.fromHex
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAccountId
import jp.co.soramitsu.runtime.multiNetwork.ChainRegistry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

class AccountAlreadyExistsException : Exception()

@HiltViewModel
class TempViewModel
@Inject constructor(
    private val coingeckoApiImpl: CoingeckoApiImpl,
    private val substrateSource: SubstrateRemoteSource,
    private val accountDao: AccountDao,
    private val metaAccountDao: MetaAccountDao,
    private val chainRegistry: ChainRegistry,
    private val storeV2: SecretStoreV2,
) : ViewModel() {

    val TAG = javaClass.name
    private val _mnemonicWords = MutableStateFlow<List<String>>(emptyList())
    val mnemonicWords = _mnemonicWords.asStateFlow()

    fun generateMnemonic() {
        Log.d(TAG, "generateMnemonic: ")
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

        Log.d(TAG, "generateKeys: ")
        try {
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
        } catch (e: Exception) {
            Log.e(TAG, "generateKeys: $e", )
            _keyPairs.value = Resource.Error(e.message.toString())
            e.printStackTrace()
        }

    }

    private val _address = MutableStateFlow<String>("")
    val address = _address.asStateFlow()

    /**
     * Get wallet address by chain id and metaAccount data
     */
    fun getWalletAddress(chain: String, metaAccount: String, metaAccountObj: MetaAccount) {
        val chainData = Gson().fromJson(chain, Chain::class.java)
//        val metaAccountData = Gson().fromJson(metaAccount, MetaAccount::class.java)
//        Log.d(TAG, "getWalletAddress:chainData: $chainData")
//        Log.d(TAG, "getWalletAddress:metaAccountData: $metaAccountData")
        val addressStr = metaAccountObj.address(chainData)

        Log.d(TAG, "getWalletAddress:address: $addressStr")

        _address.value = addressStr ?: "--"

    }


    private val _coinApi =
        MutableStateFlow<Resource<Map<String, Map<String, BigDecimal>>>>(Resource.Empty())
    val coinApi = _coinApi.asStateFlow()

    /**
     * Get giant coin market price
     */
    fun getFiatSymbol() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data = coingeckoApiImpl.getAssetPriceCoingecko("", "")
                Log.d(TAG, "getFiatSymbol: $data")
                _coinApi.value = Resource.Success(data)
            }
        }
    }

    fun getAccountInfo(chainId: ChainId, address: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data = substrateSource.getAccountInfo(chainId, accountIdOf(address))
                Log.d(TAG, "getAccountInfo: $data")
            }
        }
    }

    fun importFromSeedNoneSuspend(
        metaId: Long,
        chainId: ChainId,
        accountName: String,
        mnemonicWords: String,
        cryptoType: jp.co.soramitsu.core.model.CryptoType,
        substrateDerivationPath: String,
        ethereumDerivationPath: String
    ) {

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // call it only once
//                    val data = saveChainAccountFromMnemonic(metaId, chainId, accountName, mnemonicWords, cryptoType, substrateDerivationPath, ethereumDerivationPath)

//                    Log.d(TAG, "importFromSeedNoneSuspend: $data")
                } catch (e: Exception) {
                    Log.e(TAG, "importFromSeedNoneSuspend: $e", )
                    e.printStackTrace()
                }
            }
        }

    }

    suspend fun saveChainAccountFromMnemonic(
        metaId: Long,
        chainId: ChainId,
        accountName: String,
        mnemonicWords: String,
        cryptoType: jp.co.soramitsu.core.model.CryptoType,
        substrateDerivationPath: String,
        ethereumDerivationPath: String,
        withEth:Boolean=false
    ) {
        return withContext(Dispatchers.Default) {
            val substrateDerivationPathOrNull = substrateDerivationPath.nullIfEmpty()
            val decodedDerivationPath = substrateDerivationPathOrNull?.let {
                SubstrateJunctionDecoder.decode(it)
            }

            val derivationResult = SubstrateSeedFactory.deriveSeed32(mnemonicWords, decodedDerivationPath?.password)

            val keys = SubstrateKeypairFactory.generate(
                encryptionType = jp.co.soramitsu.common.data.mappers.mapCryptoTypeToEncryption(
                    cryptoType
                ),
                seed = derivationResult.seed,
                junctions = decodedDerivationPath?.junctions.orEmpty()
            )

            val mnemonic = MnemonicCreator.fromWords(mnemonicWords)

            val (ethereumKeypair: Keypair?, ethereumDerivationPathOrDefault: String?) = if (withEth) {
                val ethereumDerivationPathOrDefault = ethereumDerivationPath.nullIfEmpty() ?: BIP32JunctionDecoder.DEFAULT_DERIVATION_PATH
                val decodedEthereumDerivationPath = BIP32JunctionDecoder.decode(ethereumDerivationPathOrDefault)
                val ethereumSeed = EthereumSeedFactory.deriveSeed32(mnemonicWords, password = decodedEthereumDerivationPath.password).seed
                val ethereumKeypair = EthereumKeypairFactory.generate(ethereumSeed, junctions = decodedEthereumDerivationPath.junctions)

                ethereumKeypair to ethereumDerivationPathOrDefault
            } else null to null

            val position = metaAccountDao.getNextPosition()

            val secretsV2 = MetaAccountSecrets(
                substrateKeyPair = keys,
                entropy = mnemonic.entropy,
                seed = null,
                substrateDerivationPath = substrateDerivationPath,
                ethereumKeypair = ethereumKeypair,
                ethereumDerivationPath = ethereumDerivationPathOrDefault
            )

            val metaAccount = MetaAccountLocal(
                substratePublicKey = keys.publicKey,
                substrateAccountId = keys.publicKey.substrateAccountId(),
                substrateCryptoType = cryptoType,
                ethereumPublicKey = ethereumKeypair?.publicKey,
                ethereumAddress = ethereumKeypair?.publicKey?.ethereumAddressFromPublicKey(),
                name = accountName,
                isSelected = true,
                position = position
            )

            val metaAccountId = insertAccount(metaAccount)
            storeV2.putMetaAccountSecrets(metaAccountId, secretsV2)

            metaAccountId
        }
    }
    private suspend fun insertAccount(
        metaAccount: MetaAccountLocal
    ) = try {
        metaAccountDao.insertMetaAccount(metaAccount)
    } catch (e: SQLiteConstraintException) {
        throw AccountAlreadyExistsException()
    }
    private suspend fun insertChainAccount(
        chainAccount: ChainAccountLocal
    ) = try {
        metaAccountDao.insertChainAccount(chainAccount)
    } catch (e: SQLiteConstraintException) {
        throw AccountAlreadyExistsException()
    }
    fun accountIdOf(address: ChainId, isEthereumBased: Boolean = false): ByteArray {
        return if (isEthereumBased) {
            address.fromHex()
        } else {
            address.toAccountId()
        }
    }

     fun performTransfer(chainData:String,tokenStr:String){
        val chainData = Gson().fromJson(chainData, jp.co.soramitsu.runtime.multiNetwork.chain.model.Chain::class.java)

         val token = Gson().fromJson(tokenStr, jp.co.soramitsu.runtime.multiNetwork.chain.model.Chain.Asset::class.java)
         val transferData = Transfer(
             recipient = "5HGYV69cT2gDqzCmNoX42mFJscQTR7Y5XnU5xrhT6aWLFbRY",
             amount =BigDecimal("0.001"),
             chainAsset = token
         )
        Log.d(TAG, "performTransfer: transferData: $transferData, chainData: $chainData")

        val accountId = "5FjKh6JPSsNhuRbD83KVxuMtM2YBpi86wepHyuW9gU2gvobt".toAccountId()

         viewModelScope.launch {
             withContext(Dispatchers.IO) {
                 val operationHash = substrateSource.performTransfer(accountId, chainData, transferData, null, null, false)
                 Log.d(TAG, "performTransfer: $operationHash")
             }
         }

    }
}