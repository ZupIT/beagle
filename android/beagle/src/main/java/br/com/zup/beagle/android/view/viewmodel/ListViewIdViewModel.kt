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

package br.com.zup.beagle.android.view.viewmodel

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.utils.toAndroidId
import java.util.LinkedList

internal const val NO_ID_RECYCLER = "RecyclerView Id can't be -1"

internal class ListViewIdViewModel : ViewModel() {

    private val internalIdsByListId = mutableMapOf<Int, LocalListView>()

    fun createSingleManagerByListViewId(recyclerViewId: Int, previouslyEmpty: Boolean = true) {
        require(recyclerViewId != View.NO_ID) { NO_ID_RECYCLER }
        val listViewManager = internalIdsByListId[recyclerViewId]?.run {
            completelyLoaded = false
            val shouldReuse = !previouslyEmpty || reused
            if (shouldReuse) {
                markToReuse(this)
            }
        }
        if (listViewManager == null) {
            internalIdsByListId[recyclerViewId] = LocalListView()
        }
    }

    fun setViewId(recyclerViewId: Int, position: Int, viewId: Int): Int {
        require(recyclerViewId != View.NO_ID) { NO_ID_RECYCLER }
        val listViewManager = retrieveManager(recyclerViewId, position)
        return if (listViewManager.reused) {
            pollOrGenerateANewId(recyclerViewId, position) {
                generateNewViewId(listViewManager, position)
            }
        } else {
            addIdToLocalListView(listViewManager, position, viewId)
            viewId
        }
    }

    fun getViewId(recyclerViewId: Int, position: Int): Int {
        require(recyclerViewId != View.NO_ID) { NO_ID_RECYCLER }
        val listViewManager = retrieveManager(recyclerViewId, position)
        return pollOrGenerateANewId(recyclerViewId, position) {
            generateNewViewId(listViewManager, position)
        }
    }

    fun getViewId(recyclerViewId: Int, position: Int, componentId: String, itemSuffix: String): Int {
        require(recyclerViewId != View.NO_ID) { NO_ID_RECYCLER }
        val listViewManager = retrieveManager(recyclerViewId, position)
        return pollOrGenerateANewId(recyclerViewId, position) {
            generateNewViewId(listViewManager, position, componentId, itemSuffix)
        }
    }

    private fun pollOrGenerateANewId(recyclerViewId: Int, position: Int, generateNewViewId: () -> Int): Int {
        val listViewManager = retrieveManager(recyclerViewId, position)
        return if (!listViewManager.reused) {
            generateNewViewId()
        } else {
            val firstId = listViewManager.temporaryIdsByAdapterPosition[position]?.pollFirst()
            firstId ?: run {
                if (!listViewManager.completelyLoaded) {
                    generateNewViewId()
                } else {
                    throw BeagleException("Temporary ids can't be empty")
                }
            }
        }
    }

    private fun retrieveManager(recyclerViewId: Int, position: Int) = internalIdsByListId[recyclerViewId]
        ?: throw BeagleException(
            "The list id $recyclerViewId which this view in position $position belongs to, was not found"
        )

    private fun generateNewViewId(localListView: LocalListView, position: Int): Int {
        val id = View.generateViewId()
        addIdToLocalListView(localListView, position, id)
        return id
    }

    private fun generateNewViewId(
        localListView: LocalListView,
        position: Int,
        componentId: String,
        itemSuffix: String
    ): Int {
        val id = "$componentId:$itemSuffix".toAndroidId()
        addIdToLocalListView(localListView, position, id)
        return id
    }

    private fun addIdToLocalListView(localListView: LocalListView, position: Int, id: Int) {
        localListView.idsByAdapterPosition[position]?.run {
            if (!contains(id)) {
                add(id)
            }
        } ?: run {
            val listId = LinkedList<Int>()
            listId.add(id)
            localListView.idsByAdapterPosition[position] = listId
        }
    }

    fun prepareToReuseIds(viewBeingDestroyed: View) {
        internalIdsByListId[viewBeingDestroyed.id]?.let { localListView ->
            markToReuse(localListView)
            localListView.idsByAdapterPosition.values.forEach { internalListId ->
                markToReuseEachNestedList(internalListId)
            }
        }
        if (viewBeingDestroyed is ViewGroup) {
            val count = viewBeingDestroyed.childCount
            for (i in 0 until count) {
                val child = viewBeingDestroyed.getChildAt(i)
                prepareToReuseIds(child)
            }
        }
    }

    private fun markToReuseEachNestedList(internalListId: LinkedList<Int>) {
        internalListId.forEach { internalId ->
            internalIdsByListId[internalId]?.let { internalLocalListView ->
                markToReuse(internalLocalListView)
            }
        }
    }

    private fun markToReuse(localListView: LocalListView) {
        localListView.apply {
            reused = true
            temporaryIdsByAdapterPosition.clear()
            idsByAdapterPosition.forEach { (key, value) ->
                temporaryIdsByAdapterPosition[key] = LinkedList(value)
            }
        }
    }

    fun markHasCompletelyLoaded(recyclerViewId: Int) {
        require(recyclerViewId != View.NO_ID) { NO_ID_RECYCLER }
        internalIdsByListId[recyclerViewId]?.apply {
            completelyLoaded = true
        }
    }
}

data class LocalListView(
    val idsByAdapterPosition: MutableMap<Int, LinkedList<Int>> = mutableMapOf(),
    var temporaryIdsByAdapterPosition: MutableMap<Int, LinkedList<Int>> = mutableMapOf(),
    var reused: Boolean = false,
    var completelyLoaded: Boolean = false
)
