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

package br.com.zup.beagle.android.components.list

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.AsyncAction
import br.com.zup.beagle.android.action.AsyncActionStatus
import br.com.zup.beagle.android.context.AsyncActionData
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.utils.setIsAutoGenerateIdEnabled
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent

internal class ListAdapter(
    val orientation: Int,
    val template: ServerDrivenComponent,
    val iteratorName: String,
    val key: String? = null,
    val viewFactory: ViewFactory,
    val listViewModels: ListViewModels,
) : RecyclerView.Adapter<ListViewHolder>() {

    // Recyclerview id for post config changes id management
    private var recyclerId = View.NO_ID

    private var parentListViewSuffix: String? = null

    // Serializer to provide new template instances
    private val serializer = BeagleSerializer()

    // Items captured by ListView
    private var listItems: List<Any> = mutableListOf()

    // Struct that holds all data of each item
    private var adapterItems = listOf<ListItem>()

    // Struct to manage created ViewHolders
    private val createdViewHolders = mutableListOf<ListViewHolder>()

    // Each access generate a new instance of the template to avoid reference conflict
    private val templateJson = serializer.serializeComponent(template)

    private val observer = Observer<AsyncActionData> {
        manageIfInsideRecyclerView(it.origin, it.asyncAction)
    }

    init {
        listViewModels.asyncActionViewModel.asyncActionExecuted.observe(
            listViewModels.rootView.getLifecycleOwner(), observer
        )
    }

    private fun manageIfInsideRecyclerView(origin: View, asyncAction: AsyncAction) {
        if (origin.parent == null) {
            return
        }
        (origin.parent as? RecyclerView)?.let {
            addObserverToHolder(origin, asyncAction)
            return
        } ?: (origin.parent as? View)?.let { parent ->
            manageIfInsideRecyclerView(parent, asyncAction)
        }
    }

    private fun addObserverToHolder(viewCallingAsyncAction: View, asyncAction: AsyncAction) {
        createdViewHolders.forEach { holder ->
            val holderFound = viewCallingAsyncAction.id == holder.itemView.id
            if (holderFound) {
                val observer: Observer<AsyncActionStatus> = holder.observer
                    ?: Observer<AsyncActionStatus> { actionStatus ->
                        if (actionStatus == AsyncActionStatus.STARTED) {
                            holder.setIsRecyclable(false)
                        } else if (actionStatus == AsyncActionStatus.FINISHED) {
                            if (!holder.isRecyclable) {
                                holder.setIsRecyclable(true)
                            }
                        }
                    }
                holder.observer = observer
                asyncAction.status.observe(listViewModels.rootView.getLifecycleOwner(), observer)
                return
            }
        }
    }

    fun setParentSuffix(itemSuffix: String) {
        parentListViewSuffix = itemSuffix
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val newTemplate = serializer.deserializeComponent(templateJson)
        val view = generateView(newTemplate)
        val viewHolder = ListViewHolder(
            view,
            newTemplate,
            serializer,
            listViewModels,
            templateJson,
            iteratorName
        )
        createdViewHolders.add(viewHolder)
        return viewHolder
    }

    private fun generateView(newTemplate: ServerDrivenComponent) =
        viewFactory.makeBeagleFlexView(listViewModels.rootView).apply {
            setIsAutoGenerateIdEnabled(false)
            addView(newTemplate, false)
            layoutParams = generateLayoutParams()
            if (orientation == RecyclerView.VERTICAL) {
                setHeightAutoAndDirtyAllViews()
            } else {
                setWidthAutoAndDirtyAllViews()
            }
        }

    private fun generateLayoutParams(): ViewGroup.LayoutParams {
        val width = if (orientation == RecyclerView.VERTICAL) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }
        return ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.onBind(parentListViewSuffix, key, adapterItems[position], position, recyclerId)
    }

    override fun onViewRecycled(holder: ListViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onViewAttachedToWindow()
    }

    fun setList(list: List<Any>?) {
        list?.let {
            if (list != listItems) {
                clearAdapterContent()
                notifyListViewIdViewModel(listItems.isEmpty())
                listItems = list
                adapterItems = list.map { ListItem(data = it.normalizeContextValue()) }
                notifyDataSetChanged()
            }
        } ?: clearList()
    }

    private fun clearAdapterContent() {
        adapterItems = emptyList()
        createdViewHolders.clear()
    }

    private fun notifyListViewIdViewModel(adapterPreviouslyEmpty: Boolean) {
        listViewModels
            .listViewIdViewModel
            .createSingleManagerByListViewId(getRecyclerId(), adapterPreviouslyEmpty)
    }

    private fun clearList() {
        val initialSize = adapterItems.size
        clearAdapterContent()
        notifyItemRangeRemoved(0, initialSize)
    }

    override fun getItemCount() = adapterItems.size

    fun setRecyclerId(id: Int) {
        if (id != View.NO_ID) {
            recyclerId = id
        }
    }

    private fun getRecyclerId(): Int {
        return recyclerId.takeIf {
            it != View.NO_ID
        } ?: createTempId()
    }

    private fun createTempId(): Int {
        recyclerId = listViewModels
            .generateIdViewModel
            .getViewId(
                listViewModels
                    .rootView
                    .getParentId()
            )

        return recyclerId
    }

    fun clone(): ListAdapter {
        return ListAdapter(
            this.orientation,
            this.template,
            this.iteratorName,
            this.key,
            this.viewFactory,
            this.listViewModels
        )
    }
}
