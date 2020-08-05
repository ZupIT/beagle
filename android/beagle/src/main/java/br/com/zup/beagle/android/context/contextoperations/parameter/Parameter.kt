package br.com.zup.beagle.android.context.contextoperations.parameter

import br.com.zup.beagle.android.context.contextoperations.operation.Operation

data class Parameter(val operation: Operation,
                     val arguments: List<Argument>)

data class Argument(val parameterType: ParameterTypes? = null,
                    val value: Any?)


