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

data class Flex (
    val flexDirection: FlexDirection? = null, /* = FlexDirection.COLUMN */
    val direction: Direction? = null, /* = Direction.LTR */
    val flexWrap: FlexWrap? = null, /* = FlexWrap.NO_WRAP */
    val justifyContent: JustifyContent? = null, /* = JustifyContent.FLEX_START */
    val alignItems: Alignment? = null, /* = Alignment.STRETCH */
    val alignSelf: Alignment? = null, /* = Alignment.AUTO */
    val alignContent: Alignment? = null, /* = Alignment.FLEX_START */
    val positionType: FlexPositionType? = null, /* = FlexPositionType.RELATIVE */
    val basis: UnitValue? = null, /* = UnitValue(0.0, UnitType.AUTO) */
    val flex: Double? = null, /* = 0.0 */
    val grow: Double? = null, /* = 0.0 */
    val shrink: Double? = null, /* = 1.0 */
    val display: FlexDisplay? = null, /* = FlexDisplay.FLEX */
    val size: Size? = null,
    val margin: EdgeValue? = null,
    val padding: EdgeValue? = null,
    val position: EdgeValue? = null
)

data class Size (
    val width: UnitValue? = null,
    val height: UnitValue? = null,
    val maxWidth: UnitValue? = null,
    val maxHeight: UnitValue? = null,
    val minWidth: UnitValue? = null,
    val minHeight: UnitValue? = null,
    val aspectRatio: Double? = null
)

data class EdgeValue(
    val left: UnitValue? = null,
    val top: UnitValue? = null,
    val right: UnitValue? = null,
    val bottom: UnitValue? = null,
    val start: UnitValue? = null,
    val end: UnitValue? = null,
    val horizontal: UnitValue? = null,
    val vertical: UnitValue? = null,
    val all: UnitValue? = null
)

enum class FlexDirection {
    COLUMN,
    ROW,
    COLUMN_REVERSE,
    ROW_REVERSE
}

enum class Direction {
    INHERIT,
    LTR,
    RTL
}

enum class FlexWrap {
    NO_WRAP,
    WRAP,
    WRAP_REVERSE
}

enum class JustifyContent {
    FLEX_START,
    CENTER,
    FLEX_END,
    SPACE_BETWEEN,
    SPACE_AROUND,
    SPACE_EVENLY
}

enum class Alignment {
    FLEX_START,
    CENTER,
    FLEX_END,
    SPACE_BETWEEN,
    SPACE_AROUND,
    BASELINE,
    AUTO,
    STRETCH
}

enum class FlexDisplay {
    FLEX,
    NONE
}

enum class FlexPositionType {
    ABSOLUTE,
    RELATIVE
}
