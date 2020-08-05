package br.com.zup.beagle.android.context.contextoperations.operationstrategy

import br.com.zup.beagle.android.context.contextoperations.exception.ExceptionWrapper
import br.com.zup.beagle.android.context.contextoperations.parameter.Parameter

abstract class BaseOperation <T : Operations> {
    abstract val operationType: T
    abstract fun executeOperation(parameter: Parameter) : Any?
    protected fun checkArguments(parameter: Parameter) {
        ExceptionWrapper.checkParameter(parameter)
    }
}
