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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.zup.beagle.sample.widgets.TextField
import br.com.zup.beagle.sample.widgets.TextFieldInputType
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormInputHidden
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.form.FormRemoteAction
import br.com.zup.beagle.widget.form.FormMethodType
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button

class FormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Form(
            child = Container(
                children = listOf(
                    FormInputHidden(
                        name = "hiddenParam",
                        value = "hiddenParamValue"
                    ),FormInputHidden(
                        name = "hiddenParam1",
                        value = "hiddenParamValue1"
                    ),FormInputHidden(
                        name = "hiddenParam2",
                        value = "hiddenParamValue2"
                    ),FormInputHidden(
                        name = "hiddenParam3",
                        value = "hiddenParamValue3"
                    ),FormInputHidden(
                        name = "hiddenParam4",
                        value = "hiddenParamValue4"
                    ),FormInputHidden(
                        name = "hiddenParam5",
                        value = "hiddenParamValue5"
                    ),
                    FormInput(
                        name = "nome",
                        child = TextField(
                            hint = "nome"
                        )
                    ),
                    FormInput(
                        name = "email",
                        child = TextField(
                            hint = "email"
                        )
                    ),
                    FormInput(
                        name = "senha",
                        child = TextField(
                            hint = "senha",
                            inputType = TextFieldInputType.PASSWORD
                        )
                    ),
                    FormSubmit(
                        child = Button(
                            style = "DesignSystem.Button.Text",
                            text = "submit"
                        ).applyFlex(Flex(margin = EdgeValue(top = UnitValue(30.0, UnitType.REAL))))
                    )
                )
            ).applyFlex(
                Flex(
                    padding = EdgeValue(
                        all = UnitValue(30.0, UnitType.REAL)
                    )
                )
            ),
            action = FormRemoteAction(
                method = FormMethodType.POST,
                path = "endereco/endpoint"
            )
        )


        return declarative.toView(this)
    }

    companion object {
        fun newInstance(): FormFragment {
            return FormFragment()
        }
    }
}
