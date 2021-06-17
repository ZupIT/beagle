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

package br.com.zup.beagle.sample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.ListView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.refresh.PullToRefresh
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.widget.core.Size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class PullToRefreshFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return buildView().toView(this)
    }

    private fun buildView() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle PullToRefresh",
            showBackButton = true,
            navigationBarItems = listOf()
        ),
        context = ContextData("listContext", listOf("Pull", "to", "refresh", "list")),
        child = PullToRefresh(
            context = ContextData("refreshContext", false),
            onPull = listOf(
                SetContext(
                    contextId = "refreshContext",
                    value = true
                ),
                DynamicSetContext(
                    contextId = "listContext",
                    valueProvider = ::getStringArray,
                    onFinished = SetContext(
                        contextId = "refreshContext",
                        value = false
                    )
                )
            ),
            isRefreshing = expressionOf("@{refreshContext}"),
            color = "#0000FF",
            child = ListView(
                dataSource = expressionOf("@{listContext}"),
                templates = listOf(
                    Template(
                        case = null,
                        view = Text(expressionOf("@{item}"))
                    )
                )
            ).applyStyle(
                style = Style(
                    size = Size(width = 100.unitPercent(), height = 100.unitPercent())
                )
            )
        )
    )

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    private fun randomString() = (1..15)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")

    private suspend fun getStringArray(): List<String> {
        delay(1000)
        return (1..Random.nextInt(100)).map { randomString() }

    }

    companion object {
        fun newInstance() = PullToRefreshFragment()
    }
}

class DynamicSetContext(
    private val contextId: String,
    private val valueProvider: suspend () -> Any,
    private val path: String? = null,
    private val onFinished: Action,
) : Action {

    override fun execute(rootView: RootView, origin: View) {
        CoroutineScope(Dispatchers.Main).launch {
            val value = withContext(Dispatchers.Default) {
                valueProvider.invoke()
            }
            SetContext(contextId, value, path).execute(rootView, origin)

            onFinished.execute(rootView, origin)
        }
    }
}