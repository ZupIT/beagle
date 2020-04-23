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

import java.util.regex.Pattern
import android.util.LruCache
import java.util.Arrays
import java.util.StringTokenizer

/**
 * This class is based on flipkart-incubator/proteus's implementation
 * link @{link https://github.com/flipkart-incubator/proteus/blob/master/proteus-core/src/main/java/com/flipkart/android/proteus/value/Binding.java}
 * @author hernandazevedozup
 */
const val EMPTY = ""
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
abstract class Binding : Value(){
    companion object {

        val BINDING_PREFIX_0 = '@'
        val BINDING_PREFIX_1 = '{'
        val BINDING_SUFFIX = '}'

        val INDEX = "\$index"

        val ARRAY_DATA_LENGTH_REFERENCE = "\$length"
        val ARRAY_DATA_LAST_INDEX_REFERENCE = "\$last"

        val BINDING_PATTERN =
            Pattern.compile("@\\{fn:(\\S+?)\\(((?:(?<!\\\\)'.*?(?<!\\\\)'|.?)+)\\)\\}|@\\{(.+)\\}")

        val DATA_PATH_DELIMITERS = ".]"

        val DELIMITER_OBJECT = '.'
        val DELIMITER_ARRAY_OPENING = '['
        val DELIMITER_ARRAY_CLOSING = ']'

        fun isBindingValue(value: String): Boolean {
            return (value.length > 3
                && value[0] == BINDING_PREFIX_0
                && value[1] == BINDING_PREFIX_1
                && value[value.length - 1] == BINDING_SUFFIX)
        }

        fun valueOf(value: String): Binding {
            val matcher = BINDING_PATTERN.matcher(value)
            return if (matcher.find()) {
                if (matcher.group(3) != null) { // It is data binding
                    DataBinding.valueOf(matcher.group(3))
                } else {
                    throw IllegalArgumentException("$value is not a binding")
                }
            } else {
                throw IllegalArgumentException("$value is not a binding")
            }
        }
    }
    override fun copy(): Value = this

    abstract fun evaluate(data: Value): Value

    internal class DataBinding(val tokens: kotlin.Array<Token>) : Binding() {

        companion object {
            private val DATA_BINDING_CACHE = LruCache<String, DataBinding>(64)

            fun assign(tokens: kotlin.Array<Token>, value: Value, data: Value, dataIndex: Int = 0) {
                var current = data
                var token: Token
                var index = dataIndex

                for (i in 0 until tokens.size - 1) {
                    token = tokens[i]
                    if (token.isArrayIndex) {
                        try {
                            index = getArrayIndex(token.value, dataIndex)
                        } catch (e: NumberFormatException) {
                            return
                        }

                        current = getArrayItem(current.getAsArray(), index, token.isArray)
                    } else if (token.isArray) {
                        current = getArray(current, token.value, index)
                    } else {
                        current = getObject(current, token, index)
                    }
                }

                token = tokens[tokens.size - 1]

                if (token.isArrayIndex) {
                    try {
                        index = getArrayIndex(token.value, dataIndex)
                    } catch (e: NumberFormatException) {
                        return
                    }

                    getArrayItem(current.getAsArray(), index, false)
                    current.getAsArray().remove(index)
                    current.getAsArray().add(index, value)
                } else {
                    current.getAsObject().add(token.value, value)
                }
            }

            private fun getObject(parent: Value, token: Token, index: Int): Value {
                val temp: Value?
                val `object`: ObjectValue
                if (parent.isArray()) {
                    temp = parent.getAsArray()[index]
                    if (temp.isObject()) {
                        `object` = temp.getAsObject()
                    } else {
                        `object` = ObjectValue()
                        parent.getAsArray().remove(index)
                        parent.getAsArray().add(index, `object`)
                    }
                } else {
                    temp = parent.getAsObject()[token.value]
                    if (temp != null && temp.isObject()) {
                        `object` = temp.getAsObject()
                    } else {
                        `object` = ObjectValue()
                        parent.getAsObject().add(token.value, `object`)
                    }

                }
                return `object`
            }

            private fun getArray(parent: Value, token: String, index: Int): Array {
                val temp: Value?
                val array: Array
                if (parent.isArray()) {
                    temp = parent.getAsArray()[index]
                    if (temp.isArray()) {
                        array = temp.getAsArray()
                    } else {
                        array = Array()
                        parent.getAsArray().remove(index)
                        parent.getAsArray().add(index, array)
                    }
                } else {
                    temp = parent.getAsObject()[token]
                    if (temp != null && temp.isArray()) {
                        array = temp.getAsArray()
                    } else {
                        array = Array()
                        parent.getAsObject().add(token, array)
                    }
                }
                return array
            }

            private fun getArrayItem(array: Array, index: Int, isArray: Boolean): Value {
                if (index >= array.size()) {
                    while (array.size() < index) {
                        array.add(Null.INSTANCE)
                    }
                    if (isArray) {
                        array.add(Array())
                    } else {
                        array.add(ObjectValue())
                    }
                }
                return array.get(index)
            }

            @Throws(NumberFormatException::class)
            private fun getArrayIndex(token: String, dataIndex: Int): Int {
                val index: Int
                if (INDEX == token) {
                    index = dataIndex
                } else {
                    index = Integer.parseInt(token)
                }
                return index
            }
            private fun correctPreviousToken(tokensList: kotlin.Array<Token>): kotlin.Array<Token> {
                var tokens = tokensList
                val previous = tokens[tokens.size - 1]
                val index = previous.value.indexOf(DELIMITER_ARRAY_OPENING)
                val prefix = previous.value.substring(0, index)
                val suffix = previous.value.substring(index + 1, previous.value.length)

                if (prefix == EMPTY) {
                    val token = tokens[tokens.size - 1]
                    tokens[tokens.size - 1] = Token(token.value, true, false)
                } else {
                    tokens[tokens.size - 1] = Token(prefix, true, false)
                }

                tokens = Arrays.copyOf(tokens, tokens.size + 1)
                tokens[tokens.size - 1] = Token(suffix, false, true)

                return tokens
            }

            private fun resolve(tokens: kotlin.Array<Token>, data: Value, indexParam: Int): Result {
                var index = indexParam
                // replace INDEX with index value
                if (tokens.size == 1 && INDEX == tokens[0].value) {
                    return Result.success(Primitive(index.toString()))
                } else {
                    var elementToReturn: Value? = data
                    var tempElement: Value?
                    var tempArray: Array

                    for (i in tokens.indices) {
                        val segment = tokens[i].value
                        if (elementToReturn == null) {
                            return Result.NO_SUCH_DATA_PATH_EXCEPTION
                        }
                        if (elementToReturn.isNull()) {
                            return Result.NULL_EXCEPTION
                        }
                        if ("" == segment) {
                            continue
                        }
                        if (elementToReturn.isArray()) {
                            tempArray = elementToReturn.getAsArray()

                            if (INDEX == segment) {
                                if (index < tempArray.size()) {
                                    elementToReturn = tempArray[index]
                                } else {
                                    return Result.NO_SUCH_DATA_PATH_EXCEPTION
                                }
                            } else if (ARRAY_DATA_LENGTH_REFERENCE == segment) {
                                elementToReturn = Primitive(tempArray.size())
                            } else if (ARRAY_DATA_LAST_INDEX_REFERENCE == segment) {
                                if (tempArray.size() == 0) {
                                    return Result.NO_SUCH_DATA_PATH_EXCEPTION
                                }
                                elementToReturn = tempArray[tempArray.size() - 1]
                            } else {
                                try {
                                    index = Integer.parseInt(segment)
                                } catch (e: NumberFormatException) {
                                    return Result.INVALID_DATA_PATH_EXCEPTION
                                }

                                if (index < tempArray.size()) {
                                    elementToReturn = tempArray.get(index)
                                } else {
                                    return Result.NO_SUCH_DATA_PATH_EXCEPTION
                                }
                            }
                        } else if (elementToReturn.isObject()) {
                            tempElement = elementToReturn.getAsObject().get(segment)
                            if (tempElement != null) {
                                elementToReturn = tempElement
                            } else {
                                return Result.NO_SUCH_DATA_PATH_EXCEPTION
                            }
                        } else return if (elementToReturn.isPrimitive()) {
                            Result.INVALID_DATA_PATH_EXCEPTION
                        } else {
                            Result.NO_SUCH_DATA_PATH_EXCEPTION
                        }
                    }
                    return if (elementToReturn!!.isNull()) {
                        Result.NULL_EXCEPTION
                    } else Result.success(elementToReturn)
                }
            }

            fun valueOf(data: String) : DataBinding {
                var binding: DataBinding? = DATA_BINDING_CACHE.get(data)
                if (null == binding) {
                    val tokenizer = StringTokenizer(data, DATA_PATH_DELIMITERS, true)
                    var tokens = emptyArray<Token>()
                    var token: String
                    var first: Char
                    var length: Int
                    while (tokenizer.hasMoreTokens()) {
                        token = tokenizer.nextToken()
                        length = token.length
                        first = token[0]
                        if (length == 1 && first == DELIMITER_OBJECT) {
                            continue
                        }
                        if (length == 1 && first == DELIMITER_ARRAY_CLOSING) {
                            tokens = correctPreviousToken(tokens)
                            continue
                        }
                        tokens = Arrays.copyOf(tokens, tokens.size + 1)
                        tokens[tokens.size - 1] = Token(token, false, false)
                    }
                    binding = DataBinding(tokens)
                    DATA_BINDING_CACHE.put(data, binding)
                }
                return binding
            }
        }
        override fun evaluate(data: Value): Value {
            val result = resolve(tokens, data, 0)
            return if (result.isSuccess()) result.value else Null.INSTANCE
        }

        fun assign(value: Value, data: Value, index: Int = 0) {
            assign(tokens, value, data, index)
        }

    }

    internal class Token(val value: String, val isArray: Boolean, val isArrayIndex: Boolean) {

        val isBinding = false

        companion object {

            fun getValues(tokens: kotlin.Array<Token>): kotlin.Array<String?> {
                val values = arrayOfNulls<String>(tokens.size)
                for (i in tokens.indices) {
                    values[i] = tokens[i].value
                }
                return values
            }
        }
    }
}