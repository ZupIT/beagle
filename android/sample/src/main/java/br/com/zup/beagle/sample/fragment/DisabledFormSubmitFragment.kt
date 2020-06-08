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
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.sample.widgets.MutableText
import br.com.zup.beagle.sample.widgets.TextField
import br.com.zup.beagle.utils.toView
import br.com.zup.beagle.widget.core.EdgeValue
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.form.Form
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormMethodType
import br.com.zup.beagle.widget.form.FormRemoteAction
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.layout.Container
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.TextAlignment

class DisabledFormSubmitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val declarative = makeCharade(
            CharadeInput(
                name = "mary",
                charade = "Mary's mum has 4 children. 1st child's name is April, 2nd child's name" +
                    " is May, 3rd child's name is June. Then what is the name of the 4th child?",
                validator = "Charade"
            )
        )

        return declarative.toView(this)
    }

    private fun makeCharade(charade: CharadeInput): Form {
        return Form(
            child = Container(
                children = listOf(
                    makeCharadeText(charade),
                    makeCharadeAnswer(),
                    makeCharadeAnswerInput(charade),
                    makeCharadeFormSubmit()
                )
            ),
            action = FormRemoteAction(
                method = FormMethodType.POST,
                path = "endereco/endpoint"
            )
        )
    }

    private fun makeCharadeFormSubmit(): ServerDrivenComponent {
        return FormSubmit(
            child = Button(text = "Flag", style = "DesignSystem.Button.Orange").applyFlex(
                flex = Flex(
                    alignSelf = AlignSelf.CENTER,
                    size = Size(
                        width = UnitValue(95.0, UnitType.PERCENT)
                    ),
                    margin = EdgeValue(
                        top = UnitValue(15.0, UnitType.REAL)
                    )
                )
            ),
            enabled = false
        )
    }

    private fun makeCharadeAnswerInput(charade: CharadeInput): ServerDrivenComponent {
        return FormInput(
            name = charade.name,
            child = TextField(
                hint = "answer",
                description = "mary"
            ).applyFlex(
                Flex(
                    margin = EdgeValue(
                        top = UnitValue(10.0, UnitType.REAL)
                    ),
                    size = Size(
                        width = UnitValue(92.0, UnitType.PERCENT)
                    ),
                    alignSelf = AlignSelf.CENTER
                )
            ),
            validator = charade.validator
        )
    }

    private fun makeCharadeAnswer(): ServerDrivenComponent {
        return MutableText(
            firstText = "show answer",
            secondText = "Mary",
            color = "#3380FF"
        ).applyFlex(
            Flex(
                alignSelf = AlignSelf.CENTER,
                margin = EdgeValue(
                    top = UnitValue(5.0, UnitType.REAL)
                )
            )
        )
    }

    private fun makeCharadeText(charade: CharadeInput): ServerDrivenComponent {
        return Text(
            text = charade.charade,
            alignment = TextAlignment.CENTER
        ).applyFlex(
            Flex(
                alignSelf = AlignSelf.CENTER,
                margin = EdgeValue(
                    top = UnitValue(45.0, UnitType.REAL),
                    start = UnitValue(10.0, UnitType.REAL),
                    end = UnitValue(10.0, UnitType.REAL)
                )
            )
        )
    }

    companion object {
        fun newInstance(): DisabledFormSubmitFragment {
            return DisabledFormSubmitFragment()
        }
    }
}

data class CharadeInput(
    val name: String = "",
    val charade: String = "",
    val validator: String = ""
)