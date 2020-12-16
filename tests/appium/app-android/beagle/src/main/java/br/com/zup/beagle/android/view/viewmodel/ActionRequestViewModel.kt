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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.zup.beagle.android.action.SendRequestInternal
import br.com.zup.beagle.android.data.ActionRequester
import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.android.view.mapper.toResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class Response(
    val statusCode: Int?,
    val data: Any?,
    val headers: Map<String, String> = mapOf(),
    val statusText: String? = null
)

internal sealed class FetchViewState {
    data class Error(val response: Response) : FetchViewState()
    data class Success(val response: Response) : FetchViewState()
}

internal class ActionRequestViewModel(
    private val ioDispatcher: CoroutineDispatcher = CoroutineDispatchers.IO,
    private val actionRequester: ActionRequester = ActionRequester()
) : ViewModel() {

    fun fetch(sendRequest: SendRequestInternal): LiveData<FetchViewState> {
        return FetchActionLiveData(actionRequester, sendRequest, viewModelScope, ioDispatcher)
    }
}

private class FetchActionLiveData(
    private val actionRequester: ActionRequester,
    private val sendRequest: SendRequestInternal,
    private val coroutineScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) : LiveData<FetchViewState>() {

    override fun onActive() {
        if (value == null) {
            fetchData()
        }
    }

    private fun fetchData() {
        coroutineScope.launch(ioDispatcher) {
            try {
                val response = actionRequester.fetchData(sendRequest.toRequestData())
                postValue(FetchViewState.Success(response.toResponse()))
            } catch (exception: BeagleApiException) {
                postValue(FetchViewState.Error(exception.responseData.toResponse()))
            }
        }
    }
}
