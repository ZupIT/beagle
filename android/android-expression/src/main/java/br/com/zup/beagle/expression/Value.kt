/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.zup.beagle.expression

/**
 * This class is based on flipkart-incubator/proteus's implementation
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/value/Value.java}
 * @author hernandazevedozup
 */
abstract class Value {

    abstract fun copy(): Value

    open fun getAsString(): String {
        throw IllegalAccessException("Subclass should implement")
    }

    open fun getAsBoolean(): Boolean {
        throw IllegalAccessException("Subclass should implement")
    }

    open fun getAsDouble(): Double {
        throw IllegalAccessException("Subclass should implement")
    }

    open fun getAsInt(): Int {
        throw IllegalAccessException("Subclass should implement")
    }

    open fun getAsNumber(): Number {
        throw IllegalAccessException("Subclass should implement")
    }

    open fun getAsComplexValue(): ObjectValue {
        if (isObject()) {
            return this as ObjectValue
        }
        throw IllegalStateException("It is not a Value")
    }

    open fun getAsObject(): ObjectValue {
        if (isObject()) {
            return this as ObjectValue
        }
        throw IllegalStateException("Not an ObjectValue: $this")
    }

    open fun isObject() = this is ObjectValue
    open fun isPrimitive() = this is Primitive
    open fun isArray() = this is Array
    open fun isNull() = this is Null

    open fun getAsArray(): Array {
        if (isArray()) {
            return this as Array
        }
        throw IllegalStateException("This is not a Array.")
    }

    fun getAsPrimitive(): Primitive {
        if (isPrimitive()) {
            return this as Primitive
        }
        throw IllegalStateException("This is not a Primitive.")
    }
}