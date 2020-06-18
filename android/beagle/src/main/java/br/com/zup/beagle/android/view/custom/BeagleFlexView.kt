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
import android.content.Context
import android.view.View
import br.com.zup.beagle.android.engine.mapper.FlexMapper
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.view.YogaLayout
import br.com.zup.beagle.core.FlexComponent
import br.com.zup.beagle.core.GhostComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.android.widget.RootView

@SuppressLint("ViewConstructor")
internal open class BeagleFlexView(
    context: Context,
    flex: Flex,
    private val flexMapper: FlexMapper = FlexMapper(),
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()
) : YogaLayout(context, flexMapper.makeYogaNode(flex)) {

    constructor(
        context: Context,
        flexMapper: FlexMapper = FlexMapper()
    ) : this(context, Flex(), flexMapper)

    fun addView(child: View, flex: Flex) {
        super.addView(child, flexMapper.makeYogaNode(flex))
    }

    fun addServerDrivenComponent(serverDrivenComponent: ServerDrivenComponent, rootView: RootView) {
        val component = if (serverDrivenComponent is GhostComponent) {
            serverDrivenComponent.child
        } else {
            serverDrivenComponent
        }
        val flex = (component as? FlexComponent)?.flex ?: Flex()
        val view = viewRendererFactory.make(serverDrivenComponent).build(rootView)
        view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> invalidate(view) }
        super.addView(view, flexMapper.makeYogaNode(flex))
    }
}
