package br.com.zup.beagle.android.context.operations.strategy.other

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class IsNullOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return parameter.arguments[0].value == null
    }
}