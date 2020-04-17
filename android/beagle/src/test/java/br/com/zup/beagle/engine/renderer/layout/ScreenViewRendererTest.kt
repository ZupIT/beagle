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

package br.com.zup.beagle.engine.renderer.layout

import android.graphics.Color
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRenderer
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.BeagleFlexView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.ScreenComponent
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val DEFAULT_COLOR = 0xFFFFFF

class ScreenViewRendererTest : BaseTest() {

    val screenName = "screenName"
    private var screenAnalyticsEvent = ScreenEvent( screenName = screenName )

    @MockK
    private lateinit var screenComponent: ScreenComponent
    @MockK
    private lateinit var viewRendererFactory: ViewRendererFactory
    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var rootView: RootView
    @RelaxedMockK
    private lateinit var context: BeagleActivity
    @RelaxedMockK
    private lateinit var beagleFlexView: BeagleFlexView
    @MockK
    private lateinit var component: ServerDrivenComponent
    @RelaxedMockK
    private lateinit var analytics: Analytics
    @MockK
    private lateinit var viewRenderer: ViewRenderer<*>
    @RelaxedMockK
    private lateinit var view: View
    @MockK(relaxed = true)
    private lateinit var actionBar: ActionBar
    @RelaxedMockK
    private lateinit var toolbar: Toolbar
    @MockK

    private lateinit var screenViewRenderer: ScreenViewRenderer

    override fun setUp() {
        super.setUp()

        mockkStatic(Color::class)

        every { beagleSdk.analytics } returns analytics
        every { viewFactory.makeBeagleFlexView(any()) } returns beagleFlexView
        every { viewFactory.makeBeagleFlexView(any(), any()) } returns beagleFlexView
        every { beagleFlexView.addServerDrivenComponent(any(), any()) } just Runs
        every { beagleFlexView.addView(any(), any<Flex>()) } just Runs
        every { screenComponent.navigationBar } returns null
        every { screenComponent.child } returns component
        every { screenComponent.screenAnalyticsEvent } returns null
        every { screenComponent.appearance } returns null
        every { viewRendererFactory.make(any()) } returns viewRenderer
        every { viewRenderer.build(any()) } returns view
        every { Color.parseColor(any()) } returns DEFAULT_COLOR
        every { rootView.getContext() } returns context

        screenViewRenderer = ScreenViewRenderer(
            screenComponent,
            viewRendererFactory,
            viewFactory
        )
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun build_should_create_a_screenWidget_with_grow_1_and_justifyContent_SPACE_BETWEEN() {
        // Given
        val flex = slot<Flex>()
        every { viewFactory.makeBeagleFlexView(any(), capture(flex)) } returns beagleFlexView
        every { context.supportActionBar } returns null

        // When
        screenViewRenderer.build(rootView)

        // Then
        assertEquals(1.0, flex.captured.grow)
    }

    @Test
    fun build_should_call_content_builder() {
        // Given
        val content = mockk<ServerDrivenComponent>()
        val flex = slot<Flex>()
        every { screenComponent.child } returns content
        every { beagleFlexView.addView(view, capture(flex)) } just Runs
        every { context.supportActionBar } returns null

        // When
        screenViewRenderer.build(rootView)

        // Then
        verify(atLeast = once()) { beagleFlexView.addServerDrivenComponent(content, rootView)}
    }

    @Test
    fun build_should_hideNavigationBar_when_navigationBar_is_null() {
        // GIVEN
        every { context.supportActionBar } returns actionBar
        every { context.getToolbar() } returns toolbar
        val expected = View.GONE
        every { toolbar.visibility = any() } just Runs
        every { toolbar.visibility } returns expected

        // WHEN
        screenViewRenderer.build(rootView)

        // THEN
        assertEquals(expected, toolbar.visibility)
        verify(atLeast = once()) { actionBar.hide() }
    }

    @Test
    fun should_assign_window_attach_callbacks_when_screen_event_presented() {
        // GIVEN
        every { screenComponent.screenAnalyticsEvent } returns screenAnalyticsEvent

        // When
        val view = screenViewRenderer.build(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
        verify { view.addOnAttachStateChangeListener(any()) }
    }

    @Test
    fun should_keep_window_attach_callbacks_null_when_screen_event_not_presented() {
        // When
        val view = screenViewRenderer.build(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
        verify(exactly = 0) { view.addOnAttachStateChangeListener(any()) }
    }

    @Test
    fun should_call_analytics_when_window_attach_states_changes() {
        // GIVEN
        every { screenComponent.screenAnalyticsEvent } returns screenAnalyticsEvent
        val onAttachStateChangeListenerSlot = CapturingSlot<View.OnAttachStateChangeListener>()

        // When
        val screenView = screenViewRenderer.build(rootView)
        verify { screenView.addOnAttachStateChangeListener(capture(onAttachStateChangeListenerSlot)) }
        onAttachStateChangeListenerSlot.captured.onViewAttachedToWindow(view)
        onAttachStateChangeListenerSlot.captured.onViewDetachedFromWindow(view)

        // Then
        var capturedEvent = CapturingSlot<ScreenEvent>()
        verify { analytics.sendViewWillAppearEvent(capture(capturedEvent)) }
        assertEquals(screenName, capturedEvent.captured.screenName)

        verify { analytics.sendViewWillDisappearEvent(capture(capturedEvent)) }
        assertEquals(screenName, capturedEvent.captured.screenName)
    }
}
