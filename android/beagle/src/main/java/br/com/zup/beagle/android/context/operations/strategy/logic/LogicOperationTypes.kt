package br.com.zup.beagle.android.context.operations.strategy.logic

import br.com.zup.beagle.android.context.operations.strategy.Operations

internal enum class LogicOperationTypes :
    Operations {
    CONDITION,
    NOT,
    AND,
    OR,
}
