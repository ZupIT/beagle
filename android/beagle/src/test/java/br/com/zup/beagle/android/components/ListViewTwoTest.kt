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

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection
import io.mockk.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ListViewTwoTest : BaseComponentTest() {

    private val recyclerView: RecyclerView = mockk(relaxed = true)
    private val template: ServerDrivenComponent = mockk(relaxed = true)
    private val onInit: Action = mockk(relaxed = true)
    private val dataSource: Bind<List<Any>> = mockk()
    private val onScrollEnd: Action = mockk(relaxed = true)

    private val layoutManagerSlot = slot<LinearLayoutManager>()
    private val adapterSlot = slot<RecyclerView.Adapter<RecyclerView.ViewHolder>>()
    private val isNestedScrollingEnabledSlot = slot<Boolean>()
    private val dataSourceSlot = slot<Observer<List<Any>?>>()
    private val scrollListenerSlot = slot<RecyclerView.OnScrollListener>()

    private lateinit var listView: ListViewTwo

    override fun setUp() {
        super.setUp()
        mockkConstructor(ListViewContextAdapter2::class)
        every { anyConstructed<ViewFactory>().makeRecyclerView(rootView.getContext()) } returns recyclerView
        every { recyclerView.layoutManager = capture(layoutManagerSlot) } just Runs
        every { recyclerView.isNestedScrollingEnabled = capture(isNestedScrollingEnabledSlot) } just Runs
    }

    @Test
    fun build_view_should_set_orientation_VERTICAL_when_direction_is_ListDirection_VERTICAL() {
        //given
        listView = ListViewTwo(
            direction = ListDirection.VERTICAL,
            template = template,
            dataSource = dataSource
        )
        //when
        listView.buildView(rootView)

        //then
        assertEquals(RecyclerView.VERTICAL, layoutManagerSlot.captured.orientation)
    }

    @Test
    fun build_view_should_set_orientation_VERTICAL_when_direction_is_ListDirection_HORIZONTAL() {
        //given
        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            dataSource = dataSource
        )
        //when
        listView.buildView(rootView)

        //then
        assertEquals(RecyclerView.HORIZONTAL, layoutManagerSlot.captured.orientation)
    }

    @Test
    fun build_view_should_set_adapter() {
        //given
        every { recyclerView.adapter = capture(adapterSlot) } just Runs

        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            dataSource = dataSource
        )
        //when
        listView.buildView(rootView)

        //then
        assertTrue { adapterSlot.isCaptured }
    }

    @Test
    fun build_view_should_set_isNestedScrollingEnabled_false_when_useParentScroll_is_not_passed() {
        //given
        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            dataSource = dataSource
        )
        //when
        listView.buildView(rootView)

        //then
        assertFalse { isNestedScrollingEnabledSlot.captured }
    }

    @Test
    fun build_view_should_set_isNestedScrollingEnabled_false_when_useParentScroll_is_false() {
        //given
        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            useParentScroll = false,
            dataSource = dataSource
        )
        //when
        listView.buildView(rootView)

        //then
        assertFalse { isNestedScrollingEnabledSlot.captured }
    }

    @Test
    fun build_view_should_set_isNestedScrollingEnabled_true_when_useParentScroll_is_true() {
        //given

        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            useParentScroll = true,
            dataSource = dataSource
        )
        //when
        listView.buildView(rootView)

        //then
        assertTrue { isNestedScrollingEnabledSlot.captured }
    }

    @Test
    fun build_view_should_execute_on_init_once() {
        //given

        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            onInit = listOf(onInit),
            dataSource = dataSource
        )
        every {
            onInit.execute(rootView, recyclerView)
        } just Runs
        //when
        listView.buildView(rootView)

        //then
        verify(exactly = once()) {
            onInit.execute(rootView, recyclerView)
        }
    }

    @Test
    fun when_dataSource_change_with_empty_list_clearList_should_be_called() {
        //given
        commonDataSourceObserverMock()
        every { anyConstructed<ListViewContextAdapter2>().clearList()} just Runs

        //when
        listView.buildView(rootView)
        dataSourceSlot.captured.invoke(listOf())

        //then
        verify(exactly = once()) {
            anyConstructed<ListViewContextAdapter2>().clearList()
        }
    }

    @Test
    fun when_dataSource_change_with_nullable_list_clearList_should_be_called() {
        //given
        commonDataSourceObserverMock()
        every { anyConstructed<ListViewContextAdapter2>().clearList()} just Runs

        //when
        listView.buildView(rootView)
        dataSourceSlot.captured.invoke(null)

        //then
        verify(exactly = once()) {
            anyConstructed<ListViewContextAdapter2>().clearList()
        }
    }

    @Test
    fun when_dataSource_change_with_populated_list_setList_should_be_called() {
        //given
        commonDataSourceObserverMock()
        val list = listOf("test")
        every { anyConstructed<ListViewContextAdapter2>().setList(any())} just Runs

        //when
        listView.buildView(rootView)
        dataSourceSlot.captured.invoke(list)

        //then
        verify(exactly = once()) {
            anyConstructed<ListViewContextAdapter2>().setList(list)
        }
    }

    private fun commonDataSourceObserverMock(){
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            dataSource = dataSource
        )
        every {
            listView.observeBindChanges(
                rootView = rootView,
                view = recyclerView,
                bind = dataSource,
                observes = capture(dataSourceSlot)
            )
        } just Runs
    }

    @Test
    fun when_scrolled_item_percent_visible_lass_than_scrollThreshold_onScrollEnd_should_not_execute() {
        //given
        val numberItem = 20
        val lastPosition = 5
        commonPercentScrolledMock(scrollThreshold = 50, itemCount = numberItem, lastVisibleItemPosition = lastPosition)

        //when
        listView.buildView(rootView)
        scrollListenerSlot.captured.onScrollStateChanged(recyclerView, RandomData.int())

        //then
        verify(exactly = 0) {
            onScrollEnd.execute(rootView, recyclerView)
        }
    }

    @Test
    fun when_scrolled_item_percent_visible_equal_than_scrollThreshold_onScrollEnd_should_execute() {
        //given
        val numberItem = 20
        val lastPosition = 10
        commonPercentScrolledMock(scrollThreshold = 50, itemCount = numberItem, lastVisibleItemPosition = lastPosition)

        //when
        listView.buildView(rootView)
        scrollListenerSlot.captured.onScrollStateChanged(recyclerView, RandomData.int())

        //then
        verify(exactly = 1) {
            onScrollEnd.execute(rootView, recyclerView)
        }
    }

    @Test
    fun when_scrolled_item_percent_visible_more_than_scrollThreshold_onScrollEnd_should_execute() {
        //given
        val numberItem = 20
        val lastPosition = 19
        commonPercentScrolledMock(scrollThreshold = 50, itemCount = numberItem, lastVisibleItemPosition = lastPosition)

        //when
        listView.buildView(rootView)
        scrollListenerSlot.captured.onScrollStateChanged(recyclerView, RandomData.int())

        //then
        verify(exactly = 1) {
            onScrollEnd.execute(rootView, recyclerView)
        }
    }

    @Test
    fun when_scrolled_item_percent_visible_lass_than_100_and_scrollThreshold_not_passed_onScrollEnd_should_not_execute() {
        //given
        val numberItem = 20
        val lastPosition = 10
        commonPercentScrolledMock(itemCount = numberItem, lastVisibleItemPosition = lastPosition)

        //when
        listView.buildView(rootView)
        scrollListenerSlot.captured.onScrollStateChanged(recyclerView, RandomData.int())

        //then
        verify(exactly = 0) {
            onScrollEnd.execute(rootView, recyclerView)
        }
    }

    @Test
    fun when_scrolled_item_percent_visible_is_100_and_scrollThreshold_not_passed_onScrollEnd_should_execute() {
        //given
        val numberItem = 20
        commonPercentScrolledMock(itemCount = numberItem, lastVisibleItemPosition = numberItem)

        //when
        listView.buildView(rootView)
        scrollListenerSlot.captured.onScrollStateChanged(recyclerView, RandomData.int())

        //then
        verify(exactly = 1) {
            onScrollEnd.execute(rootView, recyclerView)
        }
    }

    private fun commonPercentScrolledMock(scrollThreshold: Int? = null, itemCount: Int, lastVisibleItemPosition: Int) {
        mockkStatic("br.com.zup.beagle.android.utils.WidgetExtensionsKt")
        listView = ListViewTwo(
            direction = ListDirection.HORIZONTAL,
            template = template,
            dataSource = dataSource,
            onScrollEnd = listOf(onScrollEnd),
            scrollThreshold = scrollThreshold
        )
        val linearLayoutManager: LinearLayoutManager = mockk()
        every { recyclerView.addOnScrollListener(capture(scrollListenerSlot)) } just Runs
        every { recyclerView.layoutManager } returns linearLayoutManager
        every { linearLayoutManager.itemCount } returns itemCount
        every { linearLayoutManager.findLastVisibleItemPosition() } returns lastVisibleItemPosition
    }
}