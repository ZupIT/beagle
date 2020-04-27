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

import br.com.zup.beagle.android.expression.BuildConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParser {
    //FIXME known bug for the json below
//    private val JSON2 = """{
//	"a": [
//		[{
//			"b": 15
//		}],
//		[{
//			"c": true
//		}]
//	]
//}"""

    /**
     * Parses the json to a {@link Value} with the same structure/type of fields
     */
    fun parseJsonToValue(jsonString: String): Value {
        try {
            return fillProperties(JSONObject(jsonString))
        } catch (e: JSONException) {
            throw IllegalStateException("Error parsing jsonString to a valid JSON object", e)
        }
    }

    /**
     * Parses the json to a {@link Array} with the same structure/type of fields
     */
    fun parseJsonToArray(jsonString: String): Array {
        try {
            return fillProperties(JSONArray(jsonString)).getAsArray()
        } catch (e: JSONException) {
            throw IllegalStateException("Error parsing jsonString to a valid JSON object", e)
        }
    }

    private fun fillProperties(input: Any): Value {
        if (input is JSONObject) {
            return fillObjectValue(input)
        } else {
            return fillJsonArray(input as JSONArray)
        }
    }

    private fun fillJsonArray(input: JSONArray): Array {
        val objectArray = Array()

        for (i in 0 until input.length()) {
            when (val value = input.get(i)) {
                is JSONArray -> objectArray.add(fillProperties(JSONArray(input.toString())))
                is JSONObject -> {
                    val a = input.getJSONObject(i)
                    objectArray.add(fillProperties(a))
                }
                else -> {
                    printInDebug(value)
                    objectArray.add(Primitive(parseType(value)))
                }
            }
        }

        return objectArray
    }

    private fun fillObjectValue(input: JSONObject): ObjectValue {
        val objectValue = ObjectValue()
        val keys = input.keys()
        while (keys.hasNext()) {
            val key = keys.next() as String
            if (input.get(key) !is JSONArray) {
                val innerInput = input.get(key)
                if (innerInput is JSONObject) {
                    objectValue.add(key, fillProperties(innerInput))
                } else {
                    printInDebug("$key=$innerInput")
                    objectValue.add(key, Primitive(parseType(innerInput)))
                }
            } else {
                printInDebug("$key=[")
                objectValue.add(key, fillProperties(JSONArray(input.get(key).toString())))
                printInDebug("]")
            }
        }

        return objectValue
    }

    private fun printInDebug(str: Any) {
        if (BuildConfig.DEBUG) {
            println(str.toString())
        }
    }

    private fun parseType(input: Any): Any {
        return input
    }

}