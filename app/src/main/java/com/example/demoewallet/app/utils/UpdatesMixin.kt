package com.example.demoewallet.app.utils

import androidx.lifecycle.LiveData
import com.example.demoewallet.app.model.AssetKey
import jp.co.soramitsu.fearless_utils.runtime.AccountId

interface UpdatesMixin : UpdatesProviderUi

interface UpdatesProviderUi {
    val tokenRatesUpdate: LiveData<Set<String>>

    val assetsUpdate: LiveData<Set<AssetKey>>

    val chainsUpdate: LiveData<Set<String>>

    suspend fun startUpdateToken(symbol: String)

    suspend fun startUpdateTokens(symbols: List<String>)

    suspend fun finishUpdateTokens(symbols: List<String>)

    suspend fun finishUpdateToken(symbol: String)

    suspend fun startUpdateAsset(metaId: Long, chainId: String, accountId: AccountId, tokenSymbol: String)

    suspend fun finishUpdateAsset(metaId: Long, chainId: String, accountId: AccountId, tokenSymbol: String)

    suspend fun startChainSyncUp(chainId: String)

    suspend fun startChainsSyncUp(chainIds: List<String>)

    suspend fun finishChainSyncUp(chainId: String)
}
