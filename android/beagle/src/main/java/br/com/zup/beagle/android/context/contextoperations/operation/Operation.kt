package br.com.zup.beagle.android.context.contextoperations.operation

import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations

/**
 * @param operationToken Store the operation token that will be solved to update the principal operation with result
 *
 * @param operationStrategy The OperationType is the strategy of all operations
 * @see BaseOperation
 *
 * @param operationValue The value that will be solved by strategy above
 *
 * **/

data class Operation (val operationToken: String,
                      val operationStrategy: BaseOperation<Operations>?,
                      val operationValue: String)
