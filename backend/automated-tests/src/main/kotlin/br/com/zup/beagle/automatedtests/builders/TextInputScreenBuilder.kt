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

import br.com.zup.beagle.widget.action.Alert
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.TextInput

data class TextInputAtt(val placeholder: String, val isReadOnly: Boolean)

data class TextInputTypes(val placeholder: String, val textInputType: TextInputType)

data class TextInputHidden(val placeholder: String, val hidden: Boolean)

object TextInputScreenBuilder {

    fun build() = Screen(
        navigationBar = NavigationBar(
            title = "Beagle Text Input",
            showBackButton = true,
            navigationBarItems = listOf(
                NavigationBarItem(
                    text = "",
                    image = ImagePath.Local.justMobile("informationImage"),
                    action = Alert(
                        title = "Text Input",
                        message = "This widget will define a Text Input view natively using the server driven " +
                            "information received through Beagle.",
                        labelOk = "OK"
                    )
                )
            )
        ),
        child = Container(
            context = ContextData(id = "textInput", value = "TextInput with expression"),
            children = listOf(
                TextInput(value = "TextInput test"),
                TextInput(value = expressionOf("@{textInput}")),
                TextInput(placeholder = "TextInput placeholder"),
                Container(
                    context = ContextData(id = "isPlaceholder", value = "TextInput placeholder with expression"),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isPlaceholder}"))
                    )
                ),
                TextInput(placeholder = "Standard text with disabled field", disabled = true),
                TextInput(placeholder = "non-editable field", readOnly = true),
                Container(
                    context = ContextData(
                        id = "isReadOnly", value = TextInputAtt(placeholder = "is Read Only", isReadOnly = true)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isReadyOnly.placeholder}"),
                            readOnly = expressionOf("@{isReadOnly.isReadyOnly}"))
                    )
                ),
                TextInput(type = TextInputType.DATE),
                TextInput(type = TextInputType.EMAIL),
                TextInput(type = TextInputType.PASSWORD),
                TextInput(type = TextInputType.NUMBER),
                TextInput(type = TextInputType.TEXT),
                Container(
                    context = ContextData(
                        id = "isDateExpression", value = TextInputTypes(placeholder = "is textInput type Date",
                        textInputType = TextInputType.DATE)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isDateExpression.placeholder}"),
                            type = expressionOf("@{isDateExpression.textInputType}"))
                    )
                ),
                Container(
                    context = ContextData(
                        id = "isEmailExpression", value = TextInputTypes(placeholder = "is textInput type email",
                        textInputType = TextInputType.EMAIL)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isEmailExpression.placeholder}"),
                            type = expressionOf("@{isEmailExpression.textInputType}"))
                    )
                ),
                Container(
                    context = ContextData(
                        id = "isPasswordExpression", value = TextInputTypes(placeholder = "is textInput type password",
                        textInputType = TextInputType.PASSWORD)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isPasswordExpression.placeholder}"),
                            type = expressionOf("@{isPasswordExpression.textInputType}"))
                    )
                ),
                Container(
                    context = ContextData(
                        id = "isNumberExpression", value = TextInputTypes(placeholder = "is textInput type number",
                        textInputType = TextInputType.NUMBER)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isNumberExpression.placeholder}"),
                            type = expressionOf("@{isNumberExpression.textInputType}"))
                    )
                ),
                Container(
                    context = ContextData(
                        id = "isTextExpression", value = TextInputTypes(placeholder = "is textInput type text",
                        textInputType = TextInputType.TEXT)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{isTextExpression.placeholder}"),
                            type = expressionOf("@{isTextExpression.textInputType}"))
                    )
                ),
                TextInput(placeholder = "Field will be hidden", hidden = true),
                Container(
                    context = ContextData(
                        id = "isHiddenWithExpression", value = TextInputHidden(placeholder = "is textInput hidden",
                        hidden = true)
                    ),
                    children = listOf(
                        TextInput(placeholder = expressionOf("@{placeholder}"),
                            readOnly = expressionOf("@{placeholder.hidden}")
                        )
                    )
                ),
                TextInput(
                    placeholder = "textInput with onChange", onChange = listOf(
                    Alert(title = "Text input onChange", message = "This is a textInput with onChange action"))
                ),
                TextInput(
                    placeholder = "textInput with onChange", onFocus = listOf(
                    Alert(title = "Text input onFocus", message = "This is a textInput with onFocus action"))
                ),
                TextInput(
                    placeholder = "textInput with onChange", onBlur = listOf(
                    Alert(title = "Text input onBlur", message = "This is a textInput with onBlur action"))
                )
            )
        )
    )
}