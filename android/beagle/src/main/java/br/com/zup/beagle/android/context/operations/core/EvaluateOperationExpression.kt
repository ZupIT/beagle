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

import br.com.zup.beagle.android.context.operations.grammar.Constants
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.strategy.string.withoutApostrophe

/**
 * @property principalOperation receive the input expression and store to solve
 *                              sub operations and evaluate the result
 *
 * @param expression expression input to solve
 *
 * @param operationExpressionReader The reader that's identify an operation
 *
 * @see evaluate() Method that return the result "can be null"
 * **/

internal class EvaluateOperationExpression (
    private val operationExpressionReader: OperationExpressionReader,
    expression: String
) {

    private var principalOperation: Operation = operationExpressionReader
            .readExpression(
                expression.formatArrayParameter(),
                ReadMethod.REGEX
            )

    internal fun evaluate() : Any? {
        if (principalOperation.hasSubOperationsToSolve()) {
            return resolveSubOperation(principalOperation.operationValue)
        }

        return validateOperation(principalOperation, true)
    }

    private fun resolveSubOperation(operationValue: String) : Any? {
        val subOperation = operationExpressionReader
            .readExpression(
                operationValue,
                ReadMethod.PDA
            )
        val result = validateOperation(subOperation)
        updatePrincipalOperation(subOperation.operationToken, result.toString())

        return evaluate()
    }

    private fun updatePrincipalOperation(operationValue: String, result: Any?) {
        principalOperation = principalOperation.copy(
            operationValue = principalOperation.operationValue
                .replace(operationValue, result.toString())
        )
    }

    private fun validateOperation(operation: Operation, isFinalOperation: Boolean = false) : Any? {
        val result = operation.validate()

        return if (isFinalOperation && result.isStringType()) {
            (result as String).withoutApostrophe()
        } else {
            result
        }
    }
}

private fun Operation.hasSubOperationsToSolve() = this.operationValue.contains(GrammarChars.OPEN_PARENTHESES) &&
        this.operationValue.contains(GrammarChars.CLOSE_PARENTHESES)

private fun Any?.isStringType() : Boolean = this is String &&
        this.contains(Constants.APOSTROPHE_MARK)
