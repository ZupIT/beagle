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

package br.com.zup.beagle.widget.form

import br.com.zup.beagle.builder.BeagleListBuilder
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.action.Action
import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import br.com.zup.beagle.widget.context.ContextData
import kotlin.properties.Delegates

/**
 * Component will define a submit handler for a SimpleForm.
 *
 * @param context define the contextData that be set to form
 *
 * @param children define the items on the simple form.
 *
 * @param onSubmit define the actions you want to execute when action submit form
 *
 */
class SimpleForm(
    val context: ContextData,
    val onSubmit: List<Action>,
    val children: List<ServerDrivenComponent>
) : ServerDrivenComponent {
    companion object{
        @JvmStatic
        fun builder() = Builder()
    }
    class Builder : BeagleWidgetBuilder<SimpleForm> {
        var context: ContextData by Delegates.notNull()
        var onSubmit: MutableList<Action> by Delegates.notNull()
        var children: MutableList<ServerDrivenComponent> by Delegates.notNull()

        fun context(context: ContextData) = this.apply { this.context = context }

        fun onSubmit(onSubmit: List<Action>) = this.apply { this.onSubmit = onSubmit.toMutableList() }

        fun children(children: List<ServerDrivenComponent>)
            = this.apply { this.children = children.toMutableList() }

        fun context(block: ContextData.Builder.() -> Unit) {
            context(ContextData.Builder().apply(block).build())
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
}

fun simpleForm(block: SimpleForm.Builder.() -> Unit) = SimpleForm.Builder().apply(block).build()