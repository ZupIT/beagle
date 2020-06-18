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
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.android.view.mapper.toResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

data class Response(
    val statusCode: Int?,
    val data: String,
    val headers: Map<String, String> = mapOf(),
    val statusText: String? = null
)

internal class ActionRequestViewModel(
    private val requester: ActionRequester = ActionRequester()
) : ViewModel() {

    fun fetch(sendRequest: SendRequestInternal): LiveData<FetchViewState> {
        return FetchComponentLiveData(requester, sendRequest, viewModelScope.coroutineContext)
    }

    sealed class FetchViewState {
        data class Error(val response: Response) : FetchViewState()
        data class Success(val response: Response) : FetchViewState()
    }
}

private class FetchComponentLiveData(
    private val requester: ActionRequester,
    private val sendRequest: SendRequestInternal,
    override val coroutineContext: CoroutineContext
) : LiveData<ActionRequestViewModel.FetchViewState>(), CoroutineScope {

    override fun onActive() {
        fetchData()
    }

    private fun fetchData() {
        launch {
            value = try {
                val response = requester.fetchData(sendRequest.toRequestData())
                ActionRequestViewModel.FetchViewState.Success(response.toResponse())
            } catch (exception: BeagleApiException) {
                ActionRequestViewModel.FetchViewState.Error(exception.responseData.toResponse())
            }
        }

    }
}