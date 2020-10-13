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
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.data.ComponentRequester
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.logger.BeagleLoggerProxy
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import br.com.zup.beagle.android.utils.removeBaseUrl
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference

sealed class ViewState {
    data class Error(val throwable: Throwable, val retry: BeagleRetry) : ViewState()
    data class Loading(val value: Boolean) : ViewState()
    data class DoRender(val screenId: String?, val component: ServerDrivenComponent) : ViewState()
}

internal class BeagleViewModel(
    private val ioDispatcher: CoroutineDispatcher = CoroutineDispatchers.IO,
    private val componentRequester: ComponentRequester = ComponentRequester()
) : ViewModel() {

    fun fetchComponent(screenRequest: ScreenRequest, screen: ServerDrivenComponent? = null): LiveData<ViewState> {
        return FetchComponentLiveData(screenRequest, screen, componentRequester,
            viewModelScope, ioDispatcher)
    }

    fun fetchForCache(url: String) = viewModelScope.launch(ioDispatcher) {
        try {
            componentRequester.fetchComponent(ScreenRequest(url))
        } catch (exception: BeagleException) {
            BeagleLoggerProxy.warning(exception.message)
        }
    }

    private class FetchComponentLiveData(
        private val screenRequest: ScreenRequest,
        private val screen: ServerDrivenComponent?,
        private val componentRequester: ComponentRequester,
        private val coroutineScope: CoroutineScope,
        private val ioDispatcher: CoroutineDispatcher) : LiveData<ViewState>() {

        private val isRenderedReference = AtomicReference(false)

        override fun onActive() {
            if (isRenderedReference.get().not()) {
                fetchComponents()
            }
        }

        private fun fetchComponents() {
            coroutineScope.launch(ioDispatcher) {
                val identifier = getComponentIdentifier()
                if (screenRequest.url.isNotEmpty()) {
                    try {
                        setLoading(true)
                        val component = componentRequester.fetchComponent(screenRequest)
                        val relativePath = screenRequest.url.removeBaseUrl()
                        postLiveDataResponse(ViewState.DoRender(relativePath, component))
                    } catch (exception: BeagleException) {
                        if (screen != null) {
                            postLiveDataResponse(ViewState.DoRender(identifier, screen))
                        } else {
                            postLiveDataResponse(ViewState.Error(exception) { fetchComponents() })
                        }
                    }
                } else if (screen != null) {
                    postLiveDataResponse(ViewState.DoRender(identifier, screen))
                }
            }
        }

        private fun getComponentIdentifier() = (screen as? ScreenComponent)?.identifier ?: getIdentifierComponentId()

        private fun getIdentifierComponentId() = (screen as? IdentifierComponent)?.id

        private suspend fun postLiveDataResponse(viewState: ViewState) {
            postValue(viewState)
            setLoading(false)
            isRenderedReference.set(true)
        }

        private suspend fun setLoading(loading: Boolean) {
            withContext(coroutineScope.coroutineContext) {
                value = ViewState.Loading(loading)
            }
        }
    }
}
