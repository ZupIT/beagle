package br.com.zup.beagle.android.context.operations.exception.strategy

internal enum class ExceptionParameterTypes :
    ExceptionTypes {
    EMPTY,
    REQUIRED_ARGS,
    NUMBER,
    ARRAY,
    INDEX,
    STRING
}
