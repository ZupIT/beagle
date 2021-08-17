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

package br.com.zup.beagle.android.components.layout

import android.view.View
import br.com.zup.beagle.analytics.ScreenAnalytics
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.ToolbarManager
import br.com.zup.beagle.android.view.BeagleActivity
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.Flex

@RegisterWidget("screenComponent")
internal data class ScreenComponent(
    @Deprecated(
        "It was deprecated in version 1.5.0 and will be removed in a future version. Use field id instead."
    )
    val identifier: String? = null,
    val safeArea: SafeArea? = null,
    val navigationBar: NavigationBar? = null,
    override val child: ServerDrivenComponent,
    @Deprecated("It was deprecated in version 1.10.0 and will be removed in a future version." +
        " Use the new analytics.")
    override val screenAnalyticsEvent: ScreenEvent? = null,
    override val context: ContextData? = null,
) : WidgetView(), ScreenAnalytics, ContextComponent, SingleChildComponent {

    @Transient
    private val toolbarManager: ToolbarManager = ToolbarManager()

    override fun buildView(rootView: RootView): View {
        val container = ViewFactory.makeBeagleFlexView(rootView, Style(flex = Flex(grow = 1.0)))

        addNavigationBarIfNecessary(rootView, navigationBar, container)

        container.addView(child)

        screenAnalyticsEvent?.let {
            container.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {
                    BeagleEnvironment.beagleSdk.analytics?.trackEventOnScreenAppeared(it)
                }

                override fun onViewDetachedFromWindow(v: View?) {
                    BeagleEnvironment.beagleSdk.analytics?.trackEventOnScreenDisappeared(it)
                }
            })
        }

        return container
    }

    private fun addNavigationBarIfNecessary(
        rootView: RootView,
        navigationBar: NavigationBar?,
        container: BeagleFlexView,
    ) {

        (rootView.getContext() as? BeagleActivity)?.let {
            if (navigationBar != null) {
                configNavigationBar(rootView, navigationBar, container)
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

    private fun configNavigationBar(
        rootView: RootView,
        navigationBar: NavigationBar,
        container: BeagleFlexView,
    ) {
        (rootView.getContext() as? BeagleActivity)?.let {
            toolbarManager.configureNavigationBarForScreen(it, navigationBar)
            toolbarManager.configureToolbar(rootView, navigationBar, container, this)
        }
    }
}
