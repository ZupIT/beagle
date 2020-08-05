package br.com.zup.beagle.android.context.operations.strategy.comparison

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class ComparisionOperation(
    override val operationType: ComparisionOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return this.toBoolean(parameter)
    }

}
