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

enum class MatchTypes(val regex: Regex) {
    STRING(RegularExpressions.BETWEEN_APOSTROPHE_MARK.toRegex()),
    OPERATION(RegularExpressions.SPLIT_OPERATION_TYPE_FROM_OPERATION_REGEX.toRegex()),
    ARRAY(RegularExpressions.BETWEEN_BRACKET.toRegex())
}