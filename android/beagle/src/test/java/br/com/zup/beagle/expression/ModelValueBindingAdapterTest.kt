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

import android.content.Context
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.expression.cache.CacheProvider
import br.com.zup.beagle.expression.config.BindingSetup
import br.com.zup.beagle.testutil.CoroutineTestRule
import br.com.zup.beagle.widget.core.WidgetView
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.Array
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ModelValueBindingAdapterTest : BaseTest(){
    @InjectMockKs
    private lateinit var modelValueBindingAdapter: ModelValueBindingAdapter

    @RelaxedMockK
    private lateinit var value: Value

    private val dataBindingComponent: WidgetView = WidgetBindingSampleTest(
        intValue = BindingExpr(expression = "@{b}"),
        stringValue = BindingExpr("@{a}")
    )

    private val modelJson = """
        {
  "a" : "Beagle 2",
  "b" : 2
}
"""
    @RelaxedMockK
    private lateinit var context: Context

    private val modelPath = "/modelPath"

    @get:Rule
    val scope = CoroutineTestRule()

    private val bindingConfig = mockk<BindingSetup.BindingConfig>(relaxed = true)

    private val cacheProvider = mockk<CacheProvider<String, Binding>>(relaxed = true)

    @Before
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { cacheProvider.get(any()) } returns null
        every { bindingConfig.cacheProvider } returns cacheProvider
        mockkObject(BindingSetup)
        every { BindingSetup.bindingConfig } returns bindingConfig
        BindingSetup.bindingConfig = BindingSetup.bindingConfig
    }

    @After
    override fun tearDown() {
        super.tearDown()
        Dispatchers.resetMain()
        unmockkObject(BindingSetup)
    }

    @Test
    fun should_evaluateBinding_with_value_for_success() {
        dataBindingComponent.modelJson = modelJson
        val view = dataBindingComponent.toView(context)
        modelValueBindingAdapter.evaluateBinding(dataBindingComponent)

        assertEquals("2 - Beagle 2", (view as WidgetBindingSampleTextView).currentText)
    }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> toTypedKotlinArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }
}