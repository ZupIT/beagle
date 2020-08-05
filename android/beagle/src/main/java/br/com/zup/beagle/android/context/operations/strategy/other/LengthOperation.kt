package br.com.zup.beagle.android.context.operations.strategy.other

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.string.withoutApostrophe
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes

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