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

package br.com.zup.beagle.ui

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.core.TextInputType
import br.com.zup.beagle.widget.ui.TextInput

fun textInput(block: TextInputBuilder.() -> Unit) = TextInputBuilder().apply(block).build()

class TextInputBuilder : BeagleBuilder<TextInput> {
    var value: Bind<String>? = null
    var placeholder: Bind<String>? = null
    var disabled: Bind<Boolean>? = null
    var readOnly: Bind<Boolean>? = null
    var type: Bind<TextInputType>? = null
    var hidden: Bind<Boolean>? = null
    var styleId: String? = null
    var onChange: MutableList<Action>? = null
    var onFocus: MutableList<Action>? = null
    var onBlur: MutableList<Action>? = null

    fun value(value: Bind<String>?) = this.apply { this.value = value }
    fun placeholder(placeholder: Bind<String>?) = this.apply { this.placeholder = placeholder }
    fun disabled(disabled: Bind<Boolean>?) = this.apply { this.disabled = disabled }
    fun readOnly(readOnly: Bind<Boolean>?) = this.apply { this.readOnly = readOnly }
    fun type(type: Bind<TextInputType>?) = this.apply { this.type = type }
    fun hidden(hidden: Bind<Boolean>?) = this.apply { this.hidden = hidden }
    fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
    fun onChange(onChange: List<Action>?) = this.apply { this.onChange = onChange?.toMutableList() }
    fun onFocus(onFocus: List<Action>?) = this.apply { this.onFocus = onFocus?.toMutableList() }
    fun onBlur(onBlur: List<Action>?) = this.apply { this.onBlur = onBlur?.toMutableList() }

    fun value(block: () -> Bind<String>?) {
        value(block.invoke())
    }

    fun placeholder(block: () -> Bind<String>?) {
        placeholder(block.invoke())
    }

    fun disabled(block: () -> Bind<Boolean>?) {
        disabled(block.invoke())
    }

    fun readOnly(block: () -> Bind<Boolean>?) {
        readOnly(block.invoke())
    }

    fun type(block: () -> Bind<TextInputType>?) {
        type(block.invoke())
    }

    fun hidden(block: () -> Bind<Boolean>?) {
        hidden(block.invoke())
    }

    fun styleId(block: () -> String?) {
        styleId(block.invoke())
    }

    fun onChange(block: BeagleListBuilder<Action>.() -> Unit) {
        onChange(BeagleListBuilder<Action>().apply(block).buildNullable())
    }

    fun onFocus(block: BeagleListBuilder<Action>.() -> Unit) {
        onFocus(BeagleListBuilder<Action>().apply(block).buildNullable())
    }

    fun onBlur(block: BeagleListBuilder<Action>.() -> Unit) {
        onBlur(BeagleListBuilder<Action>().apply(block).buildNullable())
    }

    override fun build() = TextInput(
        value = value,
        placeholder = placeholder,
        disabled = disabled,
        readOnly = readOnly,
        type = type,
        hidden = hidden,
        styleId = styleId,
        onChange = onChange,
        onFocus = onFocus,
        onBlur = onBlur
    )
}