package br.com.zup.beagle.android.context.contextoperations.grammar

internal object RegularExpressions {
    const val SPLIT_OPERATION_TYPE_FROM_OPERATION_REGEX = "^(\\w[\\w\\d_]*)\\((.*)\\)\$"
    const val REMOVE_WHITESPACE_REGEX = "\\s"
    const val NUMBER_REGEX = "^[0-9]*\$|^[0-9]+.[0-9]+\$"
    const val BETWEEN_APOSTROPHE_MARK = "'(.*?)'"
    const val BETWEEN_BRACKET = "\\${GrammarChars.OPEN_BRACKET}(.*?)\\${GrammarChars.CLOSE_BRACKET}"
}
