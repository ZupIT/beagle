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
import br.com.zup.beagle.android.view.ComponentsViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection

@RegisterWidget
data class ListView(
    val children: List<ServerDrivenComponent>,
    val direction: ListDirection = ListDirection.VERTICAL
) : WidgetView() {

    @Transient
    private val componentsViewFactory = ComponentsViewFactory()

    override fun buildView(rootView: RootView): View {
        val recyclerView = componentsViewFactory.makeRecyclerView(rootView.getContext())
        recyclerView.apply {
            val orientation = toRecyclerViewOrientation()
            layoutManager = LinearLayoutManager(context, orientation, false)
            adapter = ListViewRecyclerAdapter(children, componentsViewFactory, orientation, rootView)
        }

        return recyclerView
    }

    private fun toRecyclerViewOrientation() = if (direction == ListDirection.VERTICAL) {
        RecyclerView.VERTICAL
    } else {
        RecyclerView.HORIZONTAL
    }
}

internal class ListViewRecyclerAdapter(
    private val children: List<ServerDrivenComponent>,
    private val componentsViewFactory: ComponentsViewFactory,
    private val orientation: Int,
    private val rootView: RootView
) : RecyclerView.Adapter<ViewHolder>() {

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = componentsViewFactory.makeBeagleFlexView(rootView.getContext()).also {
            val width = if (orientation == RecyclerView.VERTICAL)
                ViewGroup.LayoutParams.MATCH_PARENT else
                ViewGroup.LayoutParams.WRAP_CONTENT
            val layoutParams = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.layoutParams = layoutParams
            it.addServerDrivenComponent(children[position], rootView)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = children.size
}

internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)