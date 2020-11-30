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

package br.com.zup.beagle.android.components.list

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.components.OnInitiableComponent
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.testutil.getPrivateField
import br.com.zup.beagle.android.utils.getContextBinding
import br.com.zup.beagle.android.utils.isInitiableComponent
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.setId
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.LinkedList

@DisplayName("Given a ListViewHolder")
@ExtendWith(InstantExecutorExtension::class)
class ListViewHolderTest : BaseTest() {

    private lateinit var listViewHolder: ListViewHolder

    private val itemView = mockk<View>(relaxed = true)
    private val template = mockk<ServerDrivenComponent>(relaxed = true)
    private val serializer = mockk<BeagleSerializer>(relaxed = true)
    private val listViewModels = mockk<ListViewModels>()
    private val viewModel = mockk<ScreenContextViewModel>(relaxed = true)
    private val listViewIdViewModel = mockk<ListViewIdViewModel>(relaxed = true)
    private val jsonTemplate = ""
    private val iteratorName = "list"
    private val listItem = mockk<ListItem>(relaxed = true)

    @BeforeEach
    override fun setUp() {
        super.setUp()
        mockkConstructor(BeagleSerializer::class)

        every { listViewModels.contextViewModel } returns viewModel
        every { listViewModels.listViewIdViewModel } returns listViewIdViewModel
        listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)
    }

    @DisplayName("When create the holder")
    @Nested
    inner class Init {

        @DisplayName("Then should add view with onInit to viewsWithOnInit")
        @Test
        fun viewsWithOnInit() {
            // Given
            val expectedViewsWithIdList = listOf(itemView)
            every { itemView.isInitiableComponent() } returns true

            // When
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)
            val viewsWithOnInit = listViewHolder.getPrivateField<MutableList<View>>("viewsWithOnInit")

            // Then
            assertEquals(expectedViewsWithIdList, viewsWithOnInit)
        }

        @DisplayName("Then should add view with id to viewsWithId")
        @Test
        fun viewsWithId() {
            // Given
            val id = "10"
            val template = Container(children = listOf()).setId(id)
            val view = mockk<View>()
            every { itemView.findViewById<View>(id.toAndroidId()) } returns view

            // When
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)
            val viewsWithId = listViewHolder.getPrivateField<MutableMap<String, View>>("viewsWithId")

            // Then
            verify(exactly = 1) { itemView.findViewById<View>(id.toAndroidId()) }
            assertEquals(view, viewsWithId[id])
        }

        @DisplayName("Then should add view with context to viewsWithContext")
        @Test
        fun viewsWithContext() {
            // Given
            every { itemView.getContextBinding() } returns mockk {
                every { context } returns ContextData("id", "value")
            }

            // When
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)
            val viewsWithContext = listViewHolder.getPrivateField<MutableList<View>>("viewsWithContext")

            // Then
            assertEquals(itemView, viewsWithContext[0])
        }

        @DisplayName("Then should add imageView to directNestedImageViews")
        @Test
        fun directNestedImageViews() {
            // Given
            val itemView = mockk<ImageView>(relaxed = true)

            // When
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // Then
            assertEquals(itemView, listViewHolder.directNestedImageViews[0])
        }

        @DisplayName("Then should add textView to directNestedTextViews")
        @Test
        fun directNestedTextViews() {
            // Given
            val itemView = mockk<TextView>(relaxed = true)

            // When
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // Then
            assertEquals(itemView, listViewHolder.directNestedTextViews[0])
        }

        @DisplayName("Then should add recyclerView to directNestedRecyclers")
        @Test
        fun directNestedRecyclers() {
            // Given
            val itemView = mockk<RecyclerView>(relaxed = true)

            // When
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)
            val directNestedRecyclers = listViewHolder.getPrivateField<MutableList<RecyclerView>>("directNestedRecyclers")

            // Then
            assertEquals(itemView, directNestedRecyclers[0])
        }
    }

    @DisplayName("When onBind is called")
    @Nested
    inner class OnBind {

        @DisplayName("Then should clear contextComponents")
        @Test
        fun contextComponents() {
            // Given
            val contexts = listViewHolder.getPrivateField<MutableList<ContextData>>("contextComponents")
            contexts.add(mockk())

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertTrue { contexts.isEmpty() }
        }

        @DisplayName("Then should call deserializeComponent to a recycled item")
        @Test
        fun deserializeComponent() {
            // Given
            every { listItem.isRecycled } returns true

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            verify(exactly = 1) { serializer.deserializeComponent(jsonTemplate) }
        }

        @DisplayName("Then should add to contextComponents to a contextComponent")
        @Test
        fun contextComponent() {
            // Given
            val context = ContextData("id", "value")
            val template = Container(children = listOf(), context = context)
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)
            val contexts = listViewHolder.getPrivateField<MutableList<ContextData>>("contextComponents")

            // Then
            assertEquals(context, contexts[0])
        }

        @DisplayName("Then should generateItemSuffix to a firstTimeBinding item")
        @Test
        fun generateItemSuffix() {
            // Given
            every { listItem.firstTimeBinding } returns true
            val itemSuffixSlot = slot<String>()
            every { listItem.itemSuffix = capture(itemSuffixSlot) } just Runs
            val position = 0
            val listIdByItemKey = position.toString()

            // When
            listViewHolder.onBind(null, null, listItem, position, 0)

            // Then
            assertEquals(listIdByItemKey, itemSuffixSlot.captured)
        }

        @DisplayName("Then should generateItemSuffix to a firstTimeBinding item with parent")
        @Test
        fun generateItemSuffixWithParent() {
            // Given
            every { listItem.firstTimeBinding } returns true
            val itemSuffixSlot = slot<String>()
            every { listItem.itemSuffix = capture(itemSuffixSlot) } just Runs
            val parentListViewSuffix = "identifier"
            val position = 0
            val listIdByItemKey = "$parentListViewSuffix:$position"

            // When
            listViewHolder.onBind(parentListViewSuffix, null, listItem, position, 0)

            // Then
            assertEquals(listIdByItemKey, itemSuffixSlot.captured)
        }

        @DisplayName("Then should generateItemSuffix to a firstTimeBinding item with key")
        @Test
        fun generateItemSuffixWithKey() {
            // Given
            every { listItem.firstTimeBinding } returns true
            val itemSuffixSlot = slot<String>()
            every { listItem.itemSuffix = capture(itemSuffixSlot) } just Runs
            val data = JSONObject(""" { id: 10, name: Item } """.trimIndent())
            every { listItem.data } returns data
            val listIdByItemKey = "10"

            // When
            listViewHolder.onBind(null, "id", listItem, 0, 0)

            // Then
            assertEquals(listIdByItemKey, itemSuffixSlot.captured)
        }

        @DisplayName("Then should updateIdToEachSubView to a firstTimeBinding item")
        @Test
        fun updateIdToEachSubView() {
            // Given
            every { listItem.firstTimeBinding } returns true
            every { itemView.id } returns View.NO_ID
            val recyclerId = 0
            val position = 0
            val generatedId = 100
            every { listViewIdViewModel.getViewId(recyclerId, position) } returns generatedId
            val idSlot = slot<Int>()
            every { listItem.viewIds } returns mockk {
                every { add(capture(idSlot)) } returns true
            }
            val viewIdSlot = slot<Int>()
            every { itemView.id = capture(viewIdSlot) } just Runs

            // When
            listViewHolder.onBind(null, null, listItem, position, recyclerId)

            // Then
            assertEquals(generatedId, viewIdSlot.captured)
            assertEquals(generatedId, idSlot.captured)
            verify(exactly = 1) { viewModel.setIdToViewWithContext(itemView) }
        }

        @DisplayName("Then should updateIdToEachSubView to a firstTimeBinding item with bff id")
        @Test
        fun updateIdToEachSubViewWithBffId() {
            // Given
            val bffId = "idFromBff".toAndroidId()
            every { listItem.firstTimeBinding } returns true
            val newIdSlot = slot<Int>()
            every { itemView.id = capture(newIdSlot) } just Runs
            every { itemView.id } answers {
                if (newIdSlot.isCaptured) newIdSlot.captured else bffId
            }
            val recyclerId = 0
            val position = 0
            val generatedId = 100
            every { listViewIdViewModel.setViewId(recyclerId, position, bffId) } returns generatedId

            // When
            listViewHolder.onBind(null, null, listItem, position, recyclerId)

            // Then
            verify(exactly = 1) { viewModel.onViewIdChanged(bffId, newIdSlot.captured, itemView) }
        }

        @DisplayName("Then should updateIdToEachSubView to a firstTimeBinding item with id")
        @Test
        fun updateIdToEachSubViewWithId() {
            // Given
            val previousId = 100
            every { listItem.firstTimeBinding } returns true
            every { itemView.id } returns previousId
            val recyclerId = 0
            val position = 0
            every { listViewIdViewModel.setViewId(recyclerId, position, previousId) } returns previousId
            val idSlot = slot<Int>()
            every { listItem.viewIds } returns mockk {
                every { add(capture(idSlot)) } returns true
            }
            val viewIdSlot = slot<Int>()
            every { itemView.id = capture(viewIdSlot) } just Runs

            // When
            listViewHolder.onBind(null, null, listItem, position, recyclerId)

            // Then
            assertEquals(previousId, viewIdSlot.captured)
            assertEquals(previousId, idSlot.captured)
            verify(exactly = 1) { listViewIdViewModel.setViewId(recyclerId, position, previousId) }
        }

        @DisplayName("Then should setDefaultContextToEachContextView to a firstTimeBinding recycled item")
        @Test
        fun setDefaultContextToEachContextView() {
            // Given
            every { listItem.firstTimeBinding } returns true
            every { listItem.isRecycled } returns true
            every { itemView.getContextBinding() } returns mockk {
                every { context } returns ContextData("id", "value")
            }
            val context = mockk<ContextData>()
            every { viewModel.getContextData(itemView) } returns context

            val contextSlot = mutableListOf<ContextData>()
            every { viewModel.addContext(itemView, capture(contextSlot), shouldOverrideExistingContext = true) } just Runs
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertEquals(context, contextSlot[0])
        }

        @DisplayName("Then should setDefaultContextToEachContextView to a firstTimeBinding recycled item with savedContext")
        @Test
        fun setDefaultContextToEachContextViewWithContext() {
            // Given
            every { listItem.firstTimeBinding } returns true
            every { listItem.isRecycled } returns true
            every { itemView.getContextBinding() } returns mockk {
                every { context } returns ContextData("id", "otherContext")
            }
            every { viewModel.getContextData(itemView) } returns null

            val context = ContextData("id", "savedContext")
            val template = Container(children = listOf(), context = context)
            every { serializer.deserializeComponent(jsonTemplate) } returns template

            val contextSlot = mutableListOf<ContextData>()
            every { viewModel.addContext(itemView, capture(contextSlot), shouldOverrideExistingContext = true) } just Runs
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertEquals(context, contextSlot[0])
        }

        @DisplayName("Then should generateAdapterToEachDirectNestedRecycler to a firstTimeBinding recycled item with recycler")
        @Test
        fun generateAdapterToEachDirectNestedRecycler() {
            // Given
            every { listItem.firstTimeBinding } returns true
            every { listItem.isRecycled } returns true
            val itemView = mockk<RecyclerView>(relaxed = true)
            every { itemView.adapter } returns mockk<ListAdapter>(relaxed = true)
            val jsonTemplate = """{ "_beagleComponent_": "beagle:button", "text": "Test" }""".trimIndent()
            every { anyConstructed<BeagleSerializer>().serializeComponent(any()) } returns jsonTemplate
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            verify(exactly = 1) { itemView.swapAdapter(any(), false) }
            assertTrue(listItem.directNestedAdapters.isNotEmpty())
        }

        @DisplayName("Then should saveCreatedAdapterToEachDirectNestedRecycler to a firstTimeBinding not recycled item with recycler")
        @Test
        fun saveCreatedAdapterToEachDirectNestedRecycler() {
            // Given
            val listItem = ListItem(data = "stub")
            val itemView = mockk<RecyclerView>(relaxed = true)
            val adapter = mockk<ListAdapter>(relaxed = true)
            every { itemView.adapter } returns adapter
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertEquals(adapter, listItem.directNestedAdapters[0])
        }

        @DisplayName("Then should updateDirectNestedAdaptersSuffix to a firstTimeBinding item with recycler")
        @Test
        fun updateDirectNestedAdaptersSuffix() {
            // Given
            val suffix = "suffix"
            val listItem = ListItem(data = "stub", itemSuffix = suffix)
            val itemView = mockk<RecyclerView>(relaxed = true)
            val itemSuffixSlot = slot<String>()
            val adapter = mockk<ListAdapter>(relaxed = true) {
                every { setParentSuffix(capture(itemSuffixSlot)) } just Runs
            }
            every { itemView.adapter } returns adapter
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertEquals(suffix, itemSuffixSlot.captured)
        }

        @DisplayName("Then should restoreIds to a not firstTimeBinding item")
        @Test
        fun restoreIds() {
            // Given
            val viewIds = LinkedList<Int>().apply {
                add(100)
            }
            every { listItem.viewIds } returns viewIds
            val idSlot = slot<Int>()
            every { itemView.id = capture(idSlot) } just Runs

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertEquals(viewIds[0], idSlot.captured)
        }

        @DisplayName("Then should restoreAdapters to a not firstTimeBinding item")
        @Test
        fun restoreAdapters() {
            // Given
            val itemView = mockk<RecyclerView>(relaxed = true)
            val adapter = mockk<ListAdapter>(relaxed = true)
            val adapters = LinkedList<ListAdapter>().apply {
                add(adapter)
            }
            every { listItem.directNestedAdapters } returns adapters
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            verify(exactly = 1) { itemView.swapAdapter(adapter, false) }
        }

        @DisplayName("Then should restoreContexts to a not firstTimeBinding item")
        @Test
        fun restoreContexts() {
            // Given
            every { itemView.getContextBinding() } returns mockk {
                every { context } returns ContextData("id", "value")
            }
            listViewHolder = ListViewHolder(itemView, template, serializer, listViewModels, jsonTemplate, iteratorName)

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            verify(exactly = 1) { viewModel.restoreContext(itemView) }
        }

        @DisplayName("Then should setContext to every item")
        @Test
        fun setContext() {
            // Given
            val data = "data"
            every { listItem.data } returns data
            val contextSlot = slot<ContextData>()
            every { viewModel.addContext(itemView, capture(contextSlot), true) } just Runs

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertEquals(iteratorName, contextSlot.captured.id)
            assertEquals(data, contextSlot.captured.value)
        }

        @DisplayName("Then should set firstTimeBinding to false to every item")
        @Test
        fun firstTimeBinding() {
            // Given
            val listItem = ListItem(data = "stub")

            // When
            listViewHolder.onBind(null, null, listItem, 0, 0)

            // Then
            assertFalse(listItem.firstTimeBinding)
        }
    }
}
