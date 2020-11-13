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
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.context.expressionOf
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.NavigationBar
import br.com.zup.beagle.widget.layout.NavigationBarItem
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.ImagePath
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextInput

data class TextInputReadOnly(val value: String, val isReadOnly: Boolean)

data class TextInputType(val placeholder: String, val textInputType: TextInputType)

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
        child = ScrollView(
            children = listOf(
                Container(
                    children = listOf(
                        textInputValue(),
                        textInputPlaceholder(),
                        textInputDisabled(),
                        textInputReadOnly(),
                        textInputSecondPlan(),
                        textInputTypeNumber(),
                        textInputActions(),
                        textInputHidden()
                    )
                ).applyStyle(Style(
                    size = Size(height = 100.0.unitPercent()),
                    backgroundColor = "#000000",
                    padding = EdgeValue(left = 5.unitReal(), bottom = 5.unitReal())))
            )
        )
    )

    private fun textInputValue() = Container(
        context = ContextData(id = "textInput", value = "TextInput with expression"),
        children = listOf(
            TextInput(value = "TextInput test"),
            TextInput(value = expressionOf("@{textInput}"))
        )
    )

    private fun textInputPlaceholder() = Container(
        children = listOf(
            TextInput(placeholder = "TextInput placeholder"),
            Container(
                context = ContextData(id = "isPlaceholder", value = "TextInput placeholder with expression"),
                children = listOf(
                    TextInput(placeholder = expressionOf("@{isPlaceholder}"))
                )
            )
        )
    )

    private fun textInputDisabled() = Container(
        context = ContextData(id = "placeholderValue",
            value = "Standard text with disabled field with expression"),
        children = listOf(
            Container(
                context = ContextData(id = "isDisabled", value = true),
                children = listOf(
                    TextInput(placeholder = "Standard text with disabled field", disabled = true),
                    TextInput(placeholder = expressionOf("@{placeholderValue}"),
                        disabled = expressionOf("@{isDisabled}")
                    )
                )
            )
        )
    )

    private fun textInputReadOnly() = Container(
        context = ContextData(
            id = "isReadOnly", value = TextInputReadOnly(value = "is Read Only with expression", isReadOnly = true)
        ),
        children = listOf(
            TextInput(value = "is Read Only", readOnly = true),
            TextInput(value = expressionOf("@{isReadOnly.value}"),
                readOnly = expressionOf("@{isReadOnly.isReadOnly}")
            )
        )
    )

    private fun textInputTypeNumber() = Container(
        listOf(
            TextInput(placeholder = "is textInput type number", type = TextInputType.NUMBER),
            Container(
                context = ContextData(
                    id = "isNumberExpression",
                    value = TextInputType(placeholder = "is textInput type number with expression",
                        textInputType = TextInputType.NUMBER)
                ),
                children = listOf(
                    TextInput(placeholder = expressionOf("@{isNumberExpression.placeholder}"),
                        type = expressionOf("@{isNumberExpression.textInputType}"))
                )
            )
        )
    )

    private fun textInputSecondPlan() = Container(
        context = ContextData(id = "isTextInputInSecondPlan", value = "is a textInput in second plan with expression"),
        children = listOf(
            TextInput(placeholder = "is a textInput in second plan"),
            TextInput(placeholder = expressionOf("@{isTextInputInSecondPlan}")
            )
        )
    )

    private fun textInputActions() = Container(
        context = ContextData(
            id = "textInputActions", value = ""
        ),
        children = listOf(
            TextInput(
                placeholder = "Unordered actions",
                value = "@{textInputActions.validation}",
                readOnly = true
            ),
            TextInput(
                placeholder = "action validation",
                onChange = listOf(
                    SetContext(contextId = "textInputActions", path = "validation", value = "DidOnChange")),
                onFocus = listOf(
                    SetContext(contextId = "textInputActions", path = "validation", value = "DidOnFocus")),
                onBlur = listOf(
                    SetContext(contextId = "textInputActions", path = "validation", value = "DidOnBlur"))
            ),
            TextInput(
                placeholder = "Ordered actions",
                value = "@{textInputActions.focus}" + "@{textInputActions.change}" + "@{textInputActions.blur}",
                readOnly = true
            ),
            TextInput(
                placeholder = "action order",
                onChange = listOf(
                    SetContext(contextId = "textInputActions", path = "change", value = "DidOnChange")),
                onFocus = listOf(
                    SetContext(contextId = "textInputActions", path = "focus", value = "DidOnFocus")),
                onBlur = listOf(
                    SetContext(contextId = "textInputActions", path = "blur", value = "DidOnBlur"))
            )
        )
    )

    private fun textInputHidden() = Container(
        context = ContextData(
            id = "isHiddenWithExpression",
            value = TextInputHidden(placeholder = "this text is hidden with expression",
                hidden = true)
        ),
        children = listOf(
            TextInput(value = "this text is hidden", hidden = true),
            TextInput(value = expressionOf("@{isHiddenWithExpression.placeholder}"),
                hidden = expressionOf("@{isHiddenWithExpression.hidden}")
            ),
            Text("There are two hidden input fields above")
        )
    )
}
