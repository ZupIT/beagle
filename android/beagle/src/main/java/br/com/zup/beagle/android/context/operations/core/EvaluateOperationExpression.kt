package br.com.zup.beagle.android.context.operations.core

import br.com.zup.beagle.android.context.operations.exception.ExceptionWrapper
import br.com.zup.beagle.android.context.operations.grammar.GrammarChars
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.operation.toParameterType
import br.com.zup.beagle.android.context.operations.strategy.string.withoutApostrophe

/**
 * @property principalOperation receive the input expression and store to solve
 *                              sub operations and evaluate the result
 * @param expression expression input to solve
 * @param output expression evaluated "can be null"
 * **/

class EvaluateOperationExpression (expression: String) {

    private var principalOperation: Operation = OperationExpressionReader()
            .readExpression(
                expression.formatArrayParameter(),
                ReadMethod.REGEX
            )


    init {
        checkOperationHasError(principalOperation)
    }

    internal fun evaluate() : Any? {
        if (principalOperation.hasSubOperationsToSolve()) {
            return resolveSubOperation(principalOperation.operationValue)
        }

        return solveOperation(principalOperation, true)
    }

    private fun resolveSubOperation(operationValue: String) : Any? {
        val subOperation = OperationExpressionReader()
            .readExpression(
                operationValue,
                ReadMethod.PDA
            )
        checkOperationHasError(subOperation)
        val result = solveOperation(subOperation)
        updatePrincipalOperation(subOperation.operationToken, result.toString())

        return evaluate()
    }

    private fun updatePrincipalOperation(operationValue: String, result: Any?) {
        principalOperation = principalOperation.copy(
            operationValue = principalOperation.operationValue
                .replace(operationValue, result.toString())
        )
    }

    private fun solveOperation(operation: Operation, isFinalOperation: Boolean = false) : Any? {
        val result = operation.operationStrategy?.executeOperation(operation.toParameterType())

        return if (isFinalOperation && result.isStringType()) {
            (result as String).withoutApostrophe()
        } else {
            result
        }
    }

    private fun checkOperationHasError(operation: Operation) {
        ExceptionWrapper.checkOperation(operation)
    }
}

private fun Operation.hasSubOperationsToSolve() = this.operationValue.contains(GrammarChars.OPEN_PARENTHESES) &&
        this.operationValue.contains(GrammarChars.CLOSE_PARENTHESES)

private fun Any?.isStringType() : Boolean = this is String &&
        this.contains(GrammarChars.APOSTROPHE_MARK)
