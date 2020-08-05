package br.com.zup.beagle.android.context.operations.strategy.number

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class NumberOperation(override val operationType: NumberOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return this.toNumberResult(parameter.arguments)
    }
}
