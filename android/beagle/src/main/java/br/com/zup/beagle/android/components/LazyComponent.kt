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

package br.com.zup.beagle.android.components

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.get
import br.com.zup.beagle.android.view.ServerDrivenState
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleView
import br.com.zup.beagle.android.view.custom.OnServerStateChanged
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.databinding.BeagleIncludeErrorServerDrivenBinding

/**
 *  The LazyComponent is used when an asynchronous BFF request is made.
 *  An initialState view is set on this component.
 *  It works like a loading component or a default picture that is set until the request is fulfilled.
 *
 * @param path The URL to make the request.
 * @param initialState
 *                          define a ServerDrivenComponent that is set to be on view while the asynchronous
 *                          request made is being fulfilled.
 *
 */
@RegisterWidget("lazyComponent")
data class LazyComponent(
    val path: String,
    val initialState: ServerDrivenComponent,
) : WidgetView() {

    override fun buildView(rootView: RootView): View {
        return ViewFactory.makeBeagleView(rootView).apply {
            addServerDrivenComponent(initialState)
            updateView(path, this[0])
            serverStateChangedListener = object : OnServerStateChanged {

                override fun invoke(serverState: ServerDrivenState) {
                    if (serverState is ServerDrivenState.Error) {
                        addErrorScreen(this@apply, serverState)
                    }
                }


            }
        }
    }

    private fun addErrorScreen(beagleView: BeagleView, serverState: ServerDrivenState.Error) {
        beagleView.removeAllViews()
        val binding = BeagleIncludeErrorServerDrivenBinding.inflate(LayoutInflater.from(beagleView.context))
        beagleView.addView(binding.root)
        binding.buttonRetry.setOnClickListener {
            beagleView.removeAllViews()
            beagleView.addServerDrivenComponent(initialState)
            serverState.retry.invoke()
        }
    }
}
