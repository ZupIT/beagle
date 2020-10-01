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

import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf

/**
 * action to resolve condition and call onTrue if return true and onFalse if return is false.
 *
 * @param condition condition should represents a boolean.
 * @param onTrue define action if the condition returns true.
 * @param onFalse define action if the condition returns false.
 *
 */
data class Condition(
    val condition: Bind<Boolean>,
    val onTrue: List<Action>? = null,
    val onFalse: List<Action>? = null
): Action {
    constructor(
        condition: Boolean,
        onTrue: List<Action>? = null,
        onFalse: List<Action>? = null
    ) : this(
        condition = valueOf(condition),
        onTrue = onTrue,
        onFalse = onFalse
    )
}