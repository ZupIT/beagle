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
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.ui.ListDirection
import br.com.zup.beagle.widget.ui.ListDirection.VERTICAL
import br.com.zup.beagle.widget.ui.ListView

data class ListView(
    override val rows: List<ServerDrivenComponent>,
    override val direction: ListDirection = VERTICAL
) : ListView(rows, direction), ViewConvertable {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val recyclerView = RecyclerView(rootView.getContext())
        recyclerView.apply {
            val orientation = toRecyclerViewOrientation()
            layoutManager = LinearLayoutManager(context, orientation, false)
            adapter = ListViewRecyclerAdapter(rows, viewFactory, orientation, rootView)
        }

        return recyclerView
    }

    private fun toRecyclerViewOrientation() = if (direction == VERTICAL) {
        RecyclerView.VERTICAL
    } else {
        RecyclerView.HORIZONTAL
    }
}


internal class ListViewRecyclerAdapter(
    private val rows: List<ServerDrivenComponent>,
    private val viewFactory: ViewFactory,
    private val orientation: Int,
    private val rootView: RootView
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = viewFactory.makeBeagleFlexView(rootView.getContext()).also {
            val width = if (orientation == RecyclerView.VERTICAL)
                ViewGroup.LayoutParams.MATCH_PARENT else
                ViewGroup.LayoutParams.WRAP_CONTENT
            val layoutParams = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.layoutParams = layoutParams
            it.addServerDrivenComponent(rows[position], rootView)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = rows.size
}

internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)