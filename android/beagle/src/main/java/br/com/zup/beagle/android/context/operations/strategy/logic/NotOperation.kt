package br.com.zup.beagle.android.context.operations.strategy.logic

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class NotOperation
    (override val operationType: LogicOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return parameter.arguments.isNotEmpty() && !(parameter.arguments[0].value as Boolean)
    }
}
