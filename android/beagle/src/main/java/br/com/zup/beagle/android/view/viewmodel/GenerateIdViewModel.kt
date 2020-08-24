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
import java.util.LinkedList

internal class GenerateIdViewModel : ViewModel() {

    private val views = mutableMapOf<Int, LocalView>()

    fun createIfNotExisting(parentId: Int) {
        val view = views[parentId]
        if (view == null) {
            views[parentId] = LocalView()
        }
    }

    fun getViewId(parentId: Int): Int {
        val view = views[parentId]!!

        if (view.created) {
            return view.temporaryIds.pollFirst()!!
        }

        val id = View.generateViewId()
        view.ids.add(id)

        return id
    }

    fun setViewCreated(parentId: Int) {
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