package br.com.zup.beagle.android.context.contextoperations.exception

import br.com.zup.beagle.android.context.contextoperations.operation.Operation
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

internal object ExceptionWrapper {
    fun checkOperation(operation: Operation) {
        ExceptionController().checkOperation(operation)
    }

    fun checkParameter(parameter: Parameter) {
        ExceptionController().checkParameter(parameter)
    }
}