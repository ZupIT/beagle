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

import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import kotlin.properties.Delegates

data class SetContext(
    val contextId: String,
    val value: Any,
    val path: String? = null
) : Action {
    companion object{
        @JvmStatic
        fun builder() = Confirm.Builder()
    }
    class Builder : BeagleWidgetBuilder<SetContext> {
        var contextId: String by Delegates.notNull()
        var value: Any by Delegates.notNull()
        var path: String? = null

        fun contextId(contextId: String) = this.apply { this.contextId = contextId }
        fun value(value: Any) = this.apply { this.value = value }
        fun path(path: String?) = this.apply { this.path = path }

        fun contextId(block: () -> String) {
            contextId(block.invoke())
        }

        fun value(block: () -> Any) {
            value(block.invoke())
        }

        fun path(block: () -> String?) {
            path(block.invoke())
        }

        override fun build() = SetContext(
            contextId = contextId,
            value = value,
            path = path
        )
    }
}

fun setContext(block: SetContext.Builder.() -> Unit) = SetContext.Builder().apply(block).build()
