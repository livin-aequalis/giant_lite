package com.example.demoewallet.app.utils.formatting

import com.example.demoewallet.app.utils.decimalFormatterFor
import com.example.demoewallet.app.utils.patternWith
import java.lang.Integer.max
import java.math.BigDecimal

class DynamicPrecisionFormatter(
    private val minPrecision: Int
) : NumberFormatter {

    override fun format(number: BigDecimal): String {
        // scale() - total amount of digits after 0.,
        // precision() - amount of non-zero digits in decimal part
        val requiredPrecision = number.scale() - number.precision() + 1

        val formattingPrecision = max(minPrecision, requiredPrecision)

        return decimalFormatterFor(patternWith(formattingPrecision)).format(number)
    }
}
