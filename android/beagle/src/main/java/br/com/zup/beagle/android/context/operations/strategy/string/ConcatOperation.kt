package br.com.zup.beagle.android.context.operations.strategy.string

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class ConcatOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return parameter.arguments.map {
            it.withoutApostrophe()
        }.reduce{ accumulated , element ->
            (accumulated + element)
        }.withApostropheMark()
    }
}
