package br.com.zup.beagle.android.context.contextoperations.operationstrategy.number

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Argument
import br.com.zup.beagle.android.context.contextoperations.parameter.isDouble

internal fun BaseOperation<Operations>.toNumberResult(list: List<Argument>) : Any {
    var hasDecimal = false

    val result = list.map {
        if (it.value.isDouble()) {
            hasDecimal = true
        }

        it.value.toString().toDouble() as Number
    }.reduce { accumulated , element ->

        when (this.operationType) {
            NumberOperationTypes.SUM -> {
                accumulated.toDouble() + element.toDouble()
            }
            NumberOperationTypes.SUBTRACT -> {
                accumulated.toDouble() - element.toDouble()
            }
            NumberOperationTypes.MULTIPLY -> {
                accumulated.toDouble() * element.toDouble()
            }
            NumberOperationTypes.DIVIDE -> {
                accumulated.toDouble() / element.toDouble()
            }
            else -> 0
        }
    }

    return if (hasDecimal) result else result.toInt()
}
