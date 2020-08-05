package br.com.zup.beagle.android.context.contextoperations.operationstrategy.logic

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class AndOperation(
    override val operationType: LogicOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        parameter.arguments.forEach {
            if (!(it.value as Boolean)) {
                return false
            }
        }

        return true
    }
}
