package br.com.zup.beagle.android.context.operations.strategy.string

import br.com.zup.beagle.android.context.operations.strategy.Operations

internal enum class StringOperationTypes :
    Operations {
    CONCAT,
    CAPITALIZE,
    UPPERCASE,
    LOWERCASE,
    SUBSTRING
}
