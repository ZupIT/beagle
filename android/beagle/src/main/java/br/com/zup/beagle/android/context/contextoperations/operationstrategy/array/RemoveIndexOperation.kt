package br.com.zup.beagle.android.context.contextoperations.operationstrategy.array

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Argument
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class RemoveIndexOperation(
    override val operationType: Operations
) : BaseOperation<Operations>() {

    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        val list = parameter.arguments[0].value as MutableList<Argument>
        val indexToRemove = parameter.arguments[1].value as Int

        if (indexToRemove <= list.size - 1 && list.isNotEmpty()) {
            list.removeAt(indexToRemove)
        } else if (list.isNotEmpty()){
            list.removeAt(list.size - 1)
        }

        return list.map {
            it.value
        }.toMutableList()
    }
}
