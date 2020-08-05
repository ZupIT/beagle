package br.com.zup.beagle.android.context.operations.strategy.logic

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class ConditionOperation(
    override val operationType: LogicOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any? {
        super.checkArguments(parameter)

        val operationResult = parameter.arguments[0].value as Boolean
        val trueValue = parameter.arguments[1].value
        val falseValue = parameter.arguments[2].value

        return if (operationResult) trueValue else falseValue
    }
}
