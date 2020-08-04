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
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.getContextBinding
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.utils.setContextData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection


@RegisterWidget
internal data class ListViewTwo(
    override val context: ContextData? = null,
    val onInit: List<Action>? = null,
    val dataSource: Bind<List<Any>>? = null,
    val direction: ListDirection,
    val template: ServerDrivenComponent,
    val onScrollEnd: List<Action>? = null,
    val scrollThreshold: Int? = null,
    val useParentScroll: Boolean = false
) : WidgetView(), ContextComponent {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    @Transient
    private lateinit var contextAdapter: ListViewContextAdapter2

    override fun buildView(rootView: RootView): View {
        val recyclerView = viewFactory.makeRecyclerView(rootView.getContext())
        onInit?.forEach { action ->
            action.execute(rootView, recyclerView)
        }
        val orientation = toRecyclerViewOrientation()
        contextAdapter = ListViewContextAdapter2(template, viewFactory, orientation, rootView)
        recyclerView.apply {
            adapter = contextAdapter
            layoutManager = LinearLayoutManager(context, orientation, false)
            isNestedScrollingEnabled = useParentScroll
        }
        configDataSourceObserver(rootView, recyclerView)
        configRecyclerViewScrollListener(recyclerView, rootView)

        return recyclerView
    }

    private fun toRecyclerViewOrientation() = if (direction == ListDirection.VERTICAL) {
        RecyclerView.VERTICAL
    } else {
        RecyclerView.HORIZONTAL
    }

    private fun configDataSourceObserver(rootView: RootView, recyclerView: RecyclerView) {
        dataSource?.let {
            observeBindChanges(rootView, recyclerView, it) { value ->
                if (value.isNullOrEmpty()) {
                    contextAdapter.clearList()
                } else {
                    contextAdapter.setList(value)
                }
            }
        }
    }

    private fun configRecyclerViewScrollListener(
        recyclerView: RecyclerView,
        rootView: RootView
    ) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (needToExecuteOnScrollEnd(recyclerView)) {
                    onScrollEnd?.forEach { action ->
                        action.execute(rootView, recyclerView)
                    }
                }
            }
        })
    }

    private fun needToExecuteOnScrollEnd(recyclerView: RecyclerView): Boolean {
        val scrolledPercent = calculateScrolledPercent(recyclerView)
        scrollThreshold?.let {
            return scrolledPercent >= scrollThreshold
        }
        return scrolledPercent == 100f
    }

    private fun calculateScrolledPercent(recyclerView: RecyclerView): Float {
        val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
        var scrolled = 0f
        layoutManager?.let {
            val totalItemCount = it.itemCount.toFloat()
            val lastVisible = it.findLastVisibleItemPosition().toFloat()
            scrolled = lastVisible / totalItemCount
        }

        return scrolled
    }
}


internal class ListViewContextAdapter2(
    private val template: ServerDrivenComponent,
    private val viewFactory: ViewFactory,
    private val orientation: Int,
    private val rootView: RootView
) : RecyclerView.Adapter<ContextViewHolderTwo>() {

    private var listItems: ArrayList<Any>

    init {
        listItems = ArrayList()
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContextViewHolderTwo {
        val view = viewFactory.makeBeagleFlexView(rootView.getContext()).also {
            val width = if (orientation == RecyclerView.VERTICAL)
                ViewGroup.LayoutParams.MATCH_PARENT else
                ViewGroup.LayoutParams.WRAP_CONTENT
            val layoutParams = ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.layoutParams = layoutParams
        }
        val templateClone = template
        view.addServerDrivenComponent(templateClone, rootView)
        view.setContextData(ContextData("item", listItems[position]))
        rootView.generateViewModelInstance<ScreenContextViewModel>().linkBindingToContext()
        return ContextViewHolderTwo(view)
    }

    override fun onBindViewHolder(holder: ContextViewHolderTwo, position: Int) {
        val item = listItems[position]
        val view = holder.itemView
        view.setContextData(ContextData(id = "item", value = item))
        view.getContextBinding()?.let{contextBinding ->
            rootView.generateViewModelInstance<ScreenContextViewModel>().notifyBindingChanges(contextBinding)

        }
    }

    fun setList(list: List<Any>) {
        this.listItems = ArrayList(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        val initialSize = listItems.size
        listItems.clear()
        notifyItemRangeRemoved(0, initialSize)
    }

    override fun getItemCount(): Int = listItems.size
}

internal class ContextViewHolderTwo(itemView: View) : RecyclerView.ViewHolder(itemView)