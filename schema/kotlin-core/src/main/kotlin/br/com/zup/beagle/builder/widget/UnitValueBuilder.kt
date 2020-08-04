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

package br.com.zup.beagle.builder.widget

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue
import kotlin.properties.Delegates

fun unitValue(block: UnitValueBuilder.() -> Unit) = UnitValueBuilder().apply(block).build()

class UnitValueBuilder: BeagleBuilder<UnitValue> {
    var value: Double by Delegates.notNull()
    var type: UnitType by Delegates.notNull()

    fun value(value: Double) = this.apply { this.value = value }
    fun type(type: UnitType) = this.apply { this.type = type }

    fun real(value: Double) = this.apply {
        this.value = value
        this.type = UnitType.REAL
    }

    fun percent(value: Double) = this.apply {
        this.value = value
        this.type = UnitType.PERCENT
    }

    fun value(block: () -> Double){
        value(block.invoke())
    }

    fun type(block: () -> UnitType){
        type(block.invoke())
    }

    fun real(block: () -> Double){
        real(block.invoke())
    }

    fun percent(block: () -> Double){
        percent(block.invoke())
    }

    override fun build() = UnitValue(value, type)
}