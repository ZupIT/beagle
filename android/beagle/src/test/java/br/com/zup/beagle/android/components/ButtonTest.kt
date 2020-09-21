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
import android.support.v7.widget.AppCompatButton
import android.view.View
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
import br.com.zup.beagle.android.view.ViewFactory
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

private val DEFAULT_TEXT = Bind.Value("Hello")
private const val DEFAULT_STYLE = "DummyStyle"
private val BUTTON_STYLE = RandomData.int()

class ButtonTest : BaseComponentTest() {

    private val analytics: Analytics = mockk(relaxed = true)
    private val context: Context = mockk()
    private val button: AppCompatButton = mockk(relaxed = true, relaxUnitFun = true)

    private val listener = slot<View.OnClickListener>()

    private lateinit var buttonComponent: Button

    override fun setUp() {
        super.setUp()

        mockkConstructor(PreFetchHelper::class)
        mockkConstructor(StyleManager::class)

        every { beagleSdk.analytics } returns analytics

        every { button.setOnClickListener(capture(listener)) } just Runs
        every { button.context } returns context

        every { anyConstructed<ViewFactory>().makeButton(any(), BUTTON_STYLE) } returns button
        every { anyConstructed<PreFetchHelper>().handlePreFetch(any(), any<List<Action>>()) } just Runs
        every { anyConstructed<StyleManager>().getButtonStyle(any()) } returns BUTTON_STYLE

        every { BeagleEnvironment.application } returns mockk(relaxed = true)

        buttonComponent = Button(DEFAULT_TEXT, styleId = DEFAULT_STYLE)
    }

    @Test
    fun build_should_call_prefetch_when_action_not_null() {
        // Given
        val action = Navigate.PopView()
        buttonComponent = buttonComponent.copy(onPress = listOf(action))

        // When
        buttonComponent.buildView(rootView)

        // Then
        verify(exactly = once()) { anyConstructed<PreFetchHelper>().handlePreFetch(rootView, listOf(action)) }
    }

    @Test
    fun build_should_return_a_button_instance() {
        // When
        val view = buttonComponent.buildView(rootView)

        // Then
        assertTrue(view is AppCompatButton)
    }

    @Test
    fun build_should_setOnClickListener() {
        // When
        buttonComponent.buildView(rootView)

        // Then
        verify(exactly = once()) { button.setOnClickListener(any()) }
    }

    @Test
    fun should_call_analytics_when_button_clicked_and_click_event_presented() {
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
    fun should_not_call_analytics_when_click_event_not_presented() {
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
