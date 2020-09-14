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
import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.ext.applyStyle

class ContextOperationsFragment : Fragment() {

    private val style = Style(margin = EdgeValue(all = UnitValue(50.0, UnitType.REAL)), flex = Flex(shrink = 0.0))

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = screen()

        return context?.let { declarative.toView(this) }
    }

    fun screen() = Screen(
        navigationBar = NavigationBar(
            title = "Choose a Component",
            showBackButton = true
        ),
        child = Container(
            context = ContextData("counter", 2),
            children = listOf(
                Text(expressionOf("Counter: @{sum(2, 1)}")),
                Text(expressionOf("Counter: @{counter}")),
                Button(
                    text = "increment",
                    onPress = listOf(SetContext("counter", "@{sum(counter, 1)}"))
                ),
                Button(
                    text = "decrement",
                    onPress = listOf(SetContext("counter", "@{subtract(counter, 1)}"))
                ),
                Text(text = "The text bellow will show if the counter + 2 is below 5 or not"),
                Text(expressionOf(
                    "@{condition(lt(sum(counter, 2), 5), 'less then 5', 'greater then 5')}")
                ).applyStyle(Style(backgroundColor = "#00FF00"))
            )
        )
    )

    companion object {
        fun newInstance(): ContextOperationsFragment {
            return ContextOperationsFragment()
        }
    }
}