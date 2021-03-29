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
import android.view.ContextThemeWrapper
import android.view.View
import br.com.zup.beagle.android.engine.mapper.FlexMapper
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.utils.GenerateIdManager
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.view.YogaLayout
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.GhostComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeJNIBase

@Suppress("LeakingThis", "LongParameterList")
@SuppressLint("ViewConstructor")
open class InternalBeagleFlexView internal constructor(
    private val rootView: RootView,
    style: Style = Style(),
    private val flexMapper: FlexMapper = FlexMapper(),
    private val viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    private val viewModel: ScreenContextViewModel = rootView.generateViewModelInstance(),
    private val generateIdManager: GenerateIdManager = GenerateIdManager(rootView),
    styleId: Int = 0,
) : YogaLayout(if (styleId == 0) rootView.getContext() else ContextThemeWrapper(rootView.getContext(), styleId),
    flexMapper.makeYogaNode(style)) {

    init {
        observeStyleChanges(style, this, yogaNode)
    }

    internal var listenerOnViewDetachedFromWindow: (() -> Unit)? = null

    internal fun addViewWithStyle(child: View, style: Style) {
        addViewWithBind(style, child, this)
    }

    internal fun addServerDrivenComponent(
        serverDrivenComponent: ServerDrivenComponent,
        addLayoutChangeListener: Boolean = true,
    ) {
        val component = if (serverDrivenComponent is GhostComponent) {
            serverDrivenComponent.child
        } else {
            serverDrivenComponent
        }
        generateIdManager.manageId(component, this)

        val style = (component as? StyleComponent)?.style ?: Style()
        val view = viewRendererFactory.make(serverDrivenComponent).build(rootView)
        if (addLayoutChangeListener) {
            view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                (yogaNode as YogaNodeJNIBase).dirtyAllDescendants()
            }
        }
        addViewWithBind(style, view, view)
    }

    private fun addViewWithBind(style: Style, child: View, viewBind: View) {
        val childYogaNode = flexMapper.makeYogaNode(style)
        observeStyleChanges(style, viewBind, childYogaNode)
        super.addView(child, childYogaNode)
    }

    private fun observeStyleChanges(style: Style, view: View, yogaNode: YogaNode) {
        flexMapper.observeBindChangesFlex(style, rootView, view, yogaNode)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewModel.linkBindingToContextAndEvaluateThem(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listenerOnViewDetachedFromWindow?.invoke()
    }
}
