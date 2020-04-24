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

package br.zup.com.beagle.expression

import br.com.zup.beagle.expression.Binding
import br.com.zup.beagle.expression.cache.CacheProvider
import br.com.zup.beagle.expression.config.BindingSetup
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before

abstract class BaseTest {


    private val bindingConfig = mockk<BindingSetup.BindingConfig>(relaxed = true)

    private val cacheProvider = mockk<CacheProvider<String, Binding>>(relaxed = true)

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)

        every { cacheProvider.get(any()) } returns null
        every { bindingConfig.cacheProvider } returns cacheProvider
        mockkObject(BindingSetup)
        every { BindingSetup.bindingConfig } returns bindingConfig
        BindingSetup.bindingConfig = BindingSetup.bindingConfig
    }

    @After
    open fun tearDown() {
        unmockkObject(BindingSetup)
    }
}