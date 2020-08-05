package br.com.zup.beagle.android.context.contextoperations.operationstrategy.logic

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class NotOperation
    (override val operationType: LogicOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return parameter.arguments.isNotEmpty() && !(parameter.arguments[0].value as Boolean)
    }
}
