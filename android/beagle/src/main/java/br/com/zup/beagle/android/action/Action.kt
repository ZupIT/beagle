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

package br.com.zup.beagle.android.action

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.utils.Observer
import br.com.zup.beagle.android.utils.getWithCaller
import br.com.zup.beagle.android.widget.RootView

interface Action {
    fun execute(rootView: RootView)

    /**
     * Retrieves the value if its not an expression or observes the changes if its an expression
     * @property rootView from buildView
     * @property bind is the value that will retrieved or observed
     * @property observes is function that will be called when a expression is evaluated
     */
    fun <T> evaluateBinding(
        rootView: RootView,
        bind: Bind<T>,
        observes: Observer<T>? = null
    ): T? {
        return bind.getWithCaller(rootView, this, observes)
    }
}
