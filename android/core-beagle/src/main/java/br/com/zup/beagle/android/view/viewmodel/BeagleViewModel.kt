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
import br.com.zup.beagle.android.networking.exception.BeagleApiException
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.core.ServerDrivenComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Observable
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.CoroutineContext

sealed class ViewState {
    data class Error(val throwable: Throwable, val retry: BeagleRetry) : ViewState()
    data class Loading(val value: Boolean) : ViewState()
    data class DoRender(val screenId: String?, val component: ServerDrivenComponent) : ViewState()
}

internal class BeagleViewModel(
    private val componentRequester: ComponentRequester = ComponentRequester()
) : ViewModel() {

    private val urlObservableReference = AtomicReference(UrlObservable())

    fun fetchComponent(screenRequest: ScreenRequest, screen: ScreenComponent? = null): LiveData<ViewState> {
        return FetchComponentLiveData(screenRequest, screen, componentRequester,
            urlObservableReference, viewModelScope.coroutineContext)
    }

    fun fetchForCache(url: String) = viewModelScope.launch {
        try {
            urlObservableReference.get().setLoading(url, true)
            val component = componentRequester.fetchComponent(ScreenRequest(url))
            urlObservableReference.get().notifyLoaded(url, component)
        } catch (exception: BeagleException) {
            BeagleLoggerProxy.warning(exception.message)
        }

        urlObservableReference.get().setLoading(url, false)
    }

    //TODO Refactor this to use coroutines flow
    private class UrlObservable : Observable() {
        private var urlInLoadList = mutableListOf<String>()

        fun hasUrl(url: String) = urlInLoadList.contains(url)

        fun setLoading(url: String, loading: Boolean) {
            if (loading)
                urlInLoadList.add(url)
            else
                urlInLoadList.remove(url)
        }

        fun notifyLoaded(url: String, component: ServerDrivenComponent) {
            urlInLoadList.remove(url)
            val pair = url to component
            notifyObservers(pair)
        }
    }

    private class FetchComponentLiveData(
        private val screenRequest: ScreenRequest,
        private val screen: ScreenComponent?,
        private val componentRequester: ComponentRequester,
        private val urlObservable: AtomicReference<UrlObservable>,
        override val coroutineContext: CoroutineContext) : LiveData<ViewState>(), CoroutineScope {

        private val isRenderedReference = AtomicReference(false)

        override fun onActive() {
            if (isRenderedReference.get().not()) {
                fetchComponents()
            }
        }

        private fun fetchComponents() {
            launch {
                if (screenRequest.url.isNotEmpty()) {
                    try {
                        if (hasFetchInProgress(screenRequest.url)) {
                            waitFetchProcess(screenRequest.url)
                        } else {
                            setLoading(screenRequest.url, true)
                            val component = componentRequester.fetchComponent(screenRequest)
                            postLivedataResponse(ViewState.DoRender(screenRequest.url, component))
                        }
                    } catch (exception: BeagleApiException) {
                        if (screen != null) {
                            postLivedataResponse(ViewState.DoRender(screen.identifier, screen))
                        } else {
                            postLivedataResponse(ViewState.Error(exception) { fetchComponents() })
                        }
                    }
                } else if (screen != null) {
                    postLivedataResponse(ViewState.DoRender(screen.identifier, screen))
                }
            }
        }

        private fun postLivedataResponse(viewState: ViewState) {
            postValue(viewState)
            setLoading(screenRequest.url, false)
            isRenderedReference.set(true)
        }

        private fun setLoading(url: String, loading: Boolean) {
            urlObservable.get().setLoading(url, loading)
            value = ViewState.Loading(loading)
        }


        @Suppress("UNCHECKED_CAST")
        private fun waitFetchProcess(url: String) {
            urlObservable.get().deleteObservers()
            urlObservable.get().addObserver { _, arg ->
                (arg as? Pair<String, ServerDrivenComponent>)?.let {
                    urlObservable.get().setLoading(url, false)
                    if (url == it.first)
                        value = ViewState.DoRender(url, it.second)
                }
            }
        }

        private fun hasFetchInProgress(url: String) =
            urlObservable.get().hasUrl(url)

    }
}



