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
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.core.TextAlignment
import br.com.zup.beagle.widget.ui.Text
import kotlin.properties.Delegates

fun text(block: TextBuilder.() -> Unit) = TextBuilder().apply(block).build()

class TextBuilder: BeagleBuilder<Text> {
    var text: Bind<String> by Delegates.notNull()
    var styleId: String? = null
    var textColor: Bind<String>? = null
    var alignment: Bind<TextAlignment>? = null

    fun text(text: Bind<String>) = this.apply { this.text = text }
    fun styleId(styleId: String?) = this.apply { this.styleId = styleId }
    fun textColor(textColor: Bind<String>?) = this.apply { this.textColor = textColor }
    fun alignment(alignment: Bind<TextAlignment>?) = this.apply { this.alignment = alignment }

    fun text(block: () -> Bind<String>) {
        text(block.invoke())
    }

    fun styleId(block: () -> String?) {
        styleId(block.invoke())
    }

    fun textColor(block: () -> Bind<String>?) {
        textColor(block.invoke())
    }

    fun alignment(block: () -> Bind<TextAlignment>?) {
        alignment(block.invoke())
    }

    override fun build() = Text(
        text = text,
        styleId = styleId,
        textColor = textColor,
        alignment = alignment
    )
}