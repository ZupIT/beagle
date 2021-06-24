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

package br.com.zup.beagle.android.components.refresh

import android.graphics.Color
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.ContextComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.valueOfNullable
import br.com.zup.beagle.android.utils.handleEvent
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.core.SingleChildComponent

@RegisterWidget("pullToRefresh")
data class PullToRefresh constructor(
    override val context: ContextData? = null,
    val onPull: List<Action>,
    val isRefreshing: Bind<Boolean>? = null,
    val color: Bind<String>? = null,
    override val child: ServerDrivenComponent,
) : WidgetView(), ContextComponent, SingleChildComponent {

    constructor(
        context: ContextData? = null,
        onPull: List<Action>,
        isRefreshing: Bind<Boolean>? = null,
        color: String? = null,
        child: ServerDrivenComponent,
    ) : this(
        context = context,
        onPull = onPull,
        isRefreshing = isRefreshing,
        color = valueOfNullable(color),
        child = child,
    )

    override fun buildView(rootView: RootView): View {
        return ViewFactory.makeSwipeRefreshLayout(rootView.getContext()).apply {
            addView(buildChildView(rootView))
            setOnRefreshListener {
                handleEvent(rootView, this, onPull)
            }
            observeRefreshState(rootView, this)
            observeColor(rootView, this)
        }
    }

    private fun buildChildView(rootView: RootView) = ViewFactory.makeBeagleFlexView(rootView).apply {
        addView(child, false)
    }

    private fun observeRefreshState(rootView: RootView, view: SwipeRefreshLayout) {
        isRefreshing?.let { bind ->
            observeBindChanges(rootView, view, bind) {
                view.isRefreshing = it ?: false
            }
        }
    }

    private fun observeColor(rootView: RootView, view: SwipeRefreshLayout) {
        color?.let { bind ->
            observeBindChanges(rootView, view, bind) {
                view.setColorSchemeColors(Color.parseColor(it))
            }
        }
    }
}