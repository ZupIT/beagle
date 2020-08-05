package br.com.zup.beagle.android.context.operations.strategy.invalid

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

class InvalidOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any?  = Any()
}