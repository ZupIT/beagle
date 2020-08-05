package br.com.zup.beagle.android.context.contextoperations.operationstrategy.string

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter
import java.util.*

internal class UppercaseOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return (parameter.arguments[0].value as String)
            .withoutApostrophe()
            .toUpperCase(Locale.ROOT)
            .withApostropheMark()
    }
}
