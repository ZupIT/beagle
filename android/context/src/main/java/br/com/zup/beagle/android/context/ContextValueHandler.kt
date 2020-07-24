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

import java.lang.reflect.Type

//this object is necessary because MOSHI return every number as double
// to fix this, this function made due conversion
internal object ContextValueHandler {

    fun treatValue(value: Any, type: Type): Any {
        var treatedValue = value
        if (value is Double) {
            if (typeIsInt(type))
                treatedValue = value.toInt()
            else if (typeIsFloat(type))
                treatedValue = value.toFloat()
        }
        return treatedValue
    }

    private fun typeIsInt(type: Type) = type == Integer::class.java

    private fun typeIsFloat(type: Type) = type == Float::class.java
}