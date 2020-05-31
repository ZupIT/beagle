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

package br.com.zup.beagle.action.request.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.zup.beagle.action.request.data.ActionRequestRepository
import br.com.zup.beagle.action.request.domain.Request
import br.com.zup.beagle.action.request.domain.RequestResponse
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.exception.BeagleException
import br.com.zup.beagle.logger.BeagleLogger
import br.com.zup.beagle.utils.CoroutineDispatchers
import br.com.zup.beagle.view.ScreenRequest
import br.com.zup.beagle.view.viewmodel.ViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

internal class ActionRequestViewModel(
    private val repository: ActionRequestRepository = ActionRequestRepository.Impl()
) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = job + CoroutineDispatchers.Main

    fun fetch(request: Request): LiveData<FetchViewState> {
        val liveData = MutableLiveData<FetchViewState>()
        launch {
            try {
                val response = repository.fetchData(request)
                val state = FetchViewState.Success(response)
                liveData.value = state
            } catch (exception: BeagleException) {
                liveData.value = FetchViewState.Error(exception)
            }
        }
        return liveData
    }

    public override fun onCleared() {
        cancel()
    }


    sealed class FetchViewState {
        data class Error(val throwable: Throwable) : FetchViewState()
        data class Success(val response: RequestResponse) : FetchViewState()
    }
}



