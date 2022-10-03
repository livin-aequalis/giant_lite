package jp.co.soramitsu.common.utils.formatting

import com.example.demoewallet.app.utils.formatting.NumberFormatter
import java.math.BigDecimal

class NumberAbbreviation(
    val threshold: BigDecimal,
    val divisor: BigDecimal,
    val suffix: String,
    val formatter: NumberFormatter
)
