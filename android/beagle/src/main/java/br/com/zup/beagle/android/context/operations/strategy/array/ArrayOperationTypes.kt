package br.com.zup.beagle.android.context.operations.strategy.array

import br.com.zup.beagle.android.context.operations.strategy.Operations

internal enum class  ArrayOperationTypes :
    Operations {
    INSERT,
    REMOVE,
    REMOVE_INDEX,
    INCLUDES
}
