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

import android.view.View
import androidx.core.view.get
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.lazy.LazyComponent

data class LazyComponent(
    override val path: String,
    override val initialState: ServerDrivenComponent
) : LazyComponent(path, initialState), ViewConvertable {

    @Transient
    private val viewFactory: ViewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        return viewFactory.makeBeagleView(rootView.getContext()).apply {
            addServerDrivenComponent(initialState, rootView)
            updateView(rootView, path, this[0])
        }
    }
}
