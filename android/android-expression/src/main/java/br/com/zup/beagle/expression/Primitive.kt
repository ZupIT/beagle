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
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/value/Primitive.java}
 * @author hernandazevedozup
 */
//TODO understand if we need to separate this class on BooleanPrimitive StringPrimitive IntPrimitive DoublePrimitive
class Primitive(var value: Any) : Value() {
    constructor(boolean: Boolean) : this(boolean as Any)
    constructor(character: CharSequence) : this(character as Any)
    constructor(number: Number) : this(number as Any)

    fun isBoolean() = value is Boolean
    fun isString() = value is String
    fun isNumber() = value is Number

    override fun copy(): Value = this

    override fun getAsString(): String {
        return when {
            isNumber() -> getAsNumber().toString()
            isBoolean() -> (value as Boolean).toString()
            else -> value as String
        }
    }

    override fun getAsBoolean(): Boolean {
        return if (isBoolean()) {
            this.value as Boolean
        } else java.lang.Boolean.parseBoolean(value.toString())
    }

    override fun getAsNumber(): Number {
        return if (isString()) LazilyParsedNumber(value as String) else value as Number
    }

    override fun getAsDouble(): Double {
        return if (isNumber()) getAsNumber().toDouble() else java.lang.Double.parseDouble(
            getAsString()
        )
    }

    override fun getAsInt(): Int {
        return if (isNumber()) getAsNumber().toInt() else Integer.parseInt(getAsString())
    }

}