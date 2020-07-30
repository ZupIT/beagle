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
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection
import io.mockk.*
import org.junit.Test
import kotlin.test.assertEquals

class ListViewTwoTest : BaseComponentTest() {

    private val recyclerView: RecyclerView = mockk(relaxed = true)

    private val layoutManagerSlot = slot<LinearLayoutManager>()

    private val template: ServerDrivenComponent = mockk()

    private lateinit var listView: ListViewTwo

    override fun setUp() {
        super.setUp()
        every { beagleFlexView.addView(any()) } just Runs
        every { anyConstructed<ViewFactory>().makeRecyclerView(rootView.getContext()) } returns recyclerView
        every { recyclerView.layoutManager = capture(layoutManagerSlot) } just Runs
        every { recyclerView.adapter = any() } just Runs
    }

    @Test
    fun build_should_set_orientation_VERTICAL() {
        //given
        listView = ListViewTwo(
            direction = ListDirection.VERTICAL,
            template = template
        )
        //when
        listView.buildView(rootView)

        //then
        assertEquals(RecyclerView.VERTICAL, layoutManagerSlot.captured.orientation)
    }
}