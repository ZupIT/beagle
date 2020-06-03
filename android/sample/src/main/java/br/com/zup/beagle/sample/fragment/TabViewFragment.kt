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
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextAlignment

class TabViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = TabView(
            style = "DesignSystem.TabView.Custom",
            tabItems = listOf(
                buildTabView(
                    title = "Title 1",
                    content = Container(children = listOf(
                        Text("Content").applyFlex(
                            Flex(
                                margin = EdgeValue(
                                    top = UnitValue(
                                        10.0,
                                        UnitType.REAL
                                    )
                                )
                            )
                        ),
                        Image("imageBeagle")
                    ))),
                buildTabView(title = "Title 2", content = Button("button")),
                buildTabView(
                    title = "Title 3",
                    content = Container(
                        children = listOf(
                            Text("text tab 3", alignment = TextAlignment.CENTER)
                        )
                    )
                ),
                buildTabView(
                    title = "Title 4",
                    content =
                    Text("text").applyFlex(
                        Flex(
                            justifyContent = JustifyContent.CENTER,
                            alignItems = AlignItems.CENTER
                        )
                    )
                ),
                buildTabView(
                    title = "Title 5",
                    content =
                    Text("text").applyFlex(
                        Flex(
                            justifyContent = JustifyContent.FLEX_START,
                            alignItems = AlignItems.FLEX_END
                        )
                    )
                )
            )
        )

        return context?.let { declarative.toView(this) }
    }

    private fun buildTabView(title: String, content: ServerDrivenComponent): TabItem {
        return TabItem(
            title = title,
            content = content,
            icon = "ic_launcher_foreground"
        )
    }

    companion object {
        fun newInstance(): TabViewFragment {
            return TabViewFragment()
        }
    }
}