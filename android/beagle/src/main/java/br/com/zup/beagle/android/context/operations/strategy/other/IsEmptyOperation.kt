package br.com.zup.beagle.android.context.operations.strategy.other

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.string.withoutApostrophe
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes
import br.com.zup.beagle.android.context.operations.parameter.removeWhiteSpaces

internal class IsEmptyOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any? {
        super.checkArguments(parameter)

        if (parameter.arguments.isNotEmpty()) {
            if (parameter.arguments[0].parameterType == ParameterTypes.STRING &&
                (parameter.arguments[0].value as String)
                    .removeWhiteSpaces()
                    .withoutApostrophe()
                    .isEmpty()) {
                return true
            } else if (parameter.arguments[0].parameterType == ParameterTypes.ARRAY &&
                (parameter.arguments[0].value as List<Any?>).size == 0) {
                return true
            } else if (parameter.arguments[0].parameterType == ParameterTypes.EMPTY) {
                return true
            } else if (parameter.arguments[0].parameterType == ParameterTypes.ARRAY &&
                (parameter.arguments[0].value as List<Argument>).size > 0 &&
                (parameter.arguments[0].value as List<Argument>)[0].parameterType == ParameterTypes.EMPTY) {
                return true
            }
        }

        return false
    }
}