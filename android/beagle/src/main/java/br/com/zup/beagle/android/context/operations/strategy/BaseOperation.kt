package br.com.zup.beagle.android.context.operations.strategy

import br.com.zup.beagle.android.context.operations.exception.ExceptionWrapper
import br.com.zup.beagle.android.context.operations.parameter.Parameter

abstract class BaseOperation <T : Operations> {
    abstract val operationType: T
    abstract fun executeOperation(parameter: Parameter) : Any?
    protected fun checkArguments(parameter: Parameter) {
        ExceptionWrapper.checkParameter(parameter)
    }
}
