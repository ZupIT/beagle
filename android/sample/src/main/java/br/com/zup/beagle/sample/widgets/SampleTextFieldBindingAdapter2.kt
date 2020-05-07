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

package br.com.zup.beagle.sample.widgets

import br.com.zup.beagle.core.Binding
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.setup.BindingAdapter
import br.com.zup.beagle.utils.cast

data class SampleTextFieldBinding2(
    val placeholder: Binding<String>
) : ServerDrivenComponent

class SampleTextFieldBindingAdapter2(private val model: SampleTextFieldBinding2
) : BindingAdapter {

    override fun getBindAttributes(): List<Binding<*>> = listOf(
        model.placeholder
    )

    override fun bindModel() {
        val myWidget = SampleTextField(
            placeholder = model.placeholder.value.cast()
        )

        model.placeholder.observes {
            myWidget.onBind(myWidget.copy(placeholder = it))
        }
    }
}