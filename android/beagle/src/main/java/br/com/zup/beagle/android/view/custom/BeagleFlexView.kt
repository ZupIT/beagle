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
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.YogaLayout
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.GhostComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent

@SuppressLint("ViewConstructor")
internal open class BeagleFlexView(
    private val rootView: RootView,
    style: Style,
    private val flexMapper: FlexMapper = FlexMapper(),
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    private val viewModel: ScreenContextViewModel = rootView.generateViewModelInstance()
) : YogaLayout(rootView.getContext(), flexMapper.makeYogaNode(style)) {

    constructor(
        rootView: RootView,
        flexMapper: FlexMapper = FlexMapper()
    ) : this(rootView, Style(), flexMapper)

    fun addView(child: View, style: Style) {
        super.addView(child, flexMapper.makeYogaNode(style))
    }

    fun addServerDrivenComponent(serverDrivenComponent: ServerDrivenComponent) {
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

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel.linkBindingToContextAndEvaluateThem(this)
    }
}
