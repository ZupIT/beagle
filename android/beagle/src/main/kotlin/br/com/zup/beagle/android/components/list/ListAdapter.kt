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
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.components.utils.TemplateJson
import br.com.zup.beagle.android.context.AsyncActionData
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.data.serializer.BeagleSerializer
import br.com.zup.beagle.android.utils.setIsAutoGenerateIdEnabled
import br.com.zup.beagle.android.utils.toAndroidId
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.ServerDrivenComponent

@Suppress("LongParameterList")
internal class ListAdapter(
    val orientation: Int,
    val template: ServerDrivenComponent?,
    val iteratorName: String,
    val key: String? = null,
    val listViewModels: ListViewModels,
    val templateList: List<Template>? = null,
    val originView: View,
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
    private val templateJson = template?.let { serializer.serializeComponent(it) }

    // Each access generate a new instance of the template to avoid reference conflict
    private val templateJsonList = templateList?.let {
        it.map { template ->
            TemplateJson(template.case, serializer.serializeComponent(template.view))
        }
    }

    // This structure keeps track of known view types. This was made to improve performance due to the fact that
    // getItemViewType method is called many times for the same adapter position.
    // If you're extending this class or using it as a guide to your own implementation, it's important to notice that
    // everytime the adapter's content changes, this structure must be invalidated or updated. This includes swapping
    // items in the list or changing data inside an item that will cause the viewType for the item to change.
    private val knownViewTypes = mutableMapOf<Int, Int>()

    // Saves the default template index
    private val defaultTemplateIndex = getDefaultTemplateIndex()

    private val observer = Observer<AsyncActionData> {
        manageIfInsideRecyclerView(it.origin, it.asyncAction)
    }

    companion object {
        private const val NOT_FOUND_TEMPLATE = "{\"_beagleComponent_\": \"beagle:container\"}"
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

    override fun getItemViewType(position: Int): Int {
        return knownViewTypes.getOrPut(position) {
            val itemContext = ContextData(id = iteratorName, value = adapterItems[position].data)
            val viewModel = listViewModels.contextViewModel
            var index = 0
            var found = false
            run loop@{
                templateJsonList?.forEach {
                    when (it.case) {
                        is Bind.Expression -> {
                            if (viewModel.evaluateExpressionForGivenContext(
                                    originView, itemContext, it.case) as Boolean) {
                                found = true
                                return@loop
                            }
                        }
                        else -> {
                            if (it.case != null && it.case.value as Boolean) {
                                found = true
                                return@loop
                            }
                        }
                    }
                    index++
                }
            }
            if (found) index else defaultTemplateIndex
        }
    }

    private fun getTemplateByViewType(viewType: Int): String {
        return templateJsonList?.getOrNull(viewType)?.viewJson ?: (templateJson ?: NOT_FOUND_TEMPLATE)
    }

    private fun getDefaultTemplateIndex(): Int {
        return templateList?.indexOfFirst {
            it.case == null
        } ?: -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val jsonTemplate = getTemplateByViewType(viewType)
        val newTemplate = serializer.deserializeComponent(jsonTemplate)
        val view = generateView(newTemplate)
        val viewHolder = ListViewHolder(
            view,
            newTemplate,
            serializer,
            listViewModels,
            jsonTemplate,
            iteratorName
        )
        createdViewHolders.add(viewHolder)
        return viewHolder
    }

    private fun generateView(newTemplate: ServerDrivenComponent) =
        ViewFactory.makeBeagleFlexView(listViewModels.rootView).apply {
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

    fun setList(list: List<Any>?, componentId: String? = null) {
        list?.let {
            if (list != listItems) {
                clearAdapterContent()
                notifyListViewIdViewModel(listItems.isEmpty(), componentId)
                listItems = list
                adapterItems = list.map { ListItem(data = it.normalizeContextValue()) }
                notifyDataSetChanged()
            }
        } ?: clearList()
    }

    private fun clearAdapterContent() {
        adapterItems = emptyList()
        createdViewHolders.clear()
        knownViewTypes.clear()
    }

    private fun notifyListViewIdViewModel(adapterPreviouslyEmpty: Boolean, componentId: String?) {
        listViewModels
            .listViewIdViewModel
            .createSingleManagerByListViewId(getRecyclerId(componentId), adapterPreviouslyEmpty)
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

    private fun getRecyclerId(componentId: String?): Int {
        val id = recyclerId.takeIf {
            it != View.NO_ID
        } ?: componentId?.toAndroidId()?.takeIf {
            it != View.NO_ID
        }
        return id ?: createTempId()
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
            this.listViewModels,
            this.templateList,
            this.originView
        )
    }
}
