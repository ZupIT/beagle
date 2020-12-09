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
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.zup.beagle.android.action.Alert
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.NavigationBarItem
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.CYAN_GREEN
import br.com.zup.beagle.sample.constants.LIGHT_RED
import br.com.zup.beagle.sample.constants.RED
import br.com.zup.beagle.sample.constants.RED_ORANGE
import br.com.zup.beagle.widget.core.EdgeValue

class NavigationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative  = Screen(
            child = Container(
                listOf(
                    createButton(
                        text = "Click to navigate",
                        navigate = Navigate.PushView(Route.Local(screen = buildStep1())),
                        backgroundColor = LIGHT_RED
                    )
                )
            )
        )
        return context?.let { declarative.toView(this) }
    }

    private fun buildStep1(): Screen = Screen(
        identifier = "/navigationbar/step1",
        context = ContextData(id = "step2", value = "/navigationbar/step2"),
        navigationBar = buildNavigationBar("Step 1"),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "PushView (Step 2)",
                    navigate = Navigate.PushView(Route.Local(screen = buildStep2())),
                    backgroundColor = LIGHT_RED
                ),
                createButton(
                    text = "PushView (Step 2) with context",
                    navigate = Navigate.PushView(Route.Remote(url = expressionOf("@{step2}"))),
                    backgroundColor = LIGHT_RED
                )
            )
        )
    )

    private fun buildStep2(): Screen = Screen(
        identifier = "/navigationbar/step2",
        navigationBar = buildNavigationBar("Step 2"),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "PushView (Step 3)",
                    navigate = Navigate.PushView(Route.Local(screen = buildStep3())),
                    backgroundColor = LIGHT_RED
                ),
                createButton(
                    text = "PushStack",
                    navigate = Navigate.PushStack(Route.Local(screen = buildNewStack())),
                    backgroundColor = LIGHT_RED
                )
            )
        )
    )

    private fun buildStep3(): Screen = Screen(
        navigationBar = buildNavigationBar("Step 3"),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "ResetApplication (Step 1)",
                    navigate = Navigate.ResetApplication(Route.Local(screen = buildNewStack())),
                    backgroundColor = RED_ORANGE
                ),
                createButton(
                    text = "PopToView (Step 1)",
                    navigate = Navigate.PopToView("/navigationbar/step1"),
                    backgroundColor = RED
                ),
                createButton(
                    text = "PushView (Step 1) - BFF",
                    navigate = Navigate.PushView(Route.Remote(url = "/navigationbar/step1")),
                    backgroundColor = LIGHT_RED
                )
            )
        )
    )

    private fun buildNewStack(): Screen = Screen(
        navigationBar = buildNavigationBar("New Stack"),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "PushView (Step 1) - BFF",
                    navigate = Navigate.PushView(Route.Remote("/navigationbar/step1")),
                    backgroundColor = LIGHT_RED
                ),
                createButton(
                    text = "PopStack",
                    navigate = Navigate.PopStack(),
                    backgroundColor = CYAN_GREEN
                )
            )
        )
    )

    private fun createButton(text: String, navigate: Navigate, backgroundColor: String) =
        Button(
            text = text,
            styleId = "DesignSystem.Stylish.ButtonAndAppearance",
            onPress = listOf(navigate)
        ).applyStyle(
            Style(
                backgroundColor = backgroundColor,
                cornerRadius = CornerRadius(radius = 10.0),
                margin = EdgeValue(
                    left = 30.unitReal(),
                    right = 30.unitReal(),
                    top = 15.unitReal()
                )
            )
        )

    private val buttonPopView = createButton(
        text = "PopView",
        navigate = Navigate.PopView(),
        backgroundColor = CYAN_GREEN
    )

    private fun buildNavigationBar(title: String): NavigationBar {
        return NavigationBar(
            title = title,
            showBackButton = true,
            navigationBarItems = navigationBarImage
        )
    }

    private val navigationBarImage = listOf(
        NavigationBarItem(
            text = "Text",
            image = ImagePath.Local("imageBeagle"),
            action = Alert(
                title = "Navigation Type",
                message = "Decide the type of navigation.",
                labelOk = "OK"
            )
        )
    )

    companion object {
        fun newInstance(): NavigationFragment {
            return NavigationFragment()
        }
    }
}