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

package br.com.zup.beagle.form

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.context.ContextDataBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.context.ContextData
import br.com.zup.beagle.widget.form.SimpleForm
import kotlin.properties.Delegates

fun simpleForm(block: SimpleFormBuilder.() -> Unit) = SimpleFormBuilder().apply(block).build()

class SimpleFormBuilder: BeagleBuilder<SimpleForm> {
    var context: ContextData by Delegates.notNull()
    var onSubmit: MutableList<Action> by Delegates.notNull()
    var children: MutableList<ServerDrivenComponent> by Delegates.notNull()

    fun context(context: ContextData) = this.apply { this.context = context }

    fun onSubmit(onSubmit: List<Action>) = this.apply { this.onSubmit = onSubmit.toMutableList() }

    fun children(children: List<ServerDrivenComponent>)
        = this.apply { this.children = children.toMutableList() }

    fun context(block: ContextDataBuilder.() -> Unit) {
        context(ContextDataBuilder().apply(block).build())
    }

    fun onSubmit(block: BeagleListBuilder<Action>.() -> Unit) {
        onSubmit(BeagleListBuilder<Action>().apply(block).build())
    }

    fun children(block: BeagleListBuilder<ServerDrivenComponent>.() -> Unit) {
        children(BeagleListBuilder<ServerDrivenComponent>().apply(block).build())
    }

    override fun build() = SimpleForm(
        context = context,
        onSubmit = onSubmit,
        children = children
    )
}