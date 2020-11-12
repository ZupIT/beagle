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
import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.action.SetContext
import br.com.zup.beagle.widget.action.SubmitForm
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.form.SimpleForm
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextInput

data class User(
    val email: String,
    val password: String,
    val placeholderEmail: String,
    val placeholderPassword: String
)

object SimpleFormScreenBuilder {
    fun build() = Screen(
        child = Container(
            children = listOf(
                Text(text = "SimpleForm", textColor = "#ffffff"),
                SimpleForm(
                    context = ContextData(id = "user", value = User(
                        email = "", password = "",
                        placeholderEmail = "Type in your email", placeholderPassword = "Type in your password")
                    ),
                    children = listOf(
                        TextInput(
                            value = expressionOf("@{user.email}"),
                            placeholder = expressionOf("@{user.placeholderEmail}"),
                            onChange = listOf(
                                SetContext(
                                    contextId = "user",
                                    path = "email",
                                    value = "@{onChange.value}"
                                )
                            )
                        ),
                        TextInput(
                            value = expressionOf("@{user.password}"),
                            placeholder = expressionOf("@{user.placeholderPassword}"),
                            onChange = listOf(
                                SetContext(
                                    contextId = "user",
                                    path = "password",
                                    value = "@{onChange.value}"
                                )
                            )
                        ),
                        Button(text = "Click to Submit",
                            onPress = listOf(SubmitForm())
                        ).applyStyle(
                            Style(backgroundColor = "#ffffff",
                                margin = EdgeValue(top = 10.unitReal())
                            )
                        )
                    ),
                    onSubmit = listOf(
                        Alert(
                            title = "Registered data",
                            message = "the email: " + "@{user.email} " + "and the password: " + "@{user.password}"
                        )
                    )
                )
            )
        ).applyStyle(
            Style(backgroundColor = "#fa7f72",
                size = Size(height = 100.00.unitPercent()))
        )
    )
}

