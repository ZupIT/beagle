package br.com.zup.beagle.android.context.operations.exception

import br.com.zup.beagle.android.context.operations.operation.Operation
import br.com.zup.beagle.android.context.operations.parameter.Parameter

internal object ExceptionWrapper {
    fun checkOperation(operation: Operation) {
        ExceptionController().checkOperation(operation)
    }

    fun checkParameter(parameter: Parameter) {
        ExceptionController().checkParameter(parameter)
    }
}