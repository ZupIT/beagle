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

package br.com.zup.beagle.widget.action

import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.context.valueOfNullable
import kotlin.properties.Delegates


/**
 * will show dialogues natively, such as an error alert indicating alternative flows, business system errors and others.
 *
 * @param title defines the title on the Dialog.
 * @param message defines the Dialog message.
 * @param labelOk define text of button positive in dialog.
 * @param onPressOk define action of button positive in dialog.
 *
 */
data class Alert(
    val title: Bind<String>?,
    val message: Bind<String>,
    val onPressOk: Action? = null,
    val labelOk: String? = null
) : Action {
    constructor(
        title: String?,
        message: String,
        onPressOk: Action? = null,
        labelOk: String? = null
    ) : this(
        title = valueOfNullable(title),
        message = valueOf(message),
        onPressOk = onPressOk,
        labelOk = labelOk
    )
    class Builder : BeagleBuilder<Alert> {
        var title: Bind<String>? = null
        var message: Bind<String> by Delegates.notNull()
        var onPressOk: Action? = null
        var labelOk: String? = null

        fun title(title: Bind<String>?) = this.apply { this.title = title }
        fun title(title: String) = title(valueOf(title))
        fun message(message: Bind<String>) = this.apply { this.message = message }
        fun message(message: String) = message(valueOf(message))
        fun onPressOk(onPressOk: Action?) = this.apply { this.onPressOk = onPressOk }
        fun labelOk(labelOk: String?) = this.apply { this.labelOk = labelOk }

        fun title(block: () -> Bind<String>?) {
            title(block.invoke())
        }

        fun message(block: () -> Bind<String>) {
            message(block.invoke())
        }

        fun onPressOk(block: () -> Action?) {
            onPressOk(block.invoke())
        }

        fun labelOk(block: () -> String?) {
            labelOk(block.invoke())
        }

        override fun build() = Alert(
            title = title,
            message = message,
            onPressOk = onPressOk,
            labelOk = labelOk
        )
    }
}

fun alert(block: Alert.Builder.() -> Unit): Alert = Alert.Builder().apply(block).build()