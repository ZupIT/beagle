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
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.sample.widgets.TextField
import br.com.zup.beagle.sample.widgets.TextFieldInputType
import br.com.zup.beagle.android.utils.toView
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.android.components.form.Form
import br.com.zup.beagle.android.components.form.FormInput
import br.com.zup.beagle.android.components.form.FormSubmit
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.layout.Screen

private const val FORM_GROUP = "FORM_GROUP"

class FormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val declarative = Form(
            additionalData = mapOf(
                "hiddenParam" to "hiddenParamValue",
                "hiddenParam1" to "hiddenParamValue1",
                "hiddenParam2" to "hiddenParamValue2",
                "hiddenParam3" to "hiddenParamValue3",
                "hiddenParam4" to "hiddenParamValue4",
                "hiddenParam5" to "hiddenParamValue5"
            ),
            child = Container(
                children = listOf(
                    FormInput(
                        name = "nome",
                        child = TextField(
                            hint = "nome"
                        )
                    ),
                    FormSubmit(
                        child = Button(
                            styleId = "DesignSystem.Button.Text",
                            text = "submit"
                        ).applyStyle(Style(margin = EdgeValue(top = UnitValue(30.0, UnitType.REAL))))
                    )
                )
            ).applyStyle(
                Style(
                    padding = EdgeValue(
                        all = UnitValue(30.0, UnitType.REAL)
                    )
                )
            ),
            onSubmit = listOf(Navigate.PushView(
                route = Route.Local(
                    screen = screen2()
                )
            )),
            shouldStoreFields = true,
            group = FORM_GROUP
        )


        return declarative.toView(this)
    }

    fun screen2(): Screen {
        return Screen(
            child = Form(
                child = Container(
                    children = listOf(
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
                                styleId = "DesignSystem.Button.Text",
                                text = "submit"
                            ).applyStyle(Style(margin = EdgeValue(top = UnitValue(30.0, UnitType.REAL))))
                        )
                    )
                ),
                additionalData = mapOf(
                    "hiddenParam6" to "hiddenParamValue6"
                ),
                onSubmit = listOf(FormRemoteAction(
                    method = FormMethodType.POST,
                    path = "endereco/endpoint"
                )),
                group = FORM_GROUP

            )
        )
    }

    companion object {
        fun newInstance(): FormFragment {
            return FormFragment()
        }
    }
}
