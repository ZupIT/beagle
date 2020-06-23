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

package br.com.zup.beagle.sample.compose.quality

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.sample.constants.BEACH_NETWORK_IMAGE
import br.com.zup.beagle.sample.constants.BLACK
import br.com.zup.beagle.sample.constants.BUTTON_STYLE_FORM
import br.com.zup.beagle.sample.constants.LIGHT_GREEN
import br.com.zup.beagle.sample.constants.LIGHT_GREY
import br.com.zup.beagle.sample.constants.PATH_URL_WEB_VIEW_ENDPOINT
import br.com.zup.beagle.sample.constants.SUBMIT_FORM_ENDPOINT
import br.com.zup.beagle.sample.widget.SampleTextField
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.FormMethodType
import br.com.zup.beagle.widget.action.FormRemoteAction
import br.com.zup.beagle.widget.core.AlignContent
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ListDirection
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.PageView
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.lazy.LazyComponent
import br.com.zup.beagle.widget.pager.PageIndicator
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.TabItem
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.WebView

object ComposePageViewQuality: ComposeComponent {
    override fun build() = PageView(
         pageIndicator = PageIndicator(
             selectedColor = BLACK,
             unselectedColor = LIGHT_GREY
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
                         children = listOf(
                             Container(
                                 children = listOf(
                                     TabView(
                                         children = listOf(tab1, tab2),
                                         styleId = "DesignSystem.TabView.Test"
                                     )
                                 )
                             ),
                             Container(
                                 children = listOf(
                                     TabView(
                                         children = listOf(tab1, tab2),
                                         styleId = "DesignSystem.TabView.Test"
                                     )
                                 )
                             ),
                             Container(
                                 children = listOf(
                                     WebView(url = PATH_URL_WEB_VIEW_ENDPOINT).applyStyle(
                                         style = Style(size = Size(width = 375.unitReal(),height = 600.unitReal()))
                                     )
                                 )
                             )
                         )
                     )
                 )
             ).applyFlex(Flex(grow = 1.0))
             ,
             ScrollView(
                 scrollDirection = ScrollAxis.VERTICAL,
                 children = listOf(
                     ListView(
                         direction = ListDirection.VERTICAL,
                         children = listOf(
                             Text("0000"),
                             Text("0001").applyStyle(style),
                             Text("0002"),
                             Text("0003"),
                             Text("0004"),
                             LazyComponent(
                                 path = "http://www.mocky.io/v2/5e4e91c02f00001f2016a8f2",
                                 initialState = Text("Loading LazyComponent...")
                             ),
                             Text("0005"),
                             Text("0006"),
                             Text("0007"),
                             Text("0008"),
                             Text("0009"),
                             Text("0010"),
                             Text("0011"),
                             Text("0012"),
                             Text("0013"),
                             Image(name = "beagle"),
                             Text("0014"),
                             Text("0015"),
                             Text("0016"),
                             NetworkImage(
                                 path = "https://www.petlove.com.br/images/breeds/193436/profile/original/beagle-p.jpg?1532538271"
                             ),
                             Text("0017"),
                             Text("0018"),
                             Text("0019"),
                             Text("0020"),
                             Container(children = listOf(Text("Text1"), Text("Text2")))
                         )
                     ),
                     Button(
                         text = "PageView Button",
                         onPress = listOf(
                             Alert(
                                 title = "Olá Tudo Bem",
                                 message = "Parabéns não quebrou",
                                 labelOk = "ok"
                             )
                         )
                     )
                 )
             ),
             Container(
                 children = listOf(
                     WebView(url = PATH_URL_WEB_VIEW_ENDPOINT).applyStyle(
                         style = Style(size = Size(width = 375.unitReal(),height = 600.unitReal()))
                     )
                 )
             )
         )
     )

    private val style = Style(size = Size(width = 100.unitReal(), height = 100.unitReal()))

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

    private val horizontalMargin = Style(margin = EdgeValue(all = 10.unitReal()))

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
                                ).applyStyle(horizontalMargin)
                            )
                        )
                    ).applyStyle(
                        style = Style(
                        padding = EdgeValue(all = 10.unitReal()),
                        flex = Flex(grow = 1.0)
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
            ).applyStyle(horizontalMargin)
        )
}