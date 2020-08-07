/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.android.context.operations.grammar

internal object RegularExpressions {
    const val SPLIT_OPERATION_TYPE_FROM_OPERATION_REGEX = "^(\\w[\\w\\d_]*)\\((.*)\\)\$"
    const val REMOVE_WHITESPACE_REGEX = "\\s"
    const val NUMBER_REGEX = "^[0-9]*\$|^[0-9]+.[0-9]+\$"
    const val BETWEEN_APOSTROPHE_MARK = "'(.*?)'"
    const val BETWEEN_BRACKET = "\\${GrammarChars.OPEN_BRACKET}(.*?)\\${GrammarChars.CLOSE_BRACKET}"
}

internal fun String.getMatchResults(matchTypes: MatchTypes) : List<String> {
    var matchResults: List<String> = ArrayList()

    matchTypes.regex.findAll(this).forEach {
        matchResults = it.groupValues
    }

    return matchResults
}

internal fun Int.isNotFullMatchItem() : Boolean = this > 0

internal fun Int.isOperationTypeOrArrayMatch() : Boolean = this == 1

internal fun Int.isOperationValueMatch() : Boolean = this == 2
