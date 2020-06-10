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

package br.com.zup.beagle.android

import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.BeagleSdk
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    protected val beagleSdk = mockk<BeagleSdk>(relaxed = true)

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)
        every { BeagleEnvironment.beagleSdk } returns beagleSdk
    }

    @After
    open fun tearDown() {
        unmockkObject(BeagleEnvironment)
    }
}
