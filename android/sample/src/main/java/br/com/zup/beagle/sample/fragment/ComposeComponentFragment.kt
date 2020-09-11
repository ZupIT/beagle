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
import br.com.zup.beagle.android.action.FormMethodType
import br.com.zup.beagle.android.action.FormRemoteAction
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.components.form.Form
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.android.components.layout.ComposeComponent
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.sample.widgets.TextField
import br.com.zup.beagle.sample.widgets.TextFieldInputType
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.JustifyContent
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue

class ComposeComponentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = ComposeFormName()

        return declarative.toView(this)
    }

    companion object {
        fun newInstance(): ComposeComponentFragment {
            return ComposeComponentFragment()
        }
    }
}

class ComposeFormName : ComposeComponent() {
    override fun build(): ServerDrivenComponent {
        return Form(
            onSubmit = listOf(FormRemoteAction(
                path = "/validate-name",
                method = FormMethodType.POST
            )),

            child = Container(
                children = listOf(
                    buildContent(),
                    buildFooter()
                )
            ).applyFlex(
                flex = Flex(
                    justifyContent = JustifyContent.SPACE_BETWEEN,
                    grow = 1.0
                )
            )
        )
    }

    private fun buildContent() = Container(
        children = listOf(
            FormInput(
                name = "name",
                child = TextField(
                    description = "digite seu nome",
                    hint = "nome",
                    color = "#FFFFFF",
                    inputType = TextFieldInputType.TEXT
                )
            )
        )
    ).applyStyle(
        style = Style(
            size = Size(
                width = UnitValue(
                    value = 100.0,
                    type = UnitType.PERCENT
                )
            ),
            margin = EdgeValue(
                all = UnitValue(
                    value = 15.0,
                    type = UnitType.REAL
                )
            )
        )
    )

    private fun buildFooter() = Container(
        children = listOf(
            (FormSubmit(
                child = Button("cadastrar", styleId = "primaryButton")
            ))
        )
    ).applyStyle(
        style = Style(
            size = Size(
                width = UnitValue(
                    value = 100.0,
                    type = UnitType.PERCENT
                )
            ),
            padding = EdgeValue(
                left = UnitValue(
                    value = 15.0,
                    type = UnitType.REAL
                ),
                right = UnitValue(
                    value = 15.0,
                    type = UnitType.REAL
                ),
                bottom = UnitValue(
                    value = 15.0,
                    type = UnitType.REAL
                )
            )
        )
    )
}
