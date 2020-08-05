package br.com.zup.beagle.android.context.contextoperations.operationstrategy.logic

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations

internal enum class LogicOperationTypes :
    Operations {
    CONDITION,
    NOT,
    AND,
    OR,
}
