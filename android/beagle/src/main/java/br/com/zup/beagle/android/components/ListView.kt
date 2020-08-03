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
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection

@RegisterWidget
data class ListView(
    val children: List<ServerDrivenComponent>? = null,
    override val context: ContextData? = null,
    val onInit: List<Action>? = null,
    val dataSource: Bind<List<Any>>? = null,
    val direction: ListDirection,
    val template: ServerDrivenComponent? = null,
    val onScrollEnd: List<Action>? = null,
    val scrollThreshold: Int? = null
) : WidgetView(), ContextComponent {

    @Deprecated(message = "", replaceWith = ReplaceWith("")) //TODO(put message here, implement replaceWith)
    constructor(
        children: List<ServerDrivenComponent>,
        direction: ListDirection
    ) : this(
        context = null,
        children = children,
        direction = direction
    )

    constructor(
        context: ContextData? = null,
        onInit: List<Action>? = null,
        dataSource: Bind<List<Any>>,
        direction: ListDirection,
        template: ServerDrivenComponent,
        onScrollEnd: List<Action>? = null,
        scrollThreshold: Int? = null
    ) : this(
        children = null,
        context = context,
        onInit = onInit,
        dataSource = dataSource,
        direction = direction,
        template = template,
        onScrollEnd = onScrollEnd,
        scrollThreshold = scrollThreshold
    )

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        if (children.isNullOrEmpty()) {
            template?.let{
                return ListViewTwo(
                    context,
                    onInit,
                    dataSource,
                    direction,
                    template,
                    onScrollEnd,
                    scrollThreshold
                ).buildView(rootView)
            }
        }

        val recyclerView = viewFactory.makeRecyclerView(rootView.getContext())
        recyclerView.apply {
            val orientation = toRecyclerViewOrientation()
            layoutManager = LinearLayoutManager(context, orientation, false)
            children?.let{
                adapter = ListViewRecyclerAdapter(children, viewFactory, orientation, rootView)
            }
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
            it.addServerDrivenComponent(children[position], rootView)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = children.size
}

internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)