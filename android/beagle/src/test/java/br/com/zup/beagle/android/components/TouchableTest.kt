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

import android.view.View
import br.com.zup.beagle.android.action.ActionExecutor
import br.com.zup.beagle.analytics.Analytics
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.android.action.Navigate
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class TouchableViewRenderer : BaseComponentTest() {

    @RelaxedMockK
    private lateinit var analytics: Analytics

    private val onClickListenerSlot = slot<View.OnClickListener>()

    private val touchableAction = Navigate.PopView()

    private lateinit var touchable: Touchable

    override fun setUp() {
        super.setUp()

        mockkConstructor(ActionExecutor::class)
        every { beagleSdk.analytics } returns analytics
        every { view.context } returns mockk()
        every { view.setOnClickListener(capture(onClickListenerSlot)) } just Runs

        touchable = Touchable(touchableAction, mockk(relaxed = true))
    }

    @Test
    fun build_should_make_child_view() {
        val actual = touchable.buildView(rootView)

        assertEquals(view, actual)
    }

    @Test
    fun build_should_call_onClickListener() {
        // Given
        val navigateSlot = slot<Navigate>()
        every { anyConstructed<ActionExecutor>().doAction(rootView, capture(navigateSlot)) } just Runs

        // When
        callBuildAndClick()

        // Then
        assertEquals(touchable.action, navigateSlot.captured)
    }

    private fun callBuildAndClick() {
        touchable.buildView(rootView)
        onClickListenerSlot.captured.onClick(view)
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
        touchable = touchable.copy(clickAnalyticsEvent = clickAnalyticsEvent)
        val onClickListenerSlot = CapturingSlot<View.OnClickListener>()

        // When
        val buttonView = touchable.buildView(rootView)
        verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
        onClickListenerSlot.captured.onClick(view)

        // Then
        verify { analytics.sendClickEvent(eq(clickAnalyticsEvent)) }
    }

    @Test
    fun should_not_call_analytics_when_click_event_not_presented() {
        // GIVEN
        val onClickListenerSlot = CapturingSlot<View.OnClickListener>()

        // When
        val buttonView = touchable.buildView(rootView)
        verify { buttonView.setOnClickListener(capture(onClickListenerSlot)) }
        onClickListenerSlot.captured.onClick(view)

        // Then
        verify(exactly = 0) { analytics.sendClickEvent(any()) }
    }
}
