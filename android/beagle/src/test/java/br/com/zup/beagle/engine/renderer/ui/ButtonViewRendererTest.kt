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

package br.com.zup.beagle.engine.renderer.ui

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import androidx.core.widget.TextViewCompat
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.action.ActionExecutor
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.engine.mapper.ViewMapper
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.utils.StyleManager
import br.com.zup.beagle.view.BeagleButtonView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.ui.Button
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertTrue

private const val DEFAULT_TEXT = "Hello"
private const val DEFAULT_STYLE = "DummyStyle"
private val BUTTON_STYLE = RandomData.int()

class ButtonViewRendererTest : BaseTest() {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var context: Context
    @RelaxedMockK
    private lateinit var buttonView: BeagleButtonView
    @RelaxedMockK
    private lateinit var button: Button
    @RelaxedMockK
    private lateinit var analytics: Analytics
    @MockK
    private lateinit var actionExecutor: ActionExecutor
    @MockK
    private lateinit var view: View
    @RelaxedMockK
    private lateinit var styleManager: StyleManager
    @MockK
    private lateinit var viewMapper: ViewMapper
    @RelaxedMockK
    private lateinit var typedArray: TypedArray

    @InjectMockKs
    private lateinit var buttonViewRenderer: ButtonViewRenderer

    override fun setUp() {
        super.setUp()

        mockkStatic(TextViewCompat::class)
        every { beagleSdk.analytics } returns analytics
        every { BeagleEnvironment.application } returns mockk(relaxed = true)
        styleManagerFactory = styleManager

        every { button.style } returns DEFAULT_STYLE
        every { button.text } returns DEFAULT_TEXT
        every { button.action } returns null
        every { actionExecutor.doAction(any(), any()) } just Runs
        every { rootView.getContext() } returns context
        every { viewFactory.makeButton(context) } returns buttonView
        every { TextViewCompat.setTextAppearance(any(), any()) } just Runs
        every { styleManager.getButtonTypedArray(context, any()) } returns typedArray
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }
    
    @Test
    fun build_should_return_a_button_instance() {
        // When
        val view = buttonViewRenderer.build(rootView)

        // Then
        assertTrue(view is BeagleButtonView)
    }

    @Test
    fun setData_with_button_should_call_TextViewCompat_setTextAppearance() {
        // Given
        val isAllCaps = false
        every { typedArray.getBoolean(any(), any()) } returns isAllCaps
        every { styleManager.getButtonStyle(any()) } returns BUTTON_STYLE

        // When
        buttonViewRenderer.build(rootView)

        // Then
        verify(exactly = once()) { buttonView.setOnClickListener(any()) }
        verify { buttonView.background = any() }
        verify(exactly = once()) { buttonView.isAllCaps = isAllCaps }
        verify(exactly = once()) { TextViewCompat.setTextAppearance(buttonView, BUTTON_STYLE) }
    }

    @Test
    fun setData_with_button_should_not_call_TextViewCompat_setTextAppearance_when_designSystem_is_null() {
        // Given
        every { BeagleEnvironment.beagleSdk.designSystem } returns null

        // When
        buttonViewRenderer.build(rootView)

        // Then
        verify(exactly = 0) { TextViewCompat.setTextAppearance(buttonView, BUTTON_STYLE) }
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
        every { button.clickAnalyticsEvent } returns clickAnalyticsEvent
        val onClickListenerSlot = CapturingSlot<View.OnClickListener>()

        // When
        val buttonView = buttonViewRenderer.build(rootView)
        verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
        onClickListenerSlot.captured.onClick(view)

        // Then
        verify { analytics.sendClickEvent(eq(clickAnalyticsEvent)) }
    }

    @Test
    fun should_not_call_analytics_when_click_event_not_presented() {
        // GIVEN
        every { button.clickAnalyticsEvent } returns null
        val onClickListenerSlot = CapturingSlot<View.OnClickListener>()

        // When
        val buttonView = buttonViewRenderer.build(rootView)
        verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
        onClickListenerSlot.captured.onClick(view)

        // Then
        verify(exactly = 0) { analytics.sendClickEvent(any()) }
    }
}
