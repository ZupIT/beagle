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

import androidx.lifecycle.ViewModel

internal data class OnInitStatus(
    var onInitCalled: Boolean = false,
    var onInitFinished: Boolean = false
)

internal class OnInitViewModel : ViewModel() {

    private val onInitStatusByViewId: MutableMap<Int, OnInitStatus> = mutableMapOf()

    fun setOnInitCalled(onInitiableViewId: Int, onInitCalled: Boolean) {
        val onInitStatus = onInitStatusByViewId[onInitiableViewId]
        onInitStatus?.let {
            it.onInitCalled = onInitCalled
        } ?: run {
            onInitStatusByViewId[onInitiableViewId] = OnInitStatus(onInitCalled = onInitCalled)
        }
    }

    fun setOnInitFinished(onInitiableViewId: Int, onInitFinished: Boolean) {
        val onInitStatus = onInitStatusByViewId[onInitiableViewId]
        onInitStatus?.let {
            it.onInitFinished = onInitFinished
        } ?: run {
            onInitStatusByViewId[onInitiableViewId] = OnInitStatus(onInitFinished = onInitFinished)
        }
    }

    fun isOnInitCalled(onInitiableViewId: Int) = onInitStatusByViewId[onInitiableViewId]?.onInitCalled ?: false

    fun isOnInitFinished(onInitiableViewId: Int) = onInitStatusByViewId[onInitiableViewId]?.onInitFinished ?: false

    fun markToRerun() {
        onInitStatusByViewId.values.forEach {
            if (!it.onInitFinished) {
                it.onInitCalled = false
            }
        }
    }
}
