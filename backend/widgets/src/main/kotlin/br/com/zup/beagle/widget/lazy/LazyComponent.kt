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

package br.com.zup.beagle.widget.lazy

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.widget.builder.BeagleWidgetBuilder
import kotlin.properties.Delegates

/**
 *  The LazyComponent is used when an asynchronous BFF request is made.
 *  An initialState view is set on this component.
 *  It works like a loading component or a default picture that is set until the request is fulfilled.
 *
 * @param path The URL to make the request.
 * @param initialState
 *                          define a ServerDrivenComponent that is set to be on view while the asynchronous
 *                          request made is being fulfilled.
 *
 */
data class LazyComponent(
    val path: String,
    val initialState: ServerDrivenComponent
) : ServerDrivenComponent {
    class Builder : BeagleWidgetBuilder<LazyComponent> {
        var path: String by Delegates.notNull()
        var initialState: ServerDrivenComponent by Delegates.notNull()

        fun path(path: String) = this.apply { this.path = path }
        fun initialState(initialState: ServerDrivenComponent) = this.apply { this.initialState = initialState }

        fun path(block: () -> String) {
            path(block.invoke())
        }

        fun initialState(block: () -> ServerDrivenComponent) {
            initialState(block.invoke())
        }

        override fun build() = LazyComponent(path, initialState)
    }
}

fun lazyComponent(block: LazyComponent.Builder.() -> Unit) = LazyComponent.Builder().apply(block).build()