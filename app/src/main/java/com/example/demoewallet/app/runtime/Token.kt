package com.example.demoewallet.app.runtime

import jp.co.soramitsu.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal
import java.math.BigInteger

class Token(
    val fiatRate: BigDecimal?,
    val fiatSymbol: String?,
    val recentRateChange: BigDecimal?,
    val configuration: Chain.Asset
) {

    fun fiatAmount(tokenAmount: BigDecimal): BigDecimal? = fiatRate?.multiply(tokenAmount)
}

fun Token.amountFromPlanks(amountInPlanks: BigInteger) = configuration.amountFromPlanks(amountInPlanks)

fun Token.planksFromAmount(amount: BigDecimal): BigInteger = configuration.planksFromAmount(amount)

fun Chain.Asset.amountFromPlanks(amountInPlanks: BigInteger) = amountInPlanks.toBigDecimal(scale = precision)

fun Chain.Asset.planksFromAmount(amount: BigDecimal): BigInteger = amount.scaleByPowerOfTen(precision).toBigInteger()
