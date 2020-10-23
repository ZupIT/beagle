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

package br.com.zup.beagle.android.context

import android.support.v4.util.LruCache
import br.com.zup.beagle.android.utils.Observer

internal data class Binding<T>(
    val observer: Observer<T?>,
    val bind: Bind.Expression<T>,
    val evaluatedExpressions: MutableMap<String, Any> = mutableMapOf()
) {
    fun notifyChanges(value: Any?) {
        try {
            observer.invoke(value as T)
        } catch (ex: ClassCastException) {
            observer.invoke(null)
        }
    }
}

internal data class ContextBinding(
    var context: ContextData,
    val bindings: MutableSet<Binding<*>> = mutableSetOf(),
    val cache: LruCache<String, Any> = LruCache(MAX_SIZE_LRU_CACHE)
) {
    companion object {
        private const val MAX_SIZE_LRU_CACHE = 300
    }
}