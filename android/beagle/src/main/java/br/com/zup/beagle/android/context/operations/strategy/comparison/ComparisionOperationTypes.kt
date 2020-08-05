package br.com.zup.beagle.android.context.operations.strategy.comparison

import br.com.zup.beagle.android.context.operations.strategy.Operations

internal enum class ComparisionOperationTypes :
    Operations {
    GREATER_THAN,
    GREATER_THAN_EQUALS,
    LESS_THEN,
    LESS_THEN_EQUALS,
    EQUALS
}
