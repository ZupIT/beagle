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

package br.com.zup.beagle.android.components

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.data.PreFetchHelper
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.StyleManager
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.view.ViewFactory
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private const val DEFAULT_STYLE = "DummyStyle"

@DisplayName("Given a Button")
internal class ButtonTest : BaseComponentTest() {

    private val buttonStyle = RandomData.int()
    private val defaultText = Bind.Value("Hello")
    private val analytics: Analytics = mockk(relaxed = true)
    private val context: Context = mockk()
    private val button: AppCompatButton = mockk(relaxed = true, relaxUnitFun = true)
    private val listener = slot<View.OnClickListener>()

    private lateinit var buttonComponent: Button

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkConstructor(PreFetchHelper::class)
        mockkConstructor(StyleManager::class)

        every { beagleSdk.analytics } returns analytics

        every { button.setOnClickListener(capture(listener)) } just Runs
        every { button.context } returns context

        every { ViewFactory.makeButton(any(), buttonStyle) } returns button
        every { ViewFactory.makeButton(any()) } returns button
        every { anyConstructed<PreFetchHelper>().handlePreFetch(any(), any<List<Action>>()) } just Runs
        every { anyConstructed<StyleManager>().getButtonStyle(DEFAULT_STYLE) } returns buttonStyle

        every { BeagleEnvironment.application } returns mockk(relaxed = true)

        buttonComponent = Button(defaultText, styleId = DEFAULT_STYLE)
    }

    @DisplayName("When set the configurations for Button")
    @Nested
    inner class ButtonConfigurations {

        @Test
        @DisplayName("Then should build a Button")
        fun testBuildButtonWithoutStyle() {
            // Given
            buttonComponent = Button(defaultText)

            // When
            val view = buttonComponent.buildView(rootView)

            // Then
            verify {
                ViewFactory.makeButton(any())
            }
            assertTrue(view is AppCompatButton)
        }

        @Test
        @DisplayName("Then should build a Button")
        fun buildButtonInstance() {
            // When
            val view = buttonComponent.buildView(rootView)

            // Then
            assertTrue(view is AppCompatButton)
            verify {
                ViewFactory.makeButton(any(), buttonStyle)
            }
        }

        @Test
        @DisplayName("Then should build a ClickListener on Button")
        fun buildClickListenerInstance() {
            // When
            buttonComponent.buildView(rootView)

            // Then
            verify(exactly = once()) { button.setOnClickListener(any()) }
        }

        @Test
        @DisplayName("Then check if prefetch was called")
        fun verifyPrefetch() {
            // Given
            val action = Navigate.PopView()
            buttonComponent = buttonComponent.copy(onPress = listOf(action))

            // When
            buttonComponent.buildView(rootView)

            // Then
            verify(exactly = once()) { anyConstructed<PreFetchHelper>().handlePreFetch(rootView, listOf(action)) }
        }
    }

    @DisplayName("When set enabled")
    @Nested
    inner class IsEnabledTest {

        @Test
        @DisplayName("Then should not call field")
        fun testIsEnabledNull() {
            // When
            buttonComponent.buildView(rootView)

            // Then
            verify(exactly = 0) { button.isEnabled = any() }
        }

        @Test
        @DisplayName("Then should set field enabled with true")
        fun testIsEnabledFalse() {
            // Given
            buttonComponent = buttonComponent.copy(enabled = Bind.Value(true))

            // When
            buttonComponent.buildView(rootView)

            // Then
            verify(exactly = 1) { button.isEnabled = true }
        }

        @Test
        @DisplayName("Then should call set field enabled with false")
        fun testIsEnabledTrue() {
            // Given
            buttonComponent = buttonComponent.copy(enabled = Bind.Value(false))

            // When
            buttonComponent.buildView(rootView)

            // Then
            verify(exactly = 1) { button.isEnabled = false }
        }
    }

    @DisplayName("When passing clickAnalyticsEvent and click on button")
    @Nested
    inner class ClickAnalyticsEventTest {

        @Test
        @DisplayName("Then should call analytics with ClickEvent")
        fun testClickAnalyticsEventClickEvent() {
            // GIVEN
            val category = "category"
            val action = "action"
            val value = "value"
            val clickAnalyticsEvent = ClickEvent(
                category,
                action,
                value
            )
            buttonComponent = buttonComponent.copy(clickAnalyticsEvent = clickAnalyticsEvent)
            val onClickListenerSlot = CapturingSlot<View.OnClickListener>()

            // When
            val buttonView = buttonComponent.buildView(rootView)
            verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
            onClickListenerSlot.captured.onClick(view)

            // Then
            verify { analytics.trackEventOnClick(eq(clickAnalyticsEvent)) }
        }

        @Test
        @DisplayName("Then shouldn't call analytics with null")
        fun testClickAnalyticsEventNull() {
            // GIVEN
            val onClickListenerSlot = CapturingSlot<View.OnClickListener>()

            // When
            val buttonView = buttonComponent.buildView(rootView)
            verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
            onClickListenerSlot.captured.onClick(view)

            // Then
            verify(exactly = 0) { analytics.trackEventOnClick(any()) }
        }
    }

    @DisplayName("When clicked")
    @Nested
    inner class ButtonClick {

        @DisplayName("Then it should call handle event")
        @Test
        fun testButtonCLickShouldCallHandleEvent() {
            // GIVEN
            val action: Action = mockk()
            val onClickListenerSlot = CapturingSlot<View.OnClickListener>()
            every { buttonComponent.handleEvent(rootView, view, listOf(action), analyticsValue = "onPress") } just Runs
            buttonComponent.copy(onPress = listOf(action))
            // When
            val buttonView = buttonComponent.buildView(rootView)
            verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
            onClickListenerSlot.captured.onClick(view)

            // Then
            verify(exactly = 0) { buttonComponent.handleEvent(rootView, view, listOf(action), analyticsValue = "OnPress") }
        }
    }
}
