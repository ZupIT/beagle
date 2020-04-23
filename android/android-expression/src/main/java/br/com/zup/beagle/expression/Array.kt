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
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/value/Array.java}
 * @author hernandazevedozup
 */
class Array(var values: MutableList<Value>) : Value() {

    constructor() : this(mutableListOf())

    override fun copy(): Array {
        val result = Array()
        for (value in values) {
            result.add(value.copy())
        }
        return result
    }

    fun size(): Int {
        return values.size
    }

    fun add(bool: Boolean) {
        values.add(Primitive(bool))
    }

    fun add(character: Char) {
        values.add(Primitive(character))
    }

    fun add(number: Number) {
        values.add(Primitive(number))
    }

    fun add(string: String) {
        values.add(Primitive(string))
    }

    fun add(value: Value) {
        values.add(value)
    }

    operator fun get(i: Int): Value {
        return values[i]
    }

    operator fun set(index: Int, value: Value): Value {
        return values.set(index, value)
    }

    fun remove(value: Value): Boolean {
        return values.remove(value)
    }

    fun remove(index: Int): Value {
        return values.removeAt(index)
    }

    fun clear() {
        values.clear()
    }

    fun add(position: Int, value: Value) {
        values.add(position, value)
    }

    fun addAll(array: Array) {
        values.addAll(array.values)
    }
}