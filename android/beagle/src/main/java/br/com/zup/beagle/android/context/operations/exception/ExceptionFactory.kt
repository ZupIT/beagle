package br.com.zup.beagle.android.context.operations.exception

import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionParameterTypes
import br.com.zup.beagle.android.context.operations.exception.strategy.ExceptionTypes
import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.strategy.string.StringOperationTypes

object ExceptionFactory {

    fun createException (exceptionTypes: ExceptionTypes,
                         operation: Operation? = null,
                         details: String) : Exception {
        val message: String

        if (exceptionTypes is ExceptionOperationTypes) {
            message = when (exceptionTypes) {
                ExceptionOperationTypes.NOT_FOUND -> {
                    "Could not resolve your operation. Did you check if the Operation is valid?:: $details"
                }
                ExceptionOperationTypes.INVALID_OPERATION -> {
                    "You can't use reserved input in a function name as (true, false, null, number and symbols) or use in parameter(true, false, null):: check $details in your operation"
                }
                ExceptionOperationTypes.MISSING_DELIMITERS -> {
                    "You forgot to input correct delimiters to function() or array{}:: check $details in your operation"
                }
            }
        } else {
            message = when (exceptionTypes as ExceptionParameterTypes) {
                ExceptionParameterTypes.EMPTY -> {
                    "Argument in operation ${operation?.operationToken} cannot be " +  exceptionTypes.name
                }
                ExceptionParameterTypes.NUMBER -> {
                    "Number operations parameters must be a number:: ${operation?.operationToken}"
                }
                ExceptionParameterTypes.ARRAY -> {
                    "First parameter from Array Operations ${operation?.operationToken} must be an Array"
                }
                ExceptionParameterTypes.STRING -> {
                    if (operation?.operationStrategy?.operationType == StringOperationTypes.SUBSTRING) {
                        "SubString operations must have substr('string', index(number), length(number)):: ${operation.operationToken}"
                    } else {
                        "String value must be between (apostrophe mark ->'<-):: $details"
                    }
                }
                ExceptionParameterTypes.INDEX -> {
                    "Index for operation ${operation?.operationToken} needs to be a Number:: $details"
                }
                ExceptionParameterTypes.REQUIRED_ARGS -> {
                    "Non passed required arguments for operation ${operation?.operationToken}:: args passed $details"
                }
            }
        }

        return Exception(message)
    }
}