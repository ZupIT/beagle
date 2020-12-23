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

import android.app.Application
import android.view.View
import androidx.test.core.app.ApplicationProvider
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.setup.BeagleSdk
import com.facebook.yoga.YogaNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.After
import org.junit.Before

open class BaseSoLoaderTest : BaseTest() {

    protected val application = ApplicationProvider.getApplicationContext() as Application


    @Before
    fun setup() {
        mockYoga()
        BeagleSdk.setInTestMode()
        MyBeagleSetup().init(application)
    }

    @After
    fun teardown() {
        BeagleSdk.deinitForTest()
    }

    private fun mockYoga() {
        val yogaNode = mockk<YogaNode>(relaxed = true, relaxUnitFun = true)
        val view = View(application)
        mockkStatic(YogaNode::class)
        every { YogaNode.create() } returns yogaNode
        every { yogaNode.data } returns view
    }
}
