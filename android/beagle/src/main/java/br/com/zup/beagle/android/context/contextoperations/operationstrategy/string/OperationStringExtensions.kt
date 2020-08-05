package br.com.zup.beagle.android.context.contextoperations.operationstrategy.string

import br.com.zup.beagle.android.context.contextoperations.grammar.GrammarChars
import br.com.zup.beagle.android.context.contextoperations.grammar.RegularExpressions
import br.com.zup.beagle.android.context.contextoperations.parameter.Argument

internal fun Argument.withoutApostrophe() : String {
    return (this.value as String).withoutApostrophe()
}

internal fun String.withoutApostrophe() : String {
    var parameter = ""

    RegularExpressions.BETWEEN_APOSTROPHE_MARK.toRegex()
        .findAll(this).forEach {
            it.groupValues.forEachIndexed { index, item ->
                if (index > 0) {
                    parameter = item
                }
            }
        }

    return parameter
}

internal fun String.withApostropheMark() : String =
    GrammarChars.APOSTROPHE_MARK + this + GrammarChars.APOSTROPHE_MARK
