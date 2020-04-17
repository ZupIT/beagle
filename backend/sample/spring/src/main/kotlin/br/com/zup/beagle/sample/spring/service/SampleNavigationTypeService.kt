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

package br.com.zup.beagle.sample.spring.service

import br.com.zup.beagle.action.Navigate
import br.com.zup.beagle.action.NavigationType
import br.com.zup.beagle.action.ShowNativeDialog
import br.com.zup.beagle.core.Appearance
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.BUTTON_STYLE_APPEARANCE
import br.com.zup.beagle.sample.constants.CYAN_BLUE
import br.com.zup.beagle.sample.constants.CYAN_GREEN
import br.com.zup.beagle.sample.constants.LIGHT_ORANGE
import br.com.zup.beagle.sample.constants.LIGHT_RED
import br.com.zup.beagle.sample.constants.NAVIGATION_TYPE_ENDPOINT
import br.com.zup.beagle.sample.constants.RED
import br.com.zup.beagle.sample.constants.RED_ORANGE
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_TYPE_STEP2_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_NAVIGATION_TYPE_STEP3_ENDPOINT
import br.com.zup.beagle.sample.constants.REPRESENTATION_PRESENT_ENDPOINT
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import org.springframework.stereotype.Service

@Service
class SampleNavigationTypeService {

    private val buttonPopView = createButton(
        text = "POP_VIEW",
        navigationType = NavigationType.POP_VIEW,
        backgroundColor = CYAN_BLUE
    )

    private val buttonAddViewStep1 = createButton(
        text = "ADD_VIEW (Step 1)",
        path = NAVIGATION_TYPE_ENDPOINT,
        navigationType = NavigationType.ADD_VIEW,
        backgroundColor = LIGHT_RED
    )

    fun createNavigationTypeView() = Screen(
        navigationBar = NavigationBar(
            title = "Step 1",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = "informationImage",
                    action = ShowNativeDialog(
                        title = "Navigation Type",
                        message = "Decide the type of navigation.",
                        buttonText = "OK"
                    )
                )
            )
        ),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "ADD_VIEW (Step 2)",
                    path = REPRESENTATION_NAVIGATION_TYPE_STEP2_ENDPOINT,
                    navigationType = NavigationType.ADD_VIEW,
                    backgroundColor = LIGHT_RED
                )
            )
        )
    )


    fun step2() = Screen(
        navigationBar = NavigationBar(
            title = "Step 2",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "ADD_VIEW (Step 3)",
                    path = REPRESENTATION_NAVIGATION_TYPE_STEP3_ENDPOINT,
                    navigationType = NavigationType.ADD_VIEW,
                    backgroundColor = LIGHT_RED
                ),
                createButton(
                    text = "PRESENT_VIEW",
                    path = REPRESENTATION_PRESENT_ENDPOINT,
                    navigationType = NavigationType.PRESENT_VIEW,
                    backgroundColor = LIGHT_ORANGE
                )
            )
        )
    )


    fun presentView() = Screen(
        navigationBar = NavigationBar(
            title = "Present",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
                buttonPopView,
                buttonAddViewStep1,
                createButton(
                    text = "FINISH_VIEW",
                    navigationType = NavigationType.FINISH_VIEW,
                    backgroundColor = CYAN_GREEN
                )
            )
        )
    )


    fun step3() = Screen(
        navigationBar = NavigationBar(
            title = "Step 3",
            showBackButton = true
        ),
        child = Container(
            children = listOf(
                buttonPopView,
                createButton(
                    text = "SWAP_VIEW (Step 1)",
                    path = NAVIGATION_TYPE_ENDPOINT,
                    navigationType = NavigationType.SWAP_VIEW,
                    backgroundColor = RED_ORANGE
                ),
                createButton(
                    text = "POP_TO_VIEW (Step 1)",
                    path = NAVIGATION_TYPE_ENDPOINT,
                    navigationType = NavigationType.POP_TO_VIEW,
                    backgroundColor = RED
                ),
                buttonAddViewStep1

            )
        )
    )

    private fun createButton(
        text: String,
        path: String? = null,
        navigationType: NavigationType,
        backgroundColor: String
    ) =
        Button(
            text = text,
            style = BUTTON_STYLE_APPEARANCE,
            action = Navigate(
                type = navigationType,
                path = path
            )
        ).applyAppearance(
            Appearance(
                backgroundColor = backgroundColor,
                cornerRadius = CornerRadius(radius = 10.0)
            )
        ).applyFlex(
            flex = Flex(
                margin = EdgeValue(
                    left = 30.unitReal(),
                    right = 30.unitReal(),
                    top = 15.unitReal()
                )
            )
        )
}
