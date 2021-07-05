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

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.components.list.ListAdapter
import br.com.zup.beagle.android.components.list.ListViewModels
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.ListDirection

@RegisterWidget("listView")
data class ListView

/**
 * ListView is a Layout component that will define a list of views natively.
 * These views could be any Server Driven Component.
 * @see ContextComponent
 * @see OnInitiableComponent
 */
@Deprecated(
    message = "It was deprecated in version 1.5 and will be removed in a future version. " +
        "Use dataSource and template instead children.",
    replaceWith = ReplaceWith(
        "ListView(direction, context, onInit, dataSource, template, onScrollEnd, scrollEndThreshold," +
            "iteratorName, key)")
)
constructor(
    val children: List<ServerDrivenComponent>? = null,
    val direction: ListDirection = ListDirection.VERTICAL,
    override val context: ContextData? = null,
    override val onInit: List<Action>? = null,
    val dataSource: Bind<List<Any>>? = null,
    val template: ServerDrivenComponent? = null,
    val onScrollEnd: List<Action>? = null,
    val scrollEndThreshold: Int? = null,
    val isScrollIndicatorVisible: Boolean = false,
    val iteratorName: String = "item",
    val key: String? = null,
    val templates: List<Template>? = null,
) : WidgetView(), ContextComponent, OnInitiableComponent by OnInitiableComponentImpl(onInit) {

    /**
     * @param children define the items on the list view.
     * @param direction define the list direction.
     */
    @Deprecated(message = "It was deprecated in version 1.5 and will be removed in a future version. " +
        "Use dataSource and template instead children.",
        replaceWith = ReplaceWith(
            "ListView(direction, context, onInit, dataSource, template, onScrollEnd, scrollEndThreshold," +
                "iteratorName, key)"))
    constructor(
        children: List<ServerDrivenComponent>? = null,
        direction: ListDirection,
    ) : this(
        children = children,
        direction = direction,
        context = null
    )

    /**
     * @param direction define the list direction.
     * @param context define the contextData that be set to component.
     * @param onInit allows to define a list of actions to be performed when the Widget is displayed.
     * @param dataSource it's an expression that points to a list of values used to populate the Widget.
     * @param template represents each cell in the list through a ServerDrivenComponent.
     * @param onScrollEnd list of actions performed when the list is scrolled to the end.
     * @param scrollEndThreshold sets the scrolled percentage of the list to trigger onScrollEnd.
     * @param isScrollIndicatorVisible this attribute enables or disables the scroll bar.
     * @param iteratorName is the context identifier of each cell.
     * @param key points to a unique value present in each dataSource item
     * used as a suffix in the component ids within the Widget.
     */
    @Deprecated(message = "It was deprecated in version 1.7 and will be removed in a future version. " +
        "Use templates instead template",
        replaceWith = ReplaceWith(
            "ListView(direction, context, onInit, dataSource, onScrollEnd, scrollEndThreshold," +
                "iteratorName, key, templates)"))
    constructor(
        direction: ListDirection,
        context: ContextData? = null,
        onInit: List<Action>? = null,
        dataSource: Bind<List<Any>>,
        template: ServerDrivenComponent,
        onScrollEnd: List<Action>? = null,
        scrollEndThreshold: Int? = null,
        isScrollIndicatorVisible: Boolean = false,
        iteratorName: String = "item",
        key: String? = null,
    ) : this(
        null,
        direction,
        context,
        onInit,
        dataSource,
        template,
        onScrollEnd,
        scrollEndThreshold,
        isScrollIndicatorVisible,
        iteratorName,
        key,
        null
    )

    /**
     * @param direction define the list direction.
     * @param context define the contextData that be set to component.
     * @param onInit allows to define a list of actions to be performed when the Widget is displayed.
     * @param dataSource it's an expression that points to a list of values used to populate the Widget.
     * @param onScrollEnd list of actions performed when the list is scrolled to the end.
     * @param scrollEndThreshold sets the scrolled percentage of the list to trigger onScrollEnd.
     * @param isScrollIndicatorVisible this attribute enables or disables the scroll bar.
     * @param iteratorName is the context identifier of each cell.
     * @param key points to a unique value present in each dataSource item used as a suffix in the component ids within
     * the Widget.
     * @param templates Multiple templates support. The template to use will be decided according to the property `case`
     * of the template. The first template where `case` is `true` is the template chosen to render an item. If for every
     * template `case` is `false`, then, the first template where `case` is omitted (default template) is used.
     */
    constructor(
        direction: ListDirection,
        context: ContextData? = null,
        onInit: List<Action>? = null,
        dataSource: Bind<List<Any>>,
        onScrollEnd: List<Action>? = null,
        scrollEndThreshold: Int? = null,
        isScrollIndicatorVisible: Boolean = false,
        iteratorName: String = "item",
        key: String? = null,
        templates: List<Template>,
    ) : this(
        null,
        direction,
        context,
        onInit,
        dataSource,
        template = null,
        onScrollEnd,
        scrollEndThreshold,
        isScrollIndicatorVisible,
        iteratorName,
        key,
        templates,
    )

    @Transient
    var numColumns: Int = 0

    @Transient
    private var canScrollEnd = true

    @Transient
    private lateinit var recyclerView: BeagleRecyclerView

    @Transient
    private lateinit var rootView: RootView

    @Transient
    private lateinit var listViewIdViewModel: ListViewIdViewModel

    override fun buildView(rootView: RootView): View {
        this.rootView = rootView
        val hasTemplate = template != null || templates != null
        return if (children.isNullOrEmpty() && hasTemplate && dataSource != null) {
            buildNewListView()
        } else {
            buildOldListView()
        }
    }

    private fun getLayoutManager(context: Context): RecyclerView.LayoutManager {
        return if (numColumns <= 0) {
            val orientation = listDirectionToRecyclerViewOrientation()
            LinearLayoutManager(context, orientation, false)
        } else {
            GridLayoutManager(context, numColumns, getGridDirection(), false)
        }
    }

    private fun getGridDirection() =
        if (direction == ListDirection.HORIZONTAL) GridLayoutManager.HORIZONTAL else GridLayoutManager.VERTICAL

    private fun buildOldListView(): View {
        val recyclerView = ViewFactory.makeRecyclerView(rootView.getContext())
        children?.let { children ->
            recyclerView.apply {
                val orientation = listDirectionToRecyclerViewOrientation()
                layoutManager = LinearLayoutManager(context, orientation, false)
                adapter = ListViewRecyclerAdapter(children, orientation, this@ListView.rootView)
            }
        }
        return recyclerView
    }

    private fun buildNewListView(): View {
        listViewIdViewModel = rootView.generateViewModelInstance()

        val orientation = listDirectionToRecyclerViewOrientation()

        recyclerView = generateRecyclerView(orientation)

        setupRecyclerView(orientation)
        configDataSourceObserver()
        configRecyclerViewScrollListener()
        handleOnInit(rootView, recyclerView)

        return recyclerView
    }

    @Deprecated(message = "It was deprecated in version 1.5 and will be removed in a future version. " +
        "Use new ListView implementation instead.",
        replaceWith = ReplaceWith("buildNewListView()"))
    internal class ListViewRecyclerAdapter(
        val children: List<ServerDrivenComponent>,
        private val orientation: Int,
        private val rootView: RootView,
    ) : RecyclerView.Adapter<ViewHolder>() {

        override fun getItemViewType(position: Int): Int = position

        override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
            val view = ViewFactory.makeBeagleFlexView(rootView).also {
                val width = if (orientation == RecyclerView.VERTICAL) MATCH_PARENT else WRAP_CONTENT
                val layoutParams = ViewGroup.LayoutParams(width, WRAP_CONTENT)
                it.layoutParams = layoutParams
                it.addView(children[position])
            }
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

        override fun getItemCount(): Int = children.size
    }

    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private fun listDirectionToRecyclerViewOrientation() = if (direction == ListDirection.VERTICAL) {
        RecyclerView.VERTICAL
    } else {
        RecyclerView.HORIZONTAL
    }

    private fun generateRecyclerView(orientation: Int): BeagleRecyclerView =
        if (isScrollIndicatorVisible) {
            generateRecyclerViewWithScrollIndicator(orientation)
        } else {
            ViewFactory.makeBeagleRecyclerView(rootView.getContext())
        }


    private fun generateRecyclerViewWithScrollIndicator(orientation: Int): BeagleRecyclerView =
        if (orientation == RecyclerView.VERTICAL) {
            ViewFactory.makeBeagleRecyclerViewScrollIndicatorVertical(rootView.getContext())
        } else {
            ViewFactory.makeBeagleRecyclerViewScrollIndicatorHorizontal(rootView.getContext())
        }


    private fun setupRecyclerView(orientation: Int) {
        val contextAdapter = ListAdapter(
            orientation,
            template,
            iteratorName,
            key,
            ListViewModels(rootView),
            templates,
            recyclerView,
        )
        recyclerView.apply {
            adapter = contextAdapter
            layoutManager = getLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun configDataSourceObserver() {
        observeBindChanges(rootView, recyclerView, dataSource!!) { value ->
            canScrollEnd = true
            val adapter = recyclerView.adapter as ListAdapter
            adapter.setList(value, this.id)
            if (value?.isEmpty() == true) {
                executeScrollEndActions()
            }
        }
    }

    private fun configRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // listen if reach max and notify the ViewModel
                checkIfNeedToCallScrollEnd()
                if (cannotScrollDirectionally()) {
                    listViewIdViewModel.markHasCompletelyLoaded(recyclerView.id)
                }
            }
        })
    }

    private fun checkIfNeedToCallScrollEnd() {
        onScrollEnd?.let {
            if (canCallOnScrollEnd()) {
                executeScrollEndActions()
            }
        }
    }

    private fun executeScrollEndActions() {
        onScrollEnd?.let {
            it.forEach { action ->
                action.execute(rootView, recyclerView)
            }
            canScrollEnd = false
        }
    }

    private fun canCallOnScrollEnd(): Boolean {
        val reachEnd = scrollEndThreshold?.let {
            val scrolledPercent = calculateScrolledPercent()
            scrolledPercent >= scrollEndThreshold
        } ?: cannotScrollDirectionally()
        return reachEnd && canScrollEnd
    }

    private fun cannotScrollDirectionally(): Boolean {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        return layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1
    }

    private fun calculateScrolledPercent(): Float {
        val offset = recyclerView.computeVerticalScrollOffset()
        val extent = recyclerView.computeVerticalScrollExtent()
        val range = recyclerView.computeVerticalScrollRange()

        if (range - extent <= 0.0) {
            return 100f
        }

        return 100.0f * offset / (range - extent).toFloat()
    }
}

class BeagleRecyclerView(context: Context) : RecyclerView(context) {
    override fun setId(id: Int) {
        super.setId(id)
        (adapter as ListAdapter?)?.setRecyclerId(id)
    }
}
