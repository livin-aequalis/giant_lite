package com.example.demoewallet.app.network

import java.math.BigDecimal
import javax.inject.Inject

class CoingeckoApiImpl
@Inject constructor(
    private val httpExceptionHandler: HttpExceptionHandler,
    private val coingeckoApi: CoingeckoApi
) {


     suspend fun getAssetPriceCoingecko(
         priceId: String,
        currencyId: String
    ): Map<String, Map<String, BigDecimal>> {
//        return apiCall { coingeckoApi.getAssetPrice(priceId.joinToString(","), currencyId, true) }
        return apiCall { coingeckoApi.getAssetPrice("giant", "usd", true) }
    }

    private suspend fun <T> apiCall(block: suspend () -> T): T = httpExceptionHandler.wrap(block)
}