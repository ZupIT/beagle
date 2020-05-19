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

package br.com.zup.beagle.ext

import br.com.zup.beagle.widget.core.UnitType
import br.com.zup.beagle.widget.core.UnitValue


/**
 * UnitValue extensions
 *
 */

/**
 * convert the int to value based in platform, like android this represent dp.
 * @return the unit value for real
 */
fun Int.unitReal() = UnitValue(this.toDouble(), UnitType.REAL)

/**
 * convert the value based in percentage.
 * @return the unit value for percent
 */
fun Int.unitPercent() = UnitValue(this.toDouble(), UnitType.PERCENT)

/**
 * convert the int to value based in platform, like android this represent dp.
 * @return the unit value for real
 */
fun Double.unitReal() = UnitValue(this, UnitType.REAL)

/**
 * convert the value based in percentage.
 * @return the unit value for percent
 */
fun Double.unitPercent() = UnitValue(this, UnitType.PERCENT)