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

package br.com.zup.beagle.android.mockdata

import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.core.ServerDrivenComponent

data class InternalObject(val value1: String, val value2: Int)

data class ComponentBinding(
    val value1: Bind<Int>?,
    val value2: Bind<String>,
    val value3: Bind<Boolean>,
    val value4: Bind<InternalObject>
) : ServerDrivenComponent