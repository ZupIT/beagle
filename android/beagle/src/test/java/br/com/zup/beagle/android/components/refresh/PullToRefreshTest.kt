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

package br.com.zup.beagle.android.components.refresh

import android.graphics.Color
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a PullToRefresh")
class PullToRefreshTest : BaseComponentTest() {

    private val swipeRefreshLayout: SwipeRefreshLayout = mockk(relaxed = true)
    private val context: ContextData = mockk(relaxed = true)
    private val action: Action = mockk(relaxed = true)
    private val onPullActions = listOf(action)
    private val isRefreshing: Bind<Boolean> = mockk(relaxed = true)
    private val color: Bind<String> = mockk(relaxed = true)
    private val child = mockk<ServerDrivenComponent>()
    private val listenerSlot = slot<SwipeRefreshLayout.OnRefreshListener>()
    private val booleanObserverSlot = slot<Observer<Boolean?>>()
    private val stringObserverSlot = slot<Observer<String?>>()
    private lateinit var pullToRefreshComponent: PullToRefresh

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        mockkStatic(Color::class)

        every { ViewFactory.makeSwipeRefreshLayout(any()) } returns swipeRefreshLayout
        every { swipeRefreshLayout.setOnRefreshListener(capture(listenerSlot)) } just Runs

        pullToRefreshComponent = PullToRefresh(
            context,
            onPullActions,
            isRefreshing,
            color,
            child
        )
    }

    @DisplayName("When build view")
    @Nested
    inner class PullToRefreshBuildTest {

        @Test
        @DisplayName("Then should create correct pullToRefresh")
        fun testBuildCorrectPullToRefresh() {
            // When
            val view = pullToRefreshComponent.buildView(rootView)

            // Then
            Assertions.assertTrue(view is SwipeRefreshLayout)
            verify(exactly = 1) { ViewFactory.makeSwipeRefreshLayout(rootView.getContext()) }
        }

        @Test
        @DisplayName("Then should set RefreshListener on pullToRefresh")
        fun testBuildSetRefreshListener() {
            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = 1) { swipeRefreshLayout.setOnRefreshListener(any()) }
        }

        @Test
        @DisplayName("Then should observe isRefreshing changes")
        fun testBuildObserveIsRefreshing() {
            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = 1) {
                pullToRefreshComponent.observeBindChanges(rootView, swipeRefreshLayout, isRefreshing, captureLambda())
            }
        }

        @Test
        @DisplayName("Then should observe color changes")
        fun testBuildObserveColor() {
            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = 1) {
                pullToRefreshComponent.observeBindChanges(rootView, swipeRefreshLayout, color, captureLambda())
            }
        }

        @Test
        @DisplayName("Then should add child")
        fun testBuildAddChild() {
            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = 1) {
                beagleFlexView.addView(child, false)
            }
        }
    }

    @DisplayName("When build view with null params")
    @Nested
    inner class PullToRefreshBuildWithNullParamsTest {

        @Test
        @DisplayName("Then should not observe isRefreshing changes")
        fun testBuildNotObserveIsRefreshing() {
            // Given
            pullToRefreshComponent = PullToRefresh(
                context,
                onPullActions,
                null,
                "#FF0000",
                child
            )

            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = 0) {
                pullToRefreshComponent.observeBindChanges(rootView, swipeRefreshLayout, isRefreshing, captureLambda())
            }
        }

        @Test
        @DisplayName("Then should not observe color changes")
        fun testBuildNotObserveColor() {
            // Given
            pullToRefreshComponent = PullToRefresh(
                context,
                onPullActions,
                isRefreshing,
                null as String?,
                child
            )

            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = 0) {
                pullToRefreshComponent.observeBindChanges(rootView, swipeRefreshLayout, color, captureLambda())
            }
        }
    }

    @DisplayName("When onRefresh triggered")
    @Nested
    inner class PullToRefreshOnRefreshTest {

        @Test
        @DisplayName("Then should call onPull actions")
        fun testTriggerOnRefresh() {

            // When
            pullToRefreshComponent.buildView(rootView)
            listenerSlot.captured.onRefresh()

            verify(exactly = 1) { action.execute(rootView, swipeRefreshLayout) }
        }
    }

    @DisplayName("When refresh state change")
    @Nested
    inner class PullToRefreshRefreshStateChangeTest {

        @Test
        @DisplayName("Then should update refresh state to true")
        fun testChangeIsRefreshingToTrue() {
            testIsRefreshingStateChange(true)
        }

        @Test
        @DisplayName("Then should update refresh state to false")
        fun testChangeIsRefreshingToFalse() {
            testIsRefreshingStateChange(false)
        }

        @Test
        @DisplayName("Then should update refresh state to false when isRefreshing evaluates to null")
        fun testChangeIsRefreshingToFalseWhenEvaluatesNull() {
            testIsRefreshingStateChange(null)
        }

        private fun testIsRefreshingStateChange(refreshing: Boolean?) {
            every { pullToRefreshComponent.observeBindChanges(rootView, swipeRefreshLayout, isRefreshing, capture(booleanObserverSlot)) } just Runs

            // When
            pullToRefreshComponent.buildView(rootView)
            booleanObserverSlot.captured.invoke(refreshing)

            verify(exactly = 1) { swipeRefreshLayout.isRefreshing = refreshing ?: false }
        }
    }

    @DisplayName("When color state change")
    @Nested
    inner class PullToRefreshColorStateChangeTest {

        @Test
        @DisplayName("Then should update the correct color")
        fun testChangeColorState() {
            // Given
            val colorString = "#FF0000"
            every { pullToRefreshComponent.observeBindChanges(rootView, swipeRefreshLayout, color, capture(stringObserverSlot)) } just Runs
            every { Color.parseColor(colorString) } returns -65536
            val expectedColor = Color.parseColor(colorString)

            // When
            pullToRefreshComponent.buildView(rootView)
            stringObserverSlot.captured.invoke(colorString)

            verify(exactly = 1) { swipeRefreshLayout.setColorSchemeColors(expectedColor) }
        }
    }
}