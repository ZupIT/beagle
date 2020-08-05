package br.com.zup.beagle.android.context.contextoperations.operationstrategy.string

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class SubStringOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        val value = parameter.arguments[0].withoutApostrophe()
        val from = parameter.arguments[1].value as Int
        val length = parameter.arguments[2].value as Int
        var result = parameter.arguments[0].withoutApostrophe()

        if (from < value.length && length < value.length &&  length >= from) {
             result = value.substring(from, from + length)
        }

        return result.withApostropheMark()
    }


}
