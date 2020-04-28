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

import br.com.zup.beagle.core.DataBindingComponent
import br.com.zup.beagle.data.BeagleServiceWrapper
import br.com.zup.beagle.data.FetchDataListener
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.slot
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException
import kotlin.test.assertEquals
import kotlin.test.fail

class ModelValueHelperTest {

    @InjectMockKs
    private lateinit var modelValueHelper: ModelValueHelper

    @RelaxedMockK
    private lateinit var dataBindingComponent: DataBindingComponent

    @RelaxedMockK
    private lateinit var beagleService: BeagleServiceWrapper

    @RelaxedMockK
    private lateinit var jsonParser: JsonParser

    private val modelPath = "/modelPath"

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

    @RelaxedMockK
    private lateinit var value: Value

    private val slotFetchDataListener = slot<FetchDataListener>()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        every { beagleService.fetchData(any(), capture(slotFetchDataListener)) } just Runs
        every { dataBindingComponent.modelPath } returns modelPath
        every { jsonParser.parseJsonToValue(any()) } returns value
    }

    @Test
    fun should_fetch_model_value_for_success() {
        modelValueHelper.fetchModelValue(onSuccess = {
            assertEquals(value, it)
        }, onError = {
            fail("should fetchModelValue")
        })
        slotFetchDataListener.captured.onSuccess(JSON)
    }

    @Test
    fun should_fetch_model_value_for_error() {
        val exception = RuntimeException("Error")
        modelValueHelper.fetchModelValue(onSuccess = {
            fail("should fail")
        }, onError = {
            assertEquals(exception, it)
        })
        slotFetchDataListener.captured.onError(exception)
    }
}