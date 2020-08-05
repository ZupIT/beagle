package br.com.zup.beagle.android.context.contextoperations.operationstrategy.number

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class NumberOperation(override val operationType: NumberOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return this.toNumberResult(parameter.arguments)
    }
}
