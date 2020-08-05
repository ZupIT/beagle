package br.com.zup.beagle.android.context.operations.strategy.array

import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.parameter.Argument
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal class InsertOperation(
    override val operationType: Operations
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        val list = parameter.arguments[0].value as MutableList<Any?>
        val itemToInsert = parameter.arguments[1]
        val indexToInsert = parameter.arguments[2].value as Int

        if (indexToInsert <= list.size - 1) {
            list[indexToInsert] = itemToInsert
        } else {
            list.add(itemToInsert)
        }

        return list.map {
            (it as Argument).value
        }.toMutableList()
    }
}
