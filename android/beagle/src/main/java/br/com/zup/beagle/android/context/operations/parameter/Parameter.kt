package br.com.zup.beagle.android.context.operations.parameter

import br.com.zup.beagle.android.context.operations.operation.Operation

data class Parameter(val operation: Operation,
                     val arguments: List<Argument>)

data class Argument(val parameterType: ParameterTypes? = null,
                    val value: Any?)


