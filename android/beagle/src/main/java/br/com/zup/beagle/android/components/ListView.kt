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
class ListView private constructor(
    val children: List<ServerDrivenComponent> = emptyList(),
    override val context: ContextData,
    val onInit: Action,
    val dataSource: Bind<List<Any>>,
    val direction: ListDirection,
    val template: ServerDrivenComponent,
    val onScrollEnd: Action,
    val scrollThreshold: Int
) : WidgetView(), ContextComponent {

    private class EmptyAction : Action {
        override fun execute(rootView: RootView, origin: View) {
        }
    }

    private class ServerDrivenComponentEmpty : ServerDrivenComponent

    @Deprecated(message = "", replaceWith = ReplaceWith("")) //TODO(put message here, implement replaceWith)
    constructor(
        children: List<ServerDrivenComponent>,
        direction: ListDirection
    ) : this(
        children = emptyList(),
        context = ContextData("", Any()),
        onInit = EmptyAction(),
        dataSource = valueOf(emptyList()),
        direction = direction,
        template = ServerDrivenComponentEmpty(),
        onScrollEnd = EmptyAction(),
        scrollThreshold = 0
    )

    constructor(
        context: ContextData,
        onInit: Action,
        dataSource: Bind<List<Any>>,
        direction: ListDirection,
        template: ServerDrivenComponent,
        onScrollEnd: Action,
        scrollThreshold: Int = 100
    ) : this(
        children = emptyList(),
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

    @Transient
    private lateinit var contextAdapter: ListViewContextAdapter

    override fun buildView(rootView: RootView): View {
        val recyclerView = viewFactory.makeRecyclerView(rootView.getContext())
        if (children.isNullOrEmpty()) {
            recyclerView.apply {
                onInit.execute(rootView, this)
                val orientation = toRecyclerViewOrientation()
                layoutManager = LinearLayoutManager(context, orientation, false)
                contextAdapter = ListViewContextAdapter(template, viewFactory, orientation, rootView)
                this@ListView.dataSource?.let { bind ->
                    observeBindChanges(rootView, bind) { value ->
                        value?.let{
                            contextAdapter.setList(it)
                        }
                        adapter = contextAdapter
                    }
                }
            }
        } else {
            recyclerView.apply {
                val orientation = toRecyclerViewOrientation()
                layoutManager = LinearLayoutManager(context, orientation, false)
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


internal class ContextViewHolder(itemView: View, val template: ServerDrivenComponent) : RecyclerView.ViewHolder(itemView)

internal class ListViewContextAdapter(
    private val template: ServerDrivenComponent,
    private val viewFactory: ViewFactory,
    private val orientation: Int,
    private val rootView: RootView
) : RecyclerView.Adapter<ContextViewHolder>() {

    private var listItems: List<Any>

    init {
        listItems = ArrayList()
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContextViewHolder {
        val view = viewFactory.makeBeagleFlexView(rootView.getContext()).also {
            val width = if (orientation == RecyclerView.VERTICAL)
                ViewGroup.LayoutParams.MATCH_PARENT else
                ViewGroup.LayoutParams.WRAP_CONTENT
            val layoutParams = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.layoutParams = layoutParams
        }
        //TODO(Clone the template to avoid all lines having the same content)
        val templateClone = template
        rootView.generateViewModelInstance<ScreenContextViewModel>().addContext(ContextData("", Any()))
        view.addServerDrivenComponent(templateClone, rootView)
        return ContextViewHolder(view, templateClone)
    }

    override fun onBindViewHolder(holder: ContextViewHolder, position: Int) {
        val item = listItems[position]
        val contextData = ContextData(id = "item", value = item)

        updateContext(holder.template, contextData)
    }

    fun setList(list: List<Any>) {
        this.listItems = list
        notifyDataSetChanged()
    }

    fun updateContext(template: ServerDrivenComponent, contextData: ContextData) {
        //TODO(set context data of the template)
    }

    override fun getItemCount(): Int = listItems.size
}