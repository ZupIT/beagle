package br.com.zup.beagle.android.context.contextoperations.operationstrategy.other

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.string.withoutApostrophe
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter
import br.com.zup.beagle.android.context.contextoperations.parameter.ParameterTypes

internal class LengthOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        var value = 0

        if (parameter.arguments.isNotEmpty()) {
            if (parameter.arguments[0].parameterType == ParameterTypes.STRING) {
                value = (parameter.arguments[0].value as String).withoutApostrophe().length
            } else if (parameter.arguments[0].parameterType == ParameterTypes.ARRAY &&
                (parameter.arguments[0].value as List<Any?>).size > 0) {
                value = (parameter.arguments[0].value as List<Any?>).size
            }
        }

        return value
    }
}