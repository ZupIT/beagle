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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.data.ComponentRequester
import br.com.zup.beagle.android.exception.BeagleApiException
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.logger.BeagleLoggerProxy
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import br.com.zup.beagle.android.utils.implementsGenericTypeOf
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.core.ServerDrivenComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.lang.ClassCastException
import java.util.*
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

    private val urlObservableReference = AtomicReference(UrlObservable())

    fun fetchComponent(screenRequest: ScreenRequest, screen: ScreenComponent? = null): LiveData<ViewState> {
        return FetchComponentLiveData(screenRequest, screen, componentRequester,
            urlObservableReference, viewModelScope, ioDispatcher)
    }

    fun fetchForCache(url: String) = viewModelScope.launch(ioDispatcher) {
        try {
            urlObservableReference.get().addUrl(url)
            val component = componentRequester.fetchComponent(ScreenRequest(url))
            urlObservableReference.get().notifyLoaded(url, component)
        } catch (exception: BeagleException) {
            BeagleLoggerProxy.warning(exception.message)
        }
    }

    //TODO Refactor this to use coroutines flow
    private class UrlObservable : Observable() {
        private var loadingUrlSet = mutableSetOf<String>()

        fun hasUrl(url: String) = loadingUrlSet.contains(url)

        fun addUrl(url: String) {
            loadingUrlSet.add(url)
            setChanged()
        }

        fun removeUrl(url: String) {
            loadingUrlSet.remove(url)
            setChanged()
        }

        fun notifyLoaded(url: String, component: ServerDrivenComponent) {
            removeUrl(url)
            val pair = url to component
            notifyObservers(pair)
        }

        fun notifyError(url: String, exception: BeagleException) {
            removeUrl(url)
            val pair = url to exception
            notifyObservers(pair)
        }
    }

    private class FetchComponentLiveData(
        private val screenRequest: ScreenRequest,
        private val screen: ScreenComponent?,
        private val componentRequester: ComponentRequester,
        private val urlObservable: AtomicReference<UrlObservable>,
        private val coroutineScope: CoroutineScope,
        private val ioDispatcher: CoroutineDispatcher) : LiveData<ViewState>() {

        private val isRenderedReference = AtomicReference(false)

        override fun onActive() {
            if (isRenderedReference.get().not()) {
                fetchComponents()
            }
        }

        private fun fetchComponents() {
            if (screenRequest.url.isNotEmpty()) {
                if (!isUrlFetchQueued(screenRequest.url)) {
                    setLoading(true)
                    fetchComponent(screenRequest, screen)
                }
                queueUrl(screenRequest.url)
                observeUrl(screenRequest.url)
            } else if (screen != null) {
                postLivedataResponse(ViewState.DoRender(screen.identifier, screen))
            }
        }

        private fun fetchComponent(screenRequest: ScreenRequest, screen: ScreenComponent?) {
            coroutineScope.launch(ioDispatcher) {
                try {
                    val component = componentRequester.fetchComponent(screenRequest)
                    withContext(coroutineScope.coroutineContext) {
                        urlObservable.get().notifyLoaded(screenRequest.url, component)
                    }
                } catch (exception: BeagleException) {
                    withContext(coroutineScope.coroutineContext) {
                        if (screen != null) {
                            postLivedataResponse(ViewState.DoRender(screen.identifier, screen))
                        } else {
                            urlObservable.get().notifyError(screenRequest.url, exception)
                        }
                    }
                }
            }
        }

        private fun postLivedataResponse(viewState: ViewState) {
            value = viewState
            setLoading(false)
            isRenderedReference.set(true)
        }

        private fun setLoading(loading: Boolean) {
            value = ViewState.Loading(loading)
        }

        private fun isUrlFetchQueued(url: String): Boolean {
            return urlObservable.get().hasUrl(url)
        }

        private fun queueUrl(url: String) {
            urlObservable.get().addUrl(url)
        }

        private fun observeUrl(url: String) {
            urlObservable.get().addObserver(object : Observer {
                override fun update(o: Observable?, arg: Any?) {

                    (arg as Pair<*, *>).let {
                        if (it.second is ServerDrivenComponent) {
                            if (url == it.first) {
                                postLivedataResponse(ViewState.DoRender(url, it.second as ServerDrivenComponent))
                                urlObservable.get().deleteObserver(this)
                            }
                        } else {
                            if (url == it.first) {
                                postLivedataResponse(ViewState.Error(it.second as Throwable) { fetchComponents() })
                                urlObservable.get().deleteObserver(this)
                            }
                        }
                    }
                }
            })
        }

    }
}



