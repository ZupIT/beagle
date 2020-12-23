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

package br.com.zup.beagle.test.rules

import android.app.Application
import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaNode
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.runners.model.Statement

internal class BeagleComponentsStatement(private val base: Statement?) : Statement() {
    override fun evaluate() {
        setUp()
        try {
            base?.evaluate()
        } finally {
            tearDown()
        }
    }

    private fun setUp() {
        createYogaNodeMocks()
        configureSoLoader()
    }

    private fun tearDown() {
        SoLoader.deinitForTest()
    }

    private fun configureSoLoader(){
        SoLoader.setInTestMode()
    }

    private fun createYogaNodeMocks(){
        mockkStatic(YogaNode::class)

        val application = ApplicationProvider.getApplicationContext() as Application
        val yogaNode = mockk<YogaNode>(relaxed = true, relaxUnitFun = true)
        val view = View(application)

        every { YogaNode.create() } returns yogaNode
        every { yogaNode.data } returns view
    }

}