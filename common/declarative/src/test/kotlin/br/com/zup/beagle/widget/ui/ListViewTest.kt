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

package br.com.zup.beagle.widget.ui

import org.junit.Test
import kotlin.test.assertTrue

private val DEFAULT_CHILDREN = listOf(Text(""), Text(""))

internal class ListViewTest {

    @Test
    fun listView_should_have_two_children_and_its_default_values() {
        val listView = ListView(DEFAULT_CHILDREN)

        assertTrue { listView.rows.size == 2 }
        assertTrue { listView.direction == ListDirection.VERTICAL }
    }

    @Test
    fun listView_should_have_direction_VERTICAL() {
        val listView = ListView(DEFAULT_CHILDREN, direction = ListDirection.HORIZONTAL)

        assertTrue { listView.direction == ListDirection.HORIZONTAL }
    }

    @Test
    fun listView_dynamic_should_create_two_children_and_direction_VERTICAL() {
        val listView = ListView.dynamic(size = 2) { index ->
            Text(index.toString())
        }

        assertTrue { listView.rows.size == 2 }
        assertTrue { listView.direction == ListDirection.VERTICAL }
    }
}