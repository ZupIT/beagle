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

import android.content.Context
import android.view.View
import br.com.zup.beagle.android.engine.renderer.LayoutViewRenderer
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.android.engine.renderer.ViewRendererFactory
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.ToolbarManager
import br.com.zup.beagle.android.utils.configureSupportActionBar
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ViewFactory
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
        addNavigationBarIfNecessary(rootView, component.navigationBar)

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

    private fun addNavigationBarIfNecessary(rootView: RootView, navigationBar: NavigationBar?) {
        (rootView.getContext() as BeagleActivity).let {
            if (navigationBar != null) {
                configNavigationBar(rootView, navigationBar)
            } else {
                hideNavigationBar(it)
            }
        }
    }

    private fun hideNavigationBar(context: BeagleActivity) {
        context.supportActionBar?.apply {
            hide()
        }

        context.getToolbar().visibility = View.GONE
    }

    private fun configNavigationBar(rootView: RootView, navigationBar: NavigationBar) {
        (rootView.getContext() as BeagleActivity).let {
            it.configureSupportActionBar()
            toolbarManager.configureNavigationBarForScreen(it, navigationBar)
            toolbarManager.configureToolbar(rootView, navigationBar)
        }
    }
}
