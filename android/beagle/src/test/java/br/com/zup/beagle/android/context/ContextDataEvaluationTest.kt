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

package br.com.zup.beagle.android.context

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.jsonpath.JsonPathFinder
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.testutil.RandomData
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Before

internal class ContextDataEvaluationTest {

    private val jsonPathFinder = mockk<JsonPathFinder>()
    private val contextPathResolver = mockk<ContextPathResolver>()
    private val moshi = mockk<Moshi>()

    private lateinit var contextDataEvaluation: ContextDataEvaluation

    @Before
    fun setUp() {
        contextDataEvaluation = ContextDataEvaluation(
            jsonPathFinder,
            contextPathResolver,
            moshi
        )
    }



    /*@org.junit.Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONOBject() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        val model = mockk<JSONObject>()
        every { jsonPathFinder.find(any(), any()) } returns model

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { bindModel.notifyChange(bindModel) }
    }

    @org.junit.Test
    fun evaluateContextBindings_should_get_value_from_context_and_deserialize_JSONArray() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        val model = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns model

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { bindModel.notifyChange(bindModel) }
    }

    @org.junit.Test
    fun evaluateContextBindings_should_throw_exception_when_moshi_returns_null() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        val model = mockk<JSONArray>()
        every { jsonPathFinder.find(any(), any()) } returns model
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
        verify(exactly = 0) { bindModel.notifyChange(any()) }
    }

    @org.junit.Test
    fun evaluateContextBindings_should_throw_exception_when_jsonPathFinder_returns_null() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        every { moshi.adapter<Any>(any<Class<*>>()).fromJson(any<String>()) } returns null
        every { jsonPathFinder.find(any(), any()) } returns null

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify(exactly = once()) { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
        verify(exactly = 0) { bindModel.notifyChange(any()) }
    }

    @org.junit.Test
    fun evaluateContextBindings_should_throw_exception_when_trying_to_call_jsonPathFinder() {
        // Given
        val contextData = ContextData("$CONTEXT_ID.a", RandomData.string())
        contexts[CONTEXT_ID] = ContextBinding(contextData, mutableListOf(bindModel))
        every { jsonPathFinder.find(any(), any()) } throws IllegalStateException()

        // When
        contextDataManager.evaluateAllContext()

        // Then
        verify { BeagleMessageLogs.errorWhileTryingToAccessContext(any()) }
        verify { BeagleMessageLogs.errorWhileTryingToNotifyContextChanges(any()) }
        verify(exactly = 0) { bindModel.notifyChange(any()) }
    }*/
}