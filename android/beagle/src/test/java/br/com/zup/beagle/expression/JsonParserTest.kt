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

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class JsonParserTest {

    private val JSON = """{ "a": 
	{ "b": 
		{
		"c": 10, 
		"d": "ola",
		"e": [{"f": 15, "k": "18"}],
		"g": ["ola mundo", 2.5, true],
        "h": "11",
        "i": [{"j": "16"}]
		}
	}
}"""

    private val JSON2 = "DUMMY"

    private val JSON_ARRAY = """
        [ {
  "a" : "Remote Beagle 1",
  "b" : 1
}, {
  "a" : "Remote Beagle 2",
  "b" : 2
}, {
  "a" : "Remote Beagle 3",
  "b" : 3
}, {
  "a" : "Remote Beagle 4",
  "b" : 4
}, {
  "a" : "Remote Beagle 5",
  "b" : 5
}]
        """

    @InjectMockKs
    private lateinit var jsonParser: JsonParser

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun should_parse_json_for_success() {
        val parsedValue = jsonParser.parseJsonToValue(JSON)

        assertTrue { parsedValue.isObject() }
        val propertyA = parsedValue.getAsObject()["a"]
        assertTrue { propertyA?.isObject() == true }
        val propertyB = propertyA?.getAsObject()?.get("b")
        assertTrue { propertyB?.isObject() == true }
        val propertyC = propertyB?.getAsObject()?.get("c")
        assertTrue { propertyC?.isPrimitive() == true }
        assertTrue { propertyC?.getAsNumber() == 10 }
        val propertyD = propertyB?.getAsObject()?.get("d")
        assertTrue { propertyD?.isPrimitive() == true }
        assertTrue { propertyD?.getAsString() == "ola" }
        val propertyE = propertyB?.getAsObject()?.get("e")
        assertTrue { propertyE?.isArray() == true }
        assertEquals(propertyE?.getAsArray()?.size(), 1)
        val propertyG = propertyB?.getAsObject()?.get("g")
        assertTrue { propertyG?.isArray() == true }
        assertEquals(propertyE?.getAsArray()?.size(), 1)
        val propertyH = propertyB?.getAsObject()?.get("h")
        assertTrue { propertyH?.isPrimitive() == true }
        assertEquals(propertyH?.getAsString(), "11")
        val propertyI = propertyB?.getAsObject()?.get("i")
        assertEquals(propertyI?.getAsArray()?.size(), 1)
    }

    @Test(expected = IllegalStateException::class)
    fun should_parse_json_for_exception_not_array() {
        jsonParser.parseJsonToArray(JSON)
        fail("Should throw IllegalStateException")
    }

    @Test(expected = IllegalStateException::class)
    fun should_parse_json_for_exception_not_json_object() {
        jsonParser.parseJsonToValue(JSON2)
        fail("Should throw IllegalStateException")
    }

    @Test
    fun should_parse_json_for_array_success() {
        val parsedValue = jsonParser.parseJsonToArray(JSON_ARRAY)
        assertTrue { parsedValue.isArray() }
        assertEquals(5, parsedValue.getAsArray().size())
    }
}