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

package br.com.zup.beagle.context

import br.com.zup.beagle.widget.interfaces.Observer
import br.com.zup.beagle.widget.state.Observable

internal sealed class Bind<T> {
    abstract val value: Any
    abstract val type: Class<T>

    private val observable = Observable<T>()

    fun observe(observer: (value: T) -> Unit) {
        observable.addObserver(object: Observer<T> {
            override fun update(o: Observable<T>, arg: T) {
                observer(arg)
            }
        })
    }

    fun notifyChanges(value: Any) {
        val newValue = value as T
        observable.notifyObservers(newValue)
    }

    data class Expression<T>(override val value: String, override val type: Class<T>): Bind<T>()
    data class Value<T: Any>(override val value: T, override val type: Class<T>): Bind<T>()
}