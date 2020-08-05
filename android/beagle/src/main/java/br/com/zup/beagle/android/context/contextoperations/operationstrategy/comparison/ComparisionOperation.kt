package br.com.zup.beagle.android.context.contextoperations.operationstrategy.comparison

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class ComparisionOperation(
    override val operationType: ComparisionOperationTypes
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return this.toBoolean(parameter)
    }

}
