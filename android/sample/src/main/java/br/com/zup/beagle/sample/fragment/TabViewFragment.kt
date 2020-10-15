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
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Condition
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.TabItem
import br.com.zup.beagle.android.components.TabView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.widget.core.TextAlignment

class TabViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = TabView(
            styleId = "DesignSystem.TabView.Custom",
            children = listOf(
                tabViewOne(),
                buildTabView(title = "Title 2", child = Button("button")),
                tabViewThree(),
                tabViewFour(),
                tabViewFive()
            )
        )

        return context?.let { declarative.toView(this) }
    }

    private fun tabViewOne() = buildTabView(
        title = "Title 1",
        child = Container(children = listOf(
            Text("Content").applyStyle(
                Style(
                    margin = EdgeValue(
                        top = UnitValue(
                            10.0,
                            UnitType.REAL
                        )
                    )
                )
            ),
            Image(ImagePath.Local("imageBeagle"))
        )))

    private fun tabViewThree() = buildTabView(
        title = "Title 3",
        child = Container(
            context = ContextData(
                id = "user",
                value = 18
            ),
            children = listOf(
                Text("text tab 3", alignment = TextAlignment.CENTER),
                Button(
                    text = "hello",
                    onPress = listOf(
                        Condition(
                            condition = expressionOf("@{sum(user, 21)}"),
                            onTrue = listOf(
                                Alert(
                                    message = "onTrue"
                                )
                            ),
                            onFalse = listOf(
                                Alert(
                                    message = "onFalse"
                                )
                            )
                        )
                    )
                ).applyStyle(
                    Style(
                        borderColor = "#7CFC00",
                        borderWidth = 5.0
                    )
                )
            )
        )
    )

    private fun tabViewFour() =  buildTabView(
        title = "Title 4",
        child =
        Text("text").applyFlex(
            Flex(
                justifyContent = JustifyContent.CENTER,
                alignItems = AlignItems.CENTER
            )
        )
    )

    private fun tabViewFive() =  buildTabView(
        title = "Title 5",
        child =
        Text("text").applyFlex(
            Flex(
                justifyContent = JustifyContent.FLEX_START,
                alignItems = AlignItems.FLEX_END
            )
        )
    )

    private fun buildTabView(title: String, child: ServerDrivenComponent): TabItem {
        return TabItem(
            title = title,
            child = child,
            icon = ImagePath.Local("ic_launcher_foreground")
        )
    }

    companion object {
        fun newInstance(): TabViewFragment {
            return TabViewFragment()
        }
    }
}
