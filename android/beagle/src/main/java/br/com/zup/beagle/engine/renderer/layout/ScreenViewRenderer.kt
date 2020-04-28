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

package br.com.zup.beagle.engine.renderer.layout

import android.content.Context
import android.view.View
import br.com.zup.beagle.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.utils.ToolbarManager
import br.com.zup.beagle.utils.configureSupportActionBar
import br.com.zup.beagle.view.BeagleActivity
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.ScreenComponent

internal class ScreenViewRenderer(
    override val component: ScreenComponent,
    viewRendererFactory: ViewRendererFactory = ViewRendererFactory(),
    viewFactory: ViewFactory = ViewFactory(),
    private val toolbarManager: ToolbarManager = ToolbarManager()
) : LayoutViewRenderer<ScreenComponent>(viewRendererFactory, viewFactory) {

    override fun buildView(rootView: RootView): View {
        addNavigationBarIfNecessary(rootView.getContext(), component.navigationBar)

        val container = viewFactory.makeBeagleFlexView(rootView.getContext(), Flex(grow = 1.0))

        container.addServerDrivenComponent(component.child, rootView)

        component.screenAnalyticsEvent?.let {
            container.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {
                    BeagleEnvironment.beagleSdk.analytics?.sendViewWillAppearEvent(it)
                }

                override fun onViewDetachedFromWindow(v: View?) {
                    BeagleEnvironment.beagleSdk.analytics?.sendViewWillDisappearEvent(it)
                }
            })
        }

        return container
    }

    private fun addNavigationBarIfNecessary(context: Context, navigationBar: NavigationBar?) {
        if (context is BeagleActivity) {
            if (navigationBar != null) {
                configNavigationBar(context, navigationBar)
            } else {
                hideNavigationBar(context)
            }
        }
    }

    private fun hideNavigationBar(context: BeagleActivity) {
        context.supportActionBar?.apply {
            hide()
        }

        context.getToolbar().visibility = View.GONE
    }

    private fun configNavigationBar(
        context: BeagleActivity,
        navigationBar: NavigationBar
    ) {
        context.configureSupportActionBar()
        toolbarManager.configureNavigationBarForScreen(context, navigationBar)
        toolbarManager.configureToolbar(context, navigationBar)
    }
}
