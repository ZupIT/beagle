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

package br.com.zup.beagle.android.components.layout

import android.graphics.Color
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.ToolbarManager
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val DEFAULT_COLOR = 0xFFFFFF

class ScreenComponentTest : BaseComponentTest() {

    private val screenName = "screenName"
    private var screenAnalyticsEvent = ScreenEvent(screenName = screenName)

    @RelaxedMockK
    private lateinit var context: BeagleActivity

    @MockK
    private lateinit var component: ServerDrivenComponent

    @RelaxedMockK
    private lateinit var analytics: Analytics

    private lateinit var screenComponent: ScreenComponent

    override fun setUp() {
        super.setUp()

        mockkStatic(Color::class)

        every { beagleSdk.analytics } returns analytics

        every { Color.parseColor(any()) } returns DEFAULT_COLOR
        every { rootView.getContext() } returns context

        screenComponent = ScreenComponent(navigationBar = null, child = component,
            screenAnalyticsEvent = null, style = null)
    }

    @Test
    fun build_should_create_a_screenWidget_with_grow_1_and_justifyContent_SPACE_BETWEEN() {
        // Given
        val style = slot<Style>()
        every { anyConstructed<ViewFactory>().makeBeagleFlexView(any(), capture(style)) } returns beagleFlexView
        every { context.supportActionBar } returns null

        // When
        screenComponent.buildView(rootView)

        // Then
        assertEquals(1.0, style.captured.flex?.grow)
    }

    @Test
    fun build_should_call_content_builder() {
        // Given
        val style = slot<Style>()
        every { beagleFlexView.addView(view, capture(style)) } just Runs
        every { context.supportActionBar } returns null

        // When
        screenComponent.buildView(rootView)

        // Then
        verify(atLeast = once()) { beagleFlexView.addServerDrivenComponent(component) }
    }

    @Test
    fun build_should_hideNavigationBar_when_navigationBar_is_null() {
        // GIVEN
        val toolbar: Toolbar = mockk(relaxed = true)
        val actionBar: ActionBar = mockk(relaxed = true)

        every { context.supportActionBar } returns actionBar
        every { context.getToolbar() } returns toolbar
        val expected = View.GONE
        every { toolbar.visibility = any() } just Runs
        every { toolbar.visibility } returns expected

        // WHEN
        screenComponent.buildView(rootView)

        // THEN
        assertEquals(expected, toolbar.visibility)
        verify(atLeast = once()) { actionBar.hide() }
    }

    @Test
    fun should_assign_window_attach_callbacks_when_screen_event_presented() {
        // GIVEN
        screenComponent = ScreenComponent(child = component, screenAnalyticsEvent = screenAnalyticsEvent)

        // When
        val view = screenComponent.buildView(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
        verify { view.addOnAttachStateChangeListener(any()) }
    }

    @Test
    fun should_keep_window_attach_callbacks_null_when_screen_event_not_presented() {
        // When
        val view = screenComponent.buildView(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
        verify(exactly = 0) { view.addOnAttachStateChangeListener(any()) }
    }

    @Test
    fun should_call_analytics_when_window_attach_states_changes() {
        // GIVEN
        screenComponent = ScreenComponent(child = component, screenAnalyticsEvent = screenAnalyticsEvent)
        val onAttachStateChangeListenerSlot = CapturingSlot<View.OnAttachStateChangeListener>()

        // When
        val screenView = screenComponent.buildView(rootView)
        verify { screenView.addOnAttachStateChangeListener(capture(onAttachStateChangeListenerSlot)) }
        onAttachStateChangeListenerSlot.captured.onViewAttachedToWindow(view)
        onAttachStateChangeListenerSlot.captured.onViewDetachedFromWindow(view)

        // Then
        val capturedEvent = CapturingSlot<ScreenEvent>()
        verify { analytics.trackEventOnScreenAppeared(capture(capturedEvent)) }
        assertEquals(screenName, capturedEvent.captured.screenName)

        verify { analytics.trackEventOnScreenDisappeared(capture(capturedEvent)) }
        assertEquals(screenName, capturedEvent.captured.screenName)
    }

    @Test
    fun buildView_should_call_configureToolbar_before_configureNavigationBarForScreen() {
        //GIVEN
        val navigationBar = NavigationBar("Stub")
        mockkConstructor(ToolbarManager::class)
        screenComponent = ScreenComponent(child = screenComponent, navigationBar = navigationBar)

        //WHEN
        screenComponent.buildView(rootView)

        //THEN
        verifyOrder {
            anyConstructed<ToolbarManager>().configureNavigationBarForScreen(context, navigationBar)
            anyConstructed<ToolbarManager>().configureToolbar(rootView, navigationBar)
        }
    }
}
