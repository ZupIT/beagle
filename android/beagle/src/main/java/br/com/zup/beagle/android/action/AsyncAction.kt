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

package br.com.zup.beagle.android.action

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Status of asynchronous actions
 */
enum class AsyncActionStatus {
    STARTED,
    FINISHED,
    IDLE
}

/**
 * Interface that represents async actions in Beagle
 * @property status represents the current state of execution of the action
 */
interface AsyncAction {

    val status: LiveData<AsyncActionStatus>

    /**
     * Updates action status to started.
     * It is not necessary to call this method for a custom async action.
     * The initial state of the action is already updated automatically by ContextActionExecutor
     */
    fun onActionStarted()

    /**
     * Updates action status to finished
     */
    fun onActionFinished()
}

class AsyncActionImpl : AsyncAction {

    @Transient
    private val _status = MutableLiveData<AsyncActionStatus>()
    override val status get() = _status

    /**
     * Updates action status to started.
     * It is not necessary to call this method for a custom async action.
     * The initial state of the action is already updated automatically by ContextActionExecutor
     */
    override fun onActionStarted() {
        _status.value = AsyncActionStatus.STARTED
    }

    /**
     * Updates action status to finished
     */
    override fun onActionFinished() {
        _status.value = AsyncActionStatus.FINISHED
        _status.value = AsyncActionStatus.IDLE
    }
}
