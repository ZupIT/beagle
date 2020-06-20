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

package br.com.zup.beagle.sample.builder

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.*
import br.com.zup.beagle.sample.widget.SampleTextField
import br.com.zup.beagle.widget.action.FormMethodType
import br.com.zup.beagle.widget.action.FormRemoteAction
import br.com.zup.beagle.widget.core.*
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.layout.*
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.*

object PageViewScreenBuilder : ScreenBuilder {
    override fun build() = Screen(
        navigationBar = NavigationBar(
            "Beagle PageView",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = "informationImage",
                    action = Alert(
                        title = "PageView",
                        message = "This component is a specialized container " +
                            "to hold pages (views) that may be swiped.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = PageView(
            pageIndicator = PageIndicator(
                selectedColor = "#000000",
                unselectedColor = "#888888"
            ),
            children = listOf(
                ScrollView(
                    scrollDirection = ScrollAxis.VERTICAL,
                    children = listOf(
                        Container(
                            children = listOf(
                                Text("What is Lorem Ipsum? Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.  Why do we use it? It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).", alignment = TextAlignment.CENTER).applyFlex(
                                    Flex(
                                        alignSelf = AlignSelf.CENTER
                                    )
                                ),
                                NetworkImage(BEACH_NETWORK_IMAGE),
                                NetworkImage(BEACH_NETWORK_IMAGE),
                                NetworkImage(BEACH_NETWORK_IMAGE)
                            )
                        ).applyFlex(Flex(grow = 1.0))
                    )
                )
                ,
                Container(
                children = listOf(
                    TabView(
                        children = listOf(tab1, tab2),
                        styleId = "DesignSystem.TabView.Test"
                    )
                )
            ).applyFlex(Flex(grow = 1.0))
                ,
                Container(
                    children = listOf(
                        PageView(
                            pageIndicator = PageIndicator(
                                selectedColor = BLACK,
                                unselectedColor = LIGHT_GREY
                            ),
                            children = (1..3).map {
                                Text("Page $it", alignment = TextAlignment.CENTER).applyFlex(
                                    Flex(
                                        alignSelf = AlignSelf.CENTER,
                                        grow = 1.0
                                    )
                                )
                            }
                        )
                    )
                ).applyFlex(Flex(grow = 1.0))
            )
        )
    )

    private val tab1 = TabItem(
        title = "Tab 1",
        child = ScrollView(
            scrollDirection = ScrollAxis.VERTICAL,
            children = listOf(
                Text("Text1 Tab 2"),
                NetworkImage(BEACH_NETWORK_IMAGE),
                Text("Text2 Tab 2"),
                NetworkImage(BEACH_NETWORK_IMAGE),
                Text("Text3 Tab 3"),
                NetworkImage(BEACH_NETWORK_IMAGE),
                Text("Text1 Tab 2"),
                NetworkImage(BEACH_NETWORK_IMAGE),
                Text("Text2 Tab 2"),
                NetworkImage(BEACH_NETWORK_IMAGE),
                Text("Text3 Tab 3"),
                NetworkImage(BEACH_NETWORK_IMAGE)
            )
        )
    )

    private val flexHorizontalMargin = Flex(margin = EdgeValue(all = 10.unitReal()))

    private val tab2 = TabItem(
        title = "Tab 2",
        child = Container(
            children = listOf(
                Form(
                    onSubmit = listOf(FormRemoteAction(
                        path = SUBMIT_FORM_ENDPOINT,
                        method = FormMethodType.POST
                    )),
                    child = Container(
                        children = listOf(
                            customFormInput(
                                name = "optional-field",
                                placeholder = "Optional field"
                            ),
                            customFormInput(
                                name = "required-field",
                                required = true,
                                validator = "text-is-not-blank",
                                placeholder = "Required field"
                            ),
                            customFormInput(
                                name = "another-required-field",
                                required = true,
                                validator = "text-is-not-blank",
                                placeholder = "Another required field"

                            ),
                            Container(
                                children = emptyList()
                            ).applyFlex(Flex(grow = 1.0)),
                            FormSubmit(
                                enabled = false,
                                child = Button(
                                    text = "Submit Form",
                                    styleId = BUTTON_STYLE_FORM
                                ).applyFlex(flexHorizontalMargin)
                            )
                        )
                    ).applyFlex(
                            Flex(
                                grow = 1.0,
                                padding = EdgeValue(all = 10.unitReal())
                            )
                        ).applyStyle(Style(backgroundColor = LIGHT_GREEN))
                )
            )
        ).applyFlex(Flex(alignContent = AlignContent.CENTER))
    )

    private fun customFormInput(
        name: String,
        required: Boolean? = null,
        validator: String? = null,
        placeholder: String
    ) =
        FormInput(
            name = name,
            required = required,
            validator = validator,
            child = SampleTextField(
                placeholder = placeholder
            ).applyFlex(flexHorizontalMargin)
        )
}
