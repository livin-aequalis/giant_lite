package com.example.demoewallet.app.utils.formatting

import com.example.demoewallet.app.utils.decimalFormatterFor
import com.example.demoewallet.app.utils.patternWith
import java.math.BigDecimal

class FixedPrecisionFormatter(
    private val precision: Int
) : NumberFormatter {

    private val delegate = decimalFormatterFor(patternWith(precision))

    override fun format(number: BigDecimal): String {
        return delegate.format(number)
    }
}
