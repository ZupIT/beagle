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

package br.com.zup.beagle.android.context.operations.core

import br.com.zup.beagle.android.context.operations.common.ExtractValueFromExpressionPDA
import br.com.zup.beagle.android.context.operations.common.ExtractValueTypes
import br.com.zup.beagle.android.context.operations.grammar.MatchTypes
import br.com.zup.beagle.android.context.operations.grammar.isOperationTypeOrArrayMatch
import br.com.zup.beagle.android.context.operations.grammar.isOperationValueMatch
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.grammar.getMatchResults
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.operation.OperationFactory
import br.com.zup.beagle.android.context.operations.strategy.BaseOperation
import br.com.zup.beagle.android.context.operations.strategy.Operations

/**
 *
 * @see Operation This class reads the expression and output an Operation

 * @see ReadMethod There is two methods to read the PDA and REGEX

 * @see ReadMethod.REGEX The regex tries to match the expression and split the operation and value,
 *                       the problem is when the match occurs when input a simple expression
 *                       example: capitalize('name'), capitalize(' secondname'), capitalize(' thirdname')
 *                       the result of parameter will be: 'name'), capitalize(' secondname'), capitalize(' thirdname'
 *                       and this is wrong see the correct solution bellow

 * @see ReadMethod.PDA The PDA comes to solve the example above, split each operation and at the finish trigger
                       the result, normally the principal operation can be split using REGEX and SubOperations using
                       PDA. REGEX can be used in a PDA result and will be able to match a value correctly.
                       result: Name Secondname Thirdname
 *
 * @see readExpression The output method that returns the operation
 *
 * **/

internal class OperationExpressionReader {

    internal fun readExpression(expression: String, readMethod: ReadMethod): Operation {
        var operationStrategy: BaseOperation<Operations>? = null
        var operationValue = ""

        if (readMethod == ReadMethod.REGEX) {
            expression.getMatchResults(MatchTypes.OPERATION).forEachIndexed { index, match ->
                if (index.isOperationTypeOrArrayMatch()) {
                    operationStrategy = OperationFactory.create(match)
                } else if (index.isOperationValueMatch()) {
                    operationValue = match
                }
            }

        } else {
            return readExpression(
                expression = ExtractValueFromExpressionPDA(
                    inputValue = expression,
                    extractValueTypes = ExtractValueTypes.OPERATION
                ).getValue().trim(),
                readMethod = ReadMethod.REGEX
            )
        }

        return Operation(
            expression,
            operationStrategy,
            operationValue
        )
    }
}

internal fun String.formatArrayParameter() : String {
    var value = this

    value = value.replace(
        GrammarChars.OPEN_BRACES, GrammarChars.OPEN_BRACKET)

    value = value.replace(
        GrammarChars.CLOSE_BRACES, GrammarChars.CLOSE_BRACKET)

    return value
}
