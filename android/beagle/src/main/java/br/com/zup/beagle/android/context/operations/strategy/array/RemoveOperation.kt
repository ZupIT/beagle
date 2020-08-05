package br.com.zup.beagle.android.context.operations.strategy.array

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class RemoveOperation(
    override val operationType: Operations
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        val list = parameter.arguments[0].value as MutableList<Argument>
        val itemToRemove = parameter.arguments[1]
        var itemFound: Any? = null

        list.forEach {
            if (it.value == itemToRemove.value) {
                itemFound = it
            }
        }

        list.remove(itemFound)

        return list.map {
            it.value
        }.toMutableList()
    }
}
