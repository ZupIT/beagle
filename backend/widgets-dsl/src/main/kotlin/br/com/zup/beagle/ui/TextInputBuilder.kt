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

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use class TextInput.", ReplaceWith("TextInput()"))
fun textInput(block: TextInputBuilder.() -> Unit) = TextInputBuilder().apply(block).build()

@Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
    " Use class TextInput.", ReplaceWith("TextInput()"))
class TextInputBuilder : BeagleBuilder<TextInput> {
    var value: Bind<String>? = null
    var placeholder: Bind<String>? = null

    @Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
        " Use field enabled to control is enabled or not in this layout.")
    var disabled: Bind<Boolean>? = null
    var enabled: Bind<Boolean>? = null
    var readOnly: Bind<Boolean>? = null
    var type: Bind<TextInputType>? = null
    var hidden: Bind<Boolean>? = null
    var error: Bind<String>? = null
    var showError: Bind<Boolean>? = null
    var styleId: String? = null
    var onChange: MutableList<Action>? = null
    var onFocus: MutableList<Action>? = null
    var onBlur: MutableList<Action>? = null

    fun value(value: Bind<String>?) = this.apply { this.value = value }
    fun placeholder(placeholder: Bind<String>?) = this.apply { this.placeholder = placeholder }

    @Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
        " Use field enabled to control is enabled or not in this layout.",
        ReplaceWith("this.apply { this.enabled = enabled }"))
    fun disabled(disabled: Bind<Boolean>?) = this.apply { this.disabled = disabled }
    fun enabled(enabled: Bind<Boolean>?) = this.apply { this.enabled = enabled }
    fun readOnly(readOnly: Bind<Boolean>?) = this.apply { this.readOnly = readOnly }
    fun type(type: Bind<TextInputType>?) = this.apply { this.type = type }
    fun hidden(hidden: Bind<Boolean>?) = this.apply { this.hidden = hidden }
    fun error(error: Bind<String>?) = this.apply { this.error = error }
    fun showError(showError: Bind<Boolean>?) = this.apply { this.showError = showError }
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

    @Deprecated("It was deprecated in version 1.7.0 and will be removed in a future version." +
        " Use field enabled to control is enabled or not in this layout.",
        ReplaceWith("enabled(block.invoke())"))
    fun disabled(block: () -> Bind<Boolean>?) {
        disabled(block.invoke())
    }

    fun enabled(block: () -> Bind<Boolean>?) {
        enabled(block.invoke())
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

    fun error(block: () -> Bind<String>?) {
        error(block.invoke())
    }

    fun showError(block: () -> Bind<Boolean>?) {
        showError(block.invoke())
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
        error = error,
        showError = showError,
        styleId = styleId,
        onChange = onChange,
        onFocus = onFocus,
        onBlur = onBlur,
        enabled = enabled
    )
}