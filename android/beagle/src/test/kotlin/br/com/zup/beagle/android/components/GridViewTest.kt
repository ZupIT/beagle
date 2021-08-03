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

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.SendRequest
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import br.com.zup.beagle.android.view.ViewFactory
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantExecutorExtension::class)
@DisplayName("Given a GridView")
class GridViewTest : BaseComponentTest() {

    private data class Cell(
        val id: Int,
        val name: String,
    )

    private val beagleRecyclerView: BeagleRecyclerView = mockk(relaxed = true)
    private val layoutManagerSlot = slot<RecyclerView.LayoutManager>()

    private val context = ContextData(
        id = "context",
        value = listOf(Cell(10, "Item 1"), Cell(20, "Item 2"), Cell(30, "Item 3"))
    )
    private val onInit = listOf(SendRequest("http://www.init.com"))
    private val dataSource = expressionOf<List<Any>>("@{context}")
    private val templates by lazy { listOf(Template(view = Container(children = listOf(Text(expressionOf("@{item.name}")))))) }
    private val onScrollEnd = listOf(mockk<Action>(relaxed = true))
    private val iteratorName = "list"
    private val key = "id"
    private val numColumns = 3

    private lateinit var gridView: GridView

    @BeforeEach
    override fun setUp() {
        super.setUp()

        gridView = GridView(context, onInit, dataSource, templates, onScrollEnd, iteratorName = iteratorName, key = key, numColumns = numColumns)

        every { ViewFactory.makeBeagleRecyclerView(rootView.getContext()) } returns beagleRecyclerView
        every { beagleRecyclerView.layoutManager = capture(layoutManagerSlot) } just Runs

    }

    @DisplayName("When buildView")
    @Nested
    inner class GridViewBuild {
        @Test
        @DisplayName("Then should return a GridLayoutManager instance")
        fun testLayoutManager() {

            // When
            gridView.buildView(rootView)

            // Then
            Assertions.assertTrue(layoutManagerSlot.captured is GridLayoutManager)
            Assertions.assertEquals((layoutManagerSlot.captured as GridLayoutManager).spanCount, numColumns)
        }
    }

}