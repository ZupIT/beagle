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

package br.com.zup.beagle.automatedtests.builders

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.setStyle
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.form.SimpleForm
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextInput

data class DataPeople(
    val name: String,
    val surname: String,
    val birthday: String,
    val suggestedEmail: String
)

object ExpressionEscapingScreenBuilder {

    fun build(): Screen {
        return Screen(
            navigationBar = NavigationBar(
                title = "ExpressionEscaping",
                showBackButton = true
            ),
            child = Container(
                children = listOf(
                    SimpleForm(
                        onSubmit = listOf(

                        ),
                        children = listOf(
                            Text(
                                text = "Fill the form",
                                styleId = "DesignSystem.Text.helloWord"
                            ).applyStyle(
                                Style(
                                    margin = EdgeValue(top = UnitValue.real(20), bottom = UnitValue.real(20)),
                                    flex = Flex(
                                        alignSelf = AlignSelf.CENTER
                                    )
                                )
                            ),
                            textInputScreen()
                        ),
                        context = ContextData(
                            id = "escaping",
                            value = DataPeople(
                                name = "",
                                surname = "",
                                birthday = "",
                                suggestedEmail = ""
                            )
                        )
                    )


                )
            ).setStyle { this.flex = Flex(grow = 1.0) }
        )
    }

    private fun textInputScreen(): Container {
        return Container(
            children = listOf(
                createTextInput(
                    textInputPlaceholder = "Name",
                    textInputValue = "@{escaping.name}",
                    contextPath = "name"
                ),
                createTextInput(
                    textInputPlaceholder = "Surname",
                    textInputValue = "@{escaping.surname.test}",
                    contextPath = "surname"
                ),
                createTextInput(
                    textInputPlaceholder = "Birthday",
                    textInputValue = "\\@{escaping.birthday}",
                    contextPath = "birthday",
                    type = TextInputType.DATE
                ),
                createTextInput(
                    textInputPlaceholder = "Suggested email",
                    textInputValue = "@{escaping.name}@{escaping.surname}@gmail.com",
                    contextPath = "suggestedEmail"
                )
            )
        )
    }

    private fun createTextInput(
        textInputPlaceholder: String,
        textInputValue: String,
        contextPath: String,
        type: TextInputType? = null
    ): TextInput = TextInput(
        placeholder = textInputPlaceholder,
        value = textInputValue,
        type = type,
        styleId = "DesignSystem.TextInput.Style.Bff",
        onChange = listOf(
            SetContext(
                contextId = "escaping",
                path = contextPath,
                value = "@{onChange.value}"
            )
        )
    ).setStyle { styleMargin }

    var styleMargin = Style(
        margin = EdgeValue(
            top = UnitValue.real(10),
            left = UnitValue.real(25),
            right = UnitValue.real(25)
        )
    )
}
