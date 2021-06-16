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

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a PullToRefresh")
class PullToRefreshTest : BaseComponentTest() {

    private val swipeRefreshLayout: SwipeRefreshLayout = mockk(relaxed = true, relaxUnitFun = true)
    private val context: ContextData = mockk(relaxed = true)
    private val onPullActions = listOf<Action>(mockk(relaxed = true, relaxUnitFun = true))
    private val isRefreshing: Bind<Boolean> = mockk(relaxed = true, relaxUnitFun = true)
    private val color: Bind<String> = mockk(relaxed = true, relaxUnitFun = true)
    private val child = mockk<ServerDrivenComponent>()
    private val listener = slot<SwipeRefreshLayout.OnChildScrollUpCallback>()
    private lateinit var pullToRefreshComponent: PullToRefresh

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { anyConstructed<ViewFactory>().makeSwipeRefreshLayout(any()) } returns swipeRefreshLayout

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
            verify(exactly = 1) { anyConstructed<ViewFactory>().makeSwipeRefreshLayout(rootView.getContext()) }
        }

        @Test
        @DisplayName("Then should set RefreshListener on pullToRefresh")
        fun buildClickListenerInstance() {
            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = once()) { swipeRefreshLayout.setOnRefreshListener(any()) }
        }

        @Test
        @DisplayName("Then should observe isRefreshing changes")
        fun buildClickListenerInstance() {
            // When
            pullToRefreshComponent.buildView(rootView)

            // Then
            verify(exactly = once()) { swipeRefreshLayout.setOnRefreshListener(any()) }
        }
    }
}