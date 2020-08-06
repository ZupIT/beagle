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

package br.com.zup.beagle.android.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.zup.beagle.android.context.ContextConstant
import br.com.zup.beagle.android.logger.BeagleLoggerProxy
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.setup.BeagleSdk
import br.com.zup.beagle.android.widget.ActivityRootView
import io.mockk.*
import org.junit.After
import org.junit.Before

abstract class BaseTest {

    protected val rootView = mockk<ActivityRootView>(relaxed = true)
    protected val beagleSdk = mockk<BeagleSdk>(relaxed = true)

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)

        mockkObject(BeagleEnvironment)

        every { BeagleEnvironment.beagleSdk } returns beagleSdk

        ContextConstant.moshi = mockk(relaxed = true)
        ContextConstant.memoryMaximumCapacity = 15
        BeagleLoggerProxy.isLoggingEnabled = true
        BeagleLoggerProxy.logger = mockk(relaxed = true)

    }

    @After
    open fun tearDown() {
        unmockkAll()
    }

    protected fun prepareViewModelMock(viewModel: ViewModel) {
        mockkConstructor(ViewModelProvider::class)
        every { anyConstructed<ViewModelProvider>().get(viewModel::class.java) } returns viewModel
    }
}
