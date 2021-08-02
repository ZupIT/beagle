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
import androidx.lifecycle.ViewModel
import br.com.zup.beagle.android.exception.BeagleException
import java.util.LinkedList

internal const val PARENT_ID_NOT_FOUND = "The parent id not found, check if you call function createIfNotExisting"

internal class GenerateIdViewModel : ViewModel() {

    private val views = mutableMapOf<Int, LocalView>()

    fun createIfNotExisting(parentId: Int) {
        val view = views[parentId]
        if (view == null) {
            views[parentId] = LocalView()
        }
    }

    fun getViewId(parentId: Int): Int {
        val view = views[parentId] ?: throw BeagleException(PARENT_ID_NOT_FOUND)
        return if (!view.created) generateNewViewId(view) else view.temporaryIds.pollFirst()
            ?: throw BeagleException("temporary ids can't be empty")
    }

    private fun generateNewViewId(view: LocalView): Int {
        val id = View.generateViewId()
        view.ids.add(id)
        return id
    }

    fun setViewCreated(parentId: Int) {
        if (views[parentId] == null) throw BeagleException(PARENT_ID_NOT_FOUND)

        views[parentId] = views[parentId]!!.apply {
            created = true
            temporaryIds = LinkedList(ids)
        }
    }
}

data class LocalView(
    val ids: MutableList<Int> = mutableListOf(),
    var temporaryIds: LinkedList<Int> = LinkedList(),
    var created: Boolean = false
)