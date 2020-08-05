package br.com.zup.beagle.android.context.operations.core

import br.com.zup.beagle.android.context.operations.common.ExtractValueFromExpressionPDA
import br.com.zup.beagle.android.context.operations.common.ExtractValueTypes
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.grammar.RegularExpressions
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

 * @see ReadMethod.PDA The PDA comes to solve the example above, split each operation and at the finish trigger the result,
 *                     normally the principal operation can be split using REGEX and SubOperations using PDA. REGEX can be used
 *                     in a PDA result and will be able to match a value correctly. result: Name Secondname Thirdname
 *
 * @see readExpression The output method that returns the operation
 *
 * **/

internal class OperationExpressionReader {

    internal fun readExpression(expression: String, readMethod: ReadMethod): Operation {
        return getOperationByRegex(expression, readMethod)
    }

    private fun getOperationByRegex(expression: String, readMethod: ReadMethod): Operation {
        var operationStrategy: BaseOperation<Operations>? = null
        var operationValue = ""

        if (readMethod == ReadMethod.REGEX) {
            RegularExpressions.SPLIT_OPERATION_TYPE_FROM_OPERATION_REGEX.toRegex()
                .findAll(expression).forEach {
                    it.groupValues.forEachIndexed { index, item ->
                        if (index == 1) {
                            operationStrategy =
                                OperationFactory.createOperation(
                                    item
                                )
                        } else if (index == 2) {
                            operationValue = item
                        }
                    }
                }
        } else {
            return getOperationByRegex(
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
