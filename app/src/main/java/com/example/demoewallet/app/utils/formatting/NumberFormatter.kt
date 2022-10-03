package com.example.demoewallet.app.utils.formatting

import java.math.BigDecimal

interface NumberFormatter {

    fun format(number: BigDecimal): String
}
