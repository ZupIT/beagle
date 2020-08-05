package br.com.zup.beagle.android.context.contextoperations.operationstrategy.string

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

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
