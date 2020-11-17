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
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.action.AsyncAction
import br.com.zup.beagle.android.action.AsyncActionStatus
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
    val listViewModels: ListViewModels
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

    // Struct to manage recycled ViewHolders
    private val recycledViewHolders = mutableListOf<ListViewHolder>()

    // Struct to manage created ViewHolders
    private val createdViewHolders = mutableListOf<ListViewHolder>()

    // Each access generate a new instance of the template to avoid reference conflict
    private val templateJson = serializer.serializeComponent(template)

    init {
        listViewModels.asyncActionViewModel.asyncActionExecuted.observe(
            listViewModels.rootView.getLifecycleOwner(), {
            manageIfInsideRecyclerView(it.origin, it.asyncAction)
        })
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
            addServerDrivenComponent(newTemplate, false)
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
        // Checks and stores holder recycling status
        val isRecycled = recycledViewHolders.contains(holder)
        adapterItems[position].isRecycled = isRecycled
        // Handle context, ids and direct nested adapters
        holder.onBind(parentListViewSuffix, key, adapterItems[position], position, recyclerId)
    }

    override fun onViewRecycled(holder: ListViewHolder) {
        super.onViewRecycled(holder)
        recycledViewHolders.add(holder)
        // Removes the ids of each view previously set to receive new ones
        clearIds(holder.itemView)
        // Iterate over the ImageViews inside each holder and release the downloaded resources
        // before the new image is set
        holder.directNestedImageViews.forEach {
            it.setImageDrawable(null)
        }
    }

    private fun clearIds(view: View) {
        view.id = View.NO_ID
        if (view is ViewGroup) {
            view.children.forEach {
                clearIds(it)
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ListViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.directNestedTextViews.forEach {
            it.requestLayout()
        }
    }

    fun setList(list: List<Any>?, recyclerId: Int) {
        list?.let {
            if (list != listItems) {
                clearAdapterContent()
                setRecyclerId(recyclerId)
                listItems = list
                adapterItems = list.map { ListItem(data = it.normalizeContextValue()) }
                notifyDataSetChanged()
            }
        } ?: clearList()
    }

    private fun clearAdapterContent() {
        adapterItems = emptyList()
        recycledViewHolders.clear()
        createdViewHolders.clear()
    }

    private fun setRecyclerId(incomingRecyclerId: Int) {
        val recyclerIdToUse = when {
            incomingRecyclerId != View.NO_ID -> incomingRecyclerId
            recyclerId != View.NO_ID -> recyclerId
            else -> listViewModels.generateIdViewModel.getViewId(listViewModels.rootView.getParentId())
        }
        listViewModels.listViewIdViewModel.createSingleManagerByListViewId(recyclerIdToUse, listItems.isEmpty())
        recyclerId = recyclerIdToUse
    }

    private fun clearList() {
        val initialSize = adapterItems.size
        clearAdapterContent()
        notifyItemRangeRemoved(0, initialSize)
    }

    override fun getItemCount() = adapterItems.size
}
