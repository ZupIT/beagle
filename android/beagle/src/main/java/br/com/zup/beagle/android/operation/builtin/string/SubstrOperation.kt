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

package br.com.zup.beagle.android.operation.builtin.string

import br.com.zup.beagle.android.operation.Operation
import br.com.zup.beagle.android.operation.OperationType
import br.com.zup.beagle.annotation.RegisterOperation

@RegisterOperation("substr")
internal class SubstrOperation : Operation {

    override fun execute(vararg params: OperationType?): OperationType {
        val text = (params[0] as? OperationType.TypeString)?.value ?: ""
        val start = (params[1] as? OperationType.TypeNumber)?.value as Int? ?: 0
        val lengthParameter = (params.getOrNull(2) as? OperationType.TypeNumber)?.value as Int?
        val length = lengthParameter ?: text.length - start
        val end = start + (length - 1)

        if (length == 0) {
            return OperationType.TypeString("")
        }

        return OperationType.TypeString(text.substring(start..end))
    }

}