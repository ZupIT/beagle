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

import br.com.zup.beagle.android.data.serializer.adapter.generic.BeagleTypeAdapter
import br.com.zup.beagle.android.data.serializer.adapter.generic.TypeAdapterResolver
import java.lang.reflect.Type


@Suppress("UNCHECKED_CAST")
final class TypeAdapterResolverImpl : TypeAdapterResolver {
    override fun <T> getAdapter(type: Type): BeagleTypeAdapter<T>? = when(type) {
        PersonInterface::class.java ->
            PersonAdapter() as BeagleTypeAdapter<T>

        else -> null
    }

}
