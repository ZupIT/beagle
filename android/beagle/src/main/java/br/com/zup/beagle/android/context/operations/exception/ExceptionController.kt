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

package br.com.zup.beagle.android.context.operations.exception

import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.strategy.Operations
import br.com.zup.beagle.android.context.operations.strategy.array.ArrayOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.comparison.ComparisionOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.invalid.InvalidOperation
import br.com.zup.beagle.android.context.operations.strategy.number.NumberOperationTypes
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes
import br.com.zup.beagle.android.context.operations.parameter.Parameter
import br.com.zup.beagle.android.context.operations.parameter.ParameterTypes
import br.com.zup.beagle.android.context.operations.parameter.removeWhiteSpaces

internal class ExceptionController {

    internal fun checkOperation(operation: Operation) {
        val type = operation.operationStrategy

        if (type is InvalidOperation) {
            throw ExceptionFactory.createException(
                exceptionTypes = type.operationType as ExceptionOperationTypes,
                details = operation.operationToken
            )
        }
        else if (operationHasIncorrectSyntax(operation)) {
            throw ExceptionFactory.createException(
                exceptionTypes = ExceptionOperationTypes.MISSING_DELIMITERS,
                details = operation.operationToken
            )
        }
    }

    fun checkParameter(parameter: Parameter) {
        val operationType = parameter.operation.operationStrategy?.operationType

        if (parameter.arguments.isNotEmpty()) {
            if (operationType is StringOperationTypes) {
                checkParameterFromStringOperation(operationType, parameter)
            } else if (operationType is NumberOperationTypes ||
                operationType is ComparisionOperationTypes) {
                checkParameterFromNumberOperation(parameter)
            } else if (operationType is ArrayOperationTypes) {
                checkParameterFromArrayOperations(operationType, parameter)
            }
        } else {
            throw ExceptionFactory.createException(
                ExceptionParameterTypes.REQUIRED_ARGS,
                parameter.operation,
                parameter.arguments.size.toString()
            )
        }
    }

    private fun checkParameterFromArrayOperations(operationType: Operations, parameter: Parameter) {
        if ((parameter.arguments.size < 3 && operationType == ArrayOperationTypes.INSERT) ||
            (parameter.arguments.size < 2 && (operationType != ArrayOperationTypes.INSERT)) ||
            parameter.arguments.isEmpty()) {
            throw ExceptionFactory.createException(
                ExceptionParameterTypes.REQUIRED_ARGS,
                parameter.operation,
                parameter.arguments.size.toString())
        } else {
            if (parameter.arguments[0].parameterType != ParameterTypes.ARRAY) {
                throw ExceptionFactory.createException(
                    ExceptionParameterTypes.ARRAY,
                    parameter.operation,
                    parameter.arguments.size.toString()
                )
            } else if ((operationType == ArrayOperationTypes.REMOVE_INDEX || operationType == ArrayOperationTypes.INSERT) &&
                parameter.arguments[parameter.arguments.size - 1].parameterType != ParameterTypes.NUMBER
            ) {
                throw ExceptionFactory.createException(
                    ExceptionParameterTypes.INDEX,
                    parameter.operation,
                    parameter.arguments[parameter.arguments.size - 1].value.toString()
                )
            }
        }
    }

    private fun checkParameterFromStringOperation(operationType: Operations, parameter: Parameter) {
        if (operationType == StringOperationTypes.SUBSTRING) {
            if (parameter.arguments.size < 3) {
                throw ExceptionFactory.createException(
                    ExceptionParameterTypes.REQUIRED_ARGS,
                    parameter.operation,
                    parameter.arguments.size.toString()
                )
            } else {
                parameter.arguments.forEachIndexed { index, item ->
                    if ((index == 0 && item.parameterType != ParameterTypes.STRING)) {
                        throw ExceptionFactory.createException(
                            ExceptionParameterTypes.STRING,
                            parameter.operation,
                            item.toString()
                        )
                    } else if (index > 0 && (item.parameterType != ParameterTypes.NUMBER ||
                                item.value.toString().removeWhiteSpaces().isEmpty())) {
                        throw ExceptionFactory.createException(
                            ExceptionParameterTypes.INDEX,
                            parameter.operation,
                            item.toString()
                        )
                    }
                }
            }
        } else {
            parameter.arguments.forEach {
                if (it.parameterType != ParameterTypes.STRING) {
                    throw ExceptionFactory.createException(
                        ExceptionParameterTypes.STRING,
                        parameter.operation,
                        it.toString()
                    )
                }
            }
        }
    }

    private fun checkParameterFromNumberOperation(parameter: Parameter) {
        parameter.arguments.forEach {
            if (it.parameterType != ParameterTypes.NUMBER) {
                throw ExceptionFactory.createException(
                    ExceptionParameterTypes.NUMBER,
                    parameter.operation,
                    it.toString()
                )
            }
        }
    }

    private fun operationHasIncorrectSyntax(operation: Operation)  : Boolean {
        var openParenthesesCount = 0
        var closeParenthesesCount = 0
        var openBracketCount = 0
        var closeBracketCount = 0

        operation.operationToken.forEach {
            when (it) {
                GrammarChars.OPEN_PARENTHESES -> {
                    openParenthesesCount++
                }
                GrammarChars.CLOSE_PARENTHESES -> {
                    closeParenthesesCount++
                }
                GrammarChars.OPEN_BRACKET -> {
                    openBracketCount++
                }
                GrammarChars.CLOSE_BRACKET -> {
                    closeBracketCount++
                }
            }
        }

        if (openParenthesesCount != closeParenthesesCount && (openParenthesesCount > 0 || closeParenthesesCount > 0)) {
            return true
        } else if (openBracketCount != closeBracketCount && (openBracketCount > 0 || closeBracketCount > 0)) {
            return true
        } else if (openParenthesesCount == 0 && closeParenthesesCount == 0) {
            return true
        }

        return false
    }
}

