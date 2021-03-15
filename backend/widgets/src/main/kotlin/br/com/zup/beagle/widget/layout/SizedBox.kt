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

package br.com.zup.beagle.widget.layout

import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.ext.Styled
import br.com.zup.beagle.widget.core.UnitValue

fun <T : StyleComponent> SizedBox(width: Int,
                                  height: Int,
                                  child: T): T =
    SizedBox(width = UnitValue.real(width), height = UnitValue.real(height), child)

fun <T : StyleComponent> SizedBox(width: Double,
                                  height: Double,
                                  child: T): T =
    SizedBox(width = UnitValue.real(width), height = UnitValue.real(height), child)

fun <T : StyleComponent> SizedBox(width: UnitValue,
                                  height: UnitValue,
                                  child: T): T {
    return Styled(child, {
        this.size?.width = width
        this.size?.height = height
    })
}