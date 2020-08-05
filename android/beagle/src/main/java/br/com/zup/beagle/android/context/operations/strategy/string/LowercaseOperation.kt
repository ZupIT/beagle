package br.com.zup.beagle.android.context.operations.strategy.string

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import java.util.*

internal class LowercaseOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        return (parameter.arguments[0].value as String)
            .withoutApostrophe()
            .toLowerCase(Locale.ROOT)
            .withApostropheMark()
    }
}
