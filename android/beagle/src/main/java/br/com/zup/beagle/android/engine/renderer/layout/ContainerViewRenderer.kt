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

package br.com.zup.beagle.android.engine.renderer.layout

import android.view.View
import br.com.zup.beagle.android.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container

internal class ContainerViewRenderer(
    override val component: Container,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory()
) : LayoutViewRenderer<Container>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        return viewFactory.makeBeagleFlexView(rootView.getContext(), component.flex ?: Flex())
            .apply {
                addChildren(this, rootView)
            }
    }

    private fun addChildren(beagleFlexView: BeagleFlexView, rootView: RootView) {
        component.children.forEach { child ->
            beagleFlexView.addServerDrivenComponent(child, rootView)
        }
    }
}
