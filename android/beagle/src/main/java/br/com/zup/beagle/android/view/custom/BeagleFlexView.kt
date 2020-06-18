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
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.GhostComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent


@SuppressLint("ViewConstructor")
internal open class BeagleFlexView(
    context: Context,
    style: Style,
    private val flexMapper: FlexMapper = FlexMapper(),
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory()
) : YogaLayout(context, flexMapper.makeYogaNode(style)) {

    constructor(
        context: Context,
        flexMapper: FlexMapper = FlexMapper()
    ) : this(context, Style(), flexMapper)

    fun addView(child: View, style: Style) {
        super.addView(child, flexMapper.makeYogaNode(style))
    }

    fun addServerDrivenComponent(serverDrivenComponent: ServerDrivenComponent, rootView: RootView) {
        val component = if (serverDrivenComponent is GhostComponent) {
            serverDrivenComponent.child
        } else {
            serverDrivenComponent
        }
        val style = (component as? StyleComponent)?.style ?: Style()
        val view = viewRendererFactory.make(serverDrivenComponent).build(rootView)
        view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ -> invalidate(view) }
        super.addView(view, flexMapper.makeYogaNode(style))
    }
}
