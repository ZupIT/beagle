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

package br.com.zup.beagle.android.jsonpath

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JsonCreateTreeTest {

    @Test
    fun `should create sequence of array when not existing`() {
        val result = JsonCreateTree().walkingTreeAndFindKey(JSONArray(),
            JsonPathUtils.splitKeys("[0].[1].[0].[2].[3]"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_ARRAY_WITH_NO_VALUES.toString(), result.toString())
    }

    @Test
    fun `should create sequence of array when an existing a sequence`() {
        val result = JsonCreateTree().walkingTreeAndFindKey(COMPLEX_JSON_ARRAY_WITH_VALUES,
            JsonPathUtils.splitKeys("[0].[1].[0].[2].[3]"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_ARRAY_WITH_VALUES_RESULT.toString(), result.toString())
    }

    @Test
    fun `should create sequence of object when not existing sequence`() {
        val result = JsonCreateTree().walkingTreeAndFindKey(JSONObject(),
            JsonPathUtils.splitKeys("address.city.name.city"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_OBJECT_WITH_NO_VALUES.toString(), result.toString())
    }

    @Test
    fun `should create sequence of object when existing a sequence`() {
        val result = JsonCreateTree().walkingTreeAndFindKey(COMPLEX_JSON_OBJECT_WITH_VALUES,
            JsonPathUtils.splitKeys("address.city.name"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_OBJECT_WITH_VALUES_RESULT.toString(), result.toString())
    }

    @Test
    fun `should create sequence of object when current tree is array`() {
        val result = JsonCreateTree().walkingTreeAndFindKey(JSONArray(),
            JsonPathUtils.splitKeys("address.city.name.city"), JSONObject.NULL)

        assertEquals(COMPLEX_JSON_OBJECT_WITH_NO_VALUES.toString(), result.toString())
    }

    @Test
    fun `should not change sequence of object when passing the path existing`() {
        val result = JsonCreateTree().walkingTreeAndFindKey(COMPLEX_JSON_OBJECT_WITH_ARRAY,
            JsonPathUtils.splitKeys("context.name[2].d[0].e[5]"), "teste")

        assertEquals(COMPLEX_JSON_OBJECT_WITH_ARRAY.toString(), result.toString())
    }
}