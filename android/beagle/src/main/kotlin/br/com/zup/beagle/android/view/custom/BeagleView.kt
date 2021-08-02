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

package br.com.zup.beagle.android.view.custom

import android.annotation.SuppressLint
import android.view.View
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.utils.BeagleRetry
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.ScreenRequest
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.view.mapper.toRequestData
import br.com.zup.beagle.android.view.viewmodel.AnalyticsViewModel
import br.com.zup.beagle.android.view.viewmodel.BeagleViewModel
import br.com.zup.beagle.android.view.viewmodel.ViewState
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent

@Deprecated("It was deprecated in version 1.2.0 and will be removed in a future version." +
    " Use OnServerStateChanged instead.", replaceWith = ReplaceWith("OnServerStateChanged",
    "br.com.zup.beagle.android.view.custom.OnServerStateChanged"))
typealias OnStateChanged = (state: BeagleViewState) -> Unit

typealias OnServerStateChanged = (serverState: ServerDrivenState) -> Unit

typealias OnLoadCompleted = () -> Unit

sealed class BeagleViewState {
    data class Error(val throwable: Throwable) : BeagleViewState()
    object LoadStarted : BeagleViewState()
    object LoadFinished : BeagleViewState()
}

@SuppressLint("ViewConstructor")
internal class BeagleView(
    private val rootView: RootView,
    private val viewModel: BeagleViewModel = rootView.generateViewModelInstance(),
) : InternalBeagleFlexView(rootView) {

    @Deprecated("It was deprecated in version 1.2.0 and will be removed in a future version." +
        " Use serverStateChangedListener instead.")
    var stateChangedListener: OnStateChanged? = null

    var serverStateChangedListener: OnServerStateChanged? = null

    var loadCompletedListener: OnLoadCompleted? = null

    @Deprecated(
        message = "It was deprecated in version 1.7.0 and will be removed in a future version. " +
            "Use field httpAdditionalData.", replaceWith = ReplaceWith("loadView(requestData)")
    )
    fun loadView(screenRequest: ScreenRequest) {
        loadView(screenRequest.toRequestData(), null)
    }

    fun loadView(requestData: RequestData) {
        loadView(requestData, null)
    }

    fun updateView(url: String, view: View) {
        val urlFormatted = url.formatUrl()
        loadView(RequestData(url = urlFormatted), view)
    }

    private fun loadView(requestData: RequestData, view: View?) {
        viewModel.fetchComponent(requestData).observe(rootView.getLifecycleOwner(), { state ->
            handleResponse(state, view)
        })
    }

    private fun handleResponse(
        state: ViewState?, view: View?,
    ) {
        when (state) {
            is ViewState.Loading -> handleLoading(state.value)
            is ViewState.Error -> handleError(state.throwable, state.retry)
            is ViewState.DoRender -> renderComponent(state.component, view, state.screenId)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        val state = if (isLoading) {
            BeagleViewState.LoadStarted
        } else {
            BeagleViewState.LoadFinished
        }

        val serverState = if (isLoading) {
            ServerDrivenState.Started
        } else {
            ServerDrivenState.Finished
        }
        serverStateChangedListener?.invoke(serverState)
        stateChangedListener?.invoke(state)
    }

    private fun handleError(throwable: Throwable, retry: BeagleRetry) {
        stateChangedListener?.invoke(BeagleViewState.Error(throwable))
        serverStateChangedListener?.invoke(ServerDrivenState.Error(throwable, retry))
    }

    private fun renderComponent(
        component: ServerDrivenComponent,
        view: View? = null,
        screenIdentifier: String?,
    ) {
        serverStateChangedListener?.invoke(ServerDrivenState.Success)
        if (view != null) {
            removeView(view)
            addServerDrivenComponent(component)
        } else {
            removeAllViewsInLayout()
            addServerDrivenComponent(component)
            loadCompletedListener?.invoke()
        }
        screenIdentifier?.let {
            rootView.generateViewModelInstance<AnalyticsViewModel>().createScreenReport(
                screenIdentifier
            )
        }
    }
}
