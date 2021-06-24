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

package br.com.zup.beagle.android.action

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.evaluateExpression
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an Add Children")
class AddChildrenTest : BaseTest() {

    private val serverDrivenComponent = mockk<ServerDrivenComponent>(relaxed = true)
    private val value = listOf(serverDrivenComponent)
    private val origin = mockk<View>(relaxed = true)
    private val viewGroup = mockk<ViewGroup>(relaxed = true)
    private val context = mockk<AppCompatActivity>(relaxed = true)
    private val view = mockk<BeagleFlexView>(relaxed = true)
    private val id = "id"

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkObject(ViewFactory)
        mockkObject(BeagleMessageLogs)

        mockkStatic("br.com.zup.beagle.android.utils.ActionExtensionsKt")

        every { beagleSdk.logger } returns mockk(relaxed = true)
        every { rootView.getContext() } returns context
        every { context.findViewById<ViewGroup>(id.toAndroidId()) } returns viewGroup
        every { ViewFactory.makeBeagleFlexView(rootView) } returns view
        every { viewGroup.addView(any()) } just Runs
    }


    @DisplayName("When call execute with mode APPEND")
    @Nested
    inner class ModeAppendTest {

        @DisplayName("Then should execute action correctly")
        @Test
        fun testModeAppend() {
            // Given
            val action = AddChildren(id, value, Mode.APPEND)

            // When
            action.execute(rootView, origin)

            // Then
            verify(exactly = 1) { viewGroup.addView(view) }
        }
    }

    @DisplayName("When call execute with mode REPLACE")
    @Nested
    inner class ModeReplaceTest {

        @DisplayName("Then should execute action correctly")
        @Test
        fun testModeReplace() {
            // Given
            val action = AddChildren(id, value, Mode.REPLACE)

            // When
            action.execute(rootView, origin)

            // Then
            verify(exactly = 1) {
                viewGroup.removeAllViews()
                viewGroup.addView(view)
            }
        }
    }

    @DisplayName("When call execute with mode PREPEND")
    @Nested
    inner class ModePrependTest {

        @DisplayName("Then should execute action correctly")
        @Test
        fun testModePrepend() {
            // Given
            val action = AddChildren(id, value, Mode.PREPEND)

            // When
            action.execute(rootView, origin)

            // Then
            verify(exactly = 1) { viewGroup.addView(view, 0) }
        }
    }

    @DisplayName("When call execute with invalid component id")
    @Nested
    inner class WrongViewTest {

        @DisplayName("Then should emit log exception")
        @Test
        fun testLogException() {
            // Given
            val action = AddChildren("test", value, Mode.PREPEND)

            // When
            action.execute(rootView, origin)

            // Then
            verify(exactly = 1) { BeagleMessageLogs.errorWhileTryingToAddViewWithAddChildrenAction("test") }
        }
    }

    @DisplayName("When call execute with expression value")
    @Nested
    inner class ExpressionValueTest {

        @DisplayName("Then should execute action correctly")
        @Test
        fun testExpressionValue() {
            // Given
            val action = AddChildren(id, expressionOf("@{test}"), Mode.APPEND)

            val list = arrayListOf(linkedMapOf(Pair("_beagleComponent_", " beagle:container")))

            every { action.evaluateExpression(rootView, origin, action.value) } returns list as List<ServerDrivenComponent>

            // When
            action.execute(rootView, origin)

            // Then
            verify(exactly = 1) { viewGroup.addView(view) }
        }
    }
}
