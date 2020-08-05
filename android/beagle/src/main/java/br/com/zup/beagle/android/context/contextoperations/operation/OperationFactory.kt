package br.com.zup.beagle.android.context.contextoperations.operation

import br.com.zup.beagle.android.context.contextoperations.exception.strategy.ExceptionOperationTypes
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.BaseOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.Operations
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.array.*
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.comparison.ComparisionOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.comparison.ComparisionOperationTypes
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.invalid.InvalidOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.logic.*
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.number.NumberOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.number.NumberOperationTypes
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.other.IsEmptyOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.other.IsNullOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.other.LengthOperation
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.other.OtherOperationTypes
import br.com.zup.beagle.android.context.contextoperations.operationstrategy.string.*
import java.util.*

internal object OperationFactory {
    fun createOperation(value: String) : BaseOperation<Operations>? =
        value.getOperationType()
}

private fun String.getOperationType(): BaseOperation<Operations>? =
    getSumOperation()

private fun String.getSumOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "sum" -> NumberOperation(NumberOperationTypes.SUM)
        "subtract" -> NumberOperation(NumberOperationTypes.SUBTRACT)
        "multiply" -> NumberOperation(NumberOperationTypes.MULTIPLY)
        "divide" -> NumberOperation(NumberOperationTypes.DIVIDE)
        else -> getLiteralOperation()
    }

private fun String.getLiteralOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "lt" -> ComparisionOperation(ComparisionOperationTypes.LESS_THEN)
        "lte" -> ComparisionOperation(ComparisionOperationTypes.LESS_THEN_EQUALS)
        "gt" -> ComparisionOperation(ComparisionOperationTypes.GREATER_THAN)
        "gte" -> ComparisionOperation(ComparisionOperationTypes.GREATER_THAN_EQUALS)
        "eq" -> ComparisionOperation(ComparisionOperationTypes.EQUALS)
        else -> getLogicOperation()
    }

private fun String.getLogicOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "condition" -> ConditionOperation(LogicOperationTypes.CONDITION)
        "not" -> NotOperation(LogicOperationTypes.NOT)
        "and" -> AndOperation(LogicOperationTypes.AND)
        "or" -> OrOperation(LogicOperationTypes.OR)
        else -> getStringOperation()
    }

private fun String.getStringOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "concat" -> ConcatOperation(StringOperationTypes.CONCAT)
        "lowercase" -> LowercaseOperation(StringOperationTypes.LOWERCASE)
        "uppercase" -> UppercaseOperation(StringOperationTypes.UPPERCASE)
        "capitalize" -> CapitalizeOperation(StringOperationTypes.CAPITALIZE)
        "substr" -> SubStringOperation(StringOperationTypes.SUBSTRING)
        else -> getArrayOperation()
    }

private fun String.getArrayOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "insert" -> InsertOperation(ArrayOperationTypes.INSERT)
        "remove" -> RemoveOperation(ArrayOperationTypes.REMOVE)
        "removeindex" -> RemoveIndexOperation(ArrayOperationTypes.REMOVE_INDEX)
        "includes" -> IncludesOperation(ArrayOperationTypes.INCLUDES)
        else -> getOtherOperation()
    }

private fun String.getOtherOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "isnull" -> IsNullOperation(OtherOperationTypes.IS_NULL)
        "isempty" -> IsEmptyOperation(OtherOperationTypes.IS_EMPTY)
        "length" -> LengthOperation(OtherOperationTypes.LENGTH)
        else -> getInvalidOperation()
    }

private fun String.getInvalidOperation(): BaseOperation<Operations>? =
    when (this.toLowerCase(Locale.ROOT)) {
        "null", "false", "true" -> InvalidOperation(ExceptionOperationTypes.INVALID_OPERATION)
        else -> InvalidOperation(ExceptionOperationTypes.NOT_FOUND)
    }