package br.com.zup.beagle.android.context.contextoperations.operationstrategy.other

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.string.withoutApostrophe
import br.com.zup.beagle.android.context.contextoperations.parameter.Argument
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter
import br.com.zup.beagle.android.context.contextoperations.parameter.ParameterTypes
import br.com.zup.beagle.android.context.contextoperations.parameter.removeWhiteSpaces

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