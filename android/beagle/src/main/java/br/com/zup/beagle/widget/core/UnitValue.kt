// THIS IS A GENERATED FILE. DO NOT EDIT THIS
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

package br.com.zup.beagle.widget.core

import br.com.zup.beagle.core.BeagleJson

/**
 * Represents measurement values that contain both the numeric magnitude and the unit of measurement.
 *
 * @param value the numeric measurement value.
 * @param type the unit of measurement.
 *
 */
@BeagleJson
data class UnitValue(
    val value: Double,
    val type: UnitType,
)
/**
 * This defines of a unit type;
 *
 * @property REAL
 * @property PERCENT
 * @property AUTO
 *
 */

@BeagleJson
enum class UnitType {
    /**
     * Apply the value based in platform, like android this represent dp.
     */
    REAL,

    /**
     * Apply the value based in percentage.
     */
    PERCENT,

    /**
     * TODO.
     */
    AUTO
}
