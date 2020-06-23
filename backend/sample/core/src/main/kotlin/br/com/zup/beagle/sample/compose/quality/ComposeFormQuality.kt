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
import br.com.zup.beagle.sample.constants.BUTTON_STYLE_FORM
import br.com.zup.beagle.sample.constants.LIGHT_GREEN
import br.com.zup.beagle.sample.constants.SUBMIT_FORM_ENDPOINT
import br.com.zup.beagle.sample.widget.SampleTextField
import br.com.zup.beagle.widget.action.FormMethodType
import br.com.zup.beagle.widget.action.FormRemoteAction
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ScrollAxis
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.layout.ComposeComponent
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.layout.ScrollView
import br.com.zup.beagle.widget.ui.Button

object ComposeFormQuality: ComposeComponent {
    override fun build() = ScrollView(
        scrollDirection = ScrollAxis.VERTICAL,
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
                            placeholder = "Required field",
                            message = "Required Field"
                        ),
                        customFormInput(
                            name = "another-required-field",
                            required = true,
                            validator = "text-is-not-blank",
                            placeholder = "Another required field",
                            message = "Required Field Required FieldRequired FieldRequired FieldRequired FieldRequired FieldRequired Field"
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
//                                size = Size(width = 50.unitPercent(),height = 50.unitPercent())
                    )
                ).applyStyle(Style(backgroundColor = LIGHT_GREEN))
            )
        )
    )

    private val flexHorizontalMargin = Flex(margin = EdgeValue(all = 10.unitReal()))

    private fun customFormInput(
        name: String,
        required: Boolean? = null,
        validator: String? = null,
        placeholder: String,
        message: String? = null
    ) =
        FormInput(
            name = name,
            required = required,
            validator = validator,
            child = SampleTextField(
                placeholder = placeholder
            ).applyFlex(flexHorizontalMargin),
            errorMessage = message
        )
}