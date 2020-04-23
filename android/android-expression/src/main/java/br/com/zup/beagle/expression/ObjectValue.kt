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

import java.util.HashMap

/**
 * This class is based on flipkart-incubator/proteus's implementation
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/value/ObjectValue.java}
 * @author hernandazevedozup
 */
class ObjectValue : Value() {

    private val properties = HashMap<String, Value>()

    fun add(property: String, value: Value?) {
        var v = value
        if (v == null) {
            v = Null.INSTANCE
        }
        properties[property] = v
    }

    override fun copy(): ObjectValue {
        val result = ObjectValue()
        for ((key, value) in properties) {
            result[key] = value.copy()
        }
        return result
    }

    operator fun set(property: String, value: Value) {
        properties[property] = value
    }

    fun remove(property: String): Value? {
        return properties.remove(property)
    }

    fun addProperty(property: String, value: String) {
        set(property, createBeagleValue(value))
    }

    fun addProperty(property: String, value: Number) {
        set(property, createBeagleValue(value))
    }

    fun addProperty(property: String, value: Boolean) {
        set(property, createBeagleValue(value))
    }

    fun addProperty(property: String, value: Char) {
        set(property, createBeagleValue(value))
    }

    private fun createBeagleValue(value: Any): Value {
        return Primitive(value)
    }

//    fun entrySet(): Set<Entry<String, Value>> {
//        return properties.entries
//    }

    /**
     * Returns the number of key/value pairs in the object.
     *
     * @return the number of key/value pairs in the object.
     */
    fun size(): Int {
        return properties.size
    }

    /**
     * Convenience method to check if a member with the specified name is present in this object.
     *
     * @param memberName name of the member that is being checked for presence.
     * @return true if there is a member with the specified name, false otherwise.
     */
    fun has(memberName: String): Boolean {
        return properties.containsKey(memberName)
    }

    fun isSimpleValue(memberName: String): Boolean {
        return has(memberName) && get(memberName)!!.isPrimitive()
    }

    fun isBoolean(memberName: String): Boolean {
        return if (has(memberName) && get(memberName)!!.isPrimitive()) {
            getAsSimpleValue(memberName).isBoolean()
        } else false
    }

    fun isNumber(memberName: String): Boolean {
        return if (has(memberName) && get(memberName)!!.isPrimitive()) {
            getAsSimpleValue(memberName).isNumber()
        } else false
    }

    fun isComplexValue(memberName: String): Boolean {
        return has(memberName) && get(memberName)!!.isObject()
    }

    fun isArrayValue(memberName: String): Boolean {
        return has(memberName) && get(memberName)!!.isArray()
    }

    operator fun get(memberName: String): Value? {
        return properties[memberName]
    }

    fun getAsSimpleValue(memberName: String): Primitive {
        return properties[memberName] as Primitive
    }

    fun getAsBoolean(memberName: String): Boolean? {
        return if (isBoolean(memberName)) {
            getAsSimpleValue(memberName).getAsBoolean()
        } else null
    }

    fun getAsBoolean(memberName: String, defaultBeagleValue: Boolean): Boolean {
        return if (isBoolean(memberName)) {
            getAsSimpleValue(memberName).getAsBoolean()
        } else defaultBeagleValue
    }

    fun getAsInteger(memberName: String): Int? {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsInt()
        } else null
    }

    fun getAsInteger(memberName: String, defaultBeagleValue: Int): Int {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsInt()
        } else defaultBeagleValue
    }

    fun getAsFloat(memberName: String): Float? {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsDouble().toFloat()
        } else null
    }

    fun getAsFloat(memberName: String, defaultBeagleValue: Float): Float {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsDouble().toFloat()
        } else defaultBeagleValue
    }

    fun getAsDouble(memberName: String): Double? {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsDouble()
        } else null
    }

    fun getAsDouble(memberName: String, defaultBeagleValue: Double): Double {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsDouble()
        } else defaultBeagleValue
    }

    fun getAsLong(memberName: String): Long? {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsInt().toLong()
        } else null
    }

    fun getAsLong(memberName: String, defaultBeagleValue: Long): Long {
        return if (isNumber(memberName)) {
            getAsSimpleValue(memberName).getAsInt().toLong()
        } else defaultBeagleValue
    }

    fun getAsString(memberName: String): String? {
        return if (isSimpleValue(memberName)) {
            getAsSimpleValue(memberName).getAsString()
        } else null
    }

    fun getAsArray(memberName: String): Array {
        return properties.get(memberName) as Array
    }

    fun getAsComplexValue(memberName: String): ObjectValue {
        return if (isComplexValue(memberName)) {
            properties[memberName] as ObjectValue
        } else throw IllegalStateException("It is not a Value")
    }

    override fun equals(other: Any?): Boolean {
        return other === this || other is ObjectValue && other.properties == properties
    }

    override fun hashCode(): Int {
        return properties.hashCode()
    }
}