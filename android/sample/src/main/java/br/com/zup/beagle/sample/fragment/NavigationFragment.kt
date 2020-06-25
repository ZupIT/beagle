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
import br.com.zup.beagle.analytics.ClickEvent
import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.Touchable
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignItems
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.FlexDirection
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.Screen

class NavigationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Screen(
            child = Container(children = buildChildren()).applyFlex(
                Flex(
                    flexDirection = FlexDirection.COLUMN,
                    justifyContent = JustifyContent.CENTER,
                    alignItems = AlignItems.CENTER,
                    alignContent = AlignContent.SPACE_BETWEEN,
                    grow = 1.0
                )
            ),
            screenAnalyticsEvent = ScreenEvent(
                screenName = "Tela De Navegacao"
            )
        )

        return declarative.toView(this)
    }

    private fun buildChildren(): List<ServerDrivenComponent> {
        return listOf(
            Touchable(
                child = Button(text = "Click to navigate").applyStyle(
                    Style(size = Size(width = UnitValue(80.0, UnitType.PERCENT)))
                ),
                onPress = listOf(Navigate.PushView(Route.Remote("https://t001-2751a.firebaseapp.com/flow/step1.json"))),
                clickAnalyticsEvent = ClickEvent(
                    category = "Categoria",
                    label = "Descrição",
                    value = "Valor"
                )
            )
        )
    }

    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }
}