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

package br.com.zup.beagle.android.utils

import java.lang.reflect.Field
import java.lang.reflect.Modifier

private const val MODIFIERS = "modifiers"
internal fun Field.setNotFinalAndAccessible() {
    try {
        this.isAccessible = true

        val modifiersField = this.javaClass.getDeclaredField(MODIFIERS)
        modifiersField.isAccessible = true
        modifiersField.setInt(this, this.modifiers and Modifier.FINAL.inv())
    } catch (e: Exception) {
        //ignored
        e.printStackTrace()
    }
}