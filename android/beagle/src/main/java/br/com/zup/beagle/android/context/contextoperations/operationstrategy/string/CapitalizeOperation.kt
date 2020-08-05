package br.com.zup.beagle.android.context.contextoperations.operationstrategy.string

import br.com.zup.beagle.android.context.contextoperations.grammar.GrammarChars
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal class CapitalizeOperation(override val operationType: Operations) : BaseOperation<Operations>() {
    override fun executeOperation(parameter: Parameter): Any {
        super.checkArguments(parameter)

        var value = parameter.arguments[0].withoutApostrophe()
        val listWhiteSpacePositions: MutableList<Int> = ArrayList()

        value.forEachIndexed { index, char ->
            if (char == GrammarChars.WHITE_SPACE) {
                listWhiteSpacePositions.add(index)
            }
        }

        value = value.trim()

        var result = (value[0].toUpperCase() + value.substring(1).toLowerCase())

        listWhiteSpacePositions.forEach {
            result = result.restoreWhiteSpaces(it)
        }

        return result.withApostropheMark()
    }
}

private fun String.restoreWhiteSpaces(position: Int) : String =
     this.substring(0, position) + GrammarChars.WHITE_SPACE + this.substring(position);
