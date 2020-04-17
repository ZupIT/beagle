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

import br.com.zup.beagle.core.LayoutComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.Widget

enum class ListDirection {
    VERTICAL,
    HORIZONTAL
}

typealias RowBuilder = (index: Int) -> Widget

data class ListView(
    val rows: List<ServerDrivenComponent>,
    val direction: ListDirection = ListDirection.VERTICAL
) : ServerDrivenComponent, LayoutComponent {

    companion object {
        @JvmStatic
        fun dynamic(
            size: Int,
            direction: ListDirection = ListDirection.VERTICAL,
            rowBuilder: RowBuilder
        ): ListView {
            val rows = generateRows(size, rowBuilder)
            return ListView(rows = rows, direction = direction)
        }

        private fun generateRows(size: Int, rowBuilder: RowBuilder): List<Widget> {
            val children = mutableListOf<Widget>()

            for (i in 0 until size) {
                children.add(rowBuilder(i))
            }

            return children
        }
    }
}
