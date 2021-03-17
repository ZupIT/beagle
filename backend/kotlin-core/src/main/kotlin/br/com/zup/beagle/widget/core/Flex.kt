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

/**
 *
 * The flex is a Layout component that will handle your visual component positioning at the screen.
 * Internally Beagle uses a Layout engine called Yoga Layout to position elements on screen.
 * In fact it will use the HTML Flexbox properties applied on the visual components and its children.
 *
 * @param flexDirection
 *                          controls the direction in which the children of a node are laid out.
 *                          This is also referred to as the main axis
 * @param flexWrap
 *                  set on containers and controls what happens when children
 *                  overflow the size of the container along the main axis.
 * @param justifyContent align children within the main axis of their container.
 * @param alignItems Align items describes how to align children along the cross axis of their container.
 * @param alignSelf
 *                      Align self has the same options and effect as align items
 *                      but instead of affecting the children within a container.
 * @param alignContent Align content defines the distribution of lines along the cross-axis..
 * @param basis is an axis-independent way of providing the default size of an item along the main axis.
 * @param flex TODO.
 * @param grow describes how any space within a container should be distributed among its children along the main axis.
 * @param shrink
 *              describes how to shrink children along the main axis in the case that
 *              the total size of the children overflow the size of the container on the main axis.
 *
 */
data class Flex(
    var flexDirection: FlexDirection? = null, /* = FlexDirection.COLUMN */
    var flexWrap: FlexWrap? = null, /* = FlexWrap.NO_WRAP */
    var justifyContent: JustifyContent? = null, /* = JustifyContent.FLEX_START */
    var alignItems: AlignItems? = null, /* = Alignment.STRETCH */
    var alignSelf: AlignSelf? = null, /* = Alignment.AUTO */
    var alignContent: AlignContent? = null, /* = Alignment.FLEX_START */
    var basis: UnitValue? = null, /* = UnitValue(0.0, UnitType.AUTO) */
    var flex: Double? = null, /* = 0.0 */
    var grow: Double? = null, /* = 0.0 */
    var shrink: Double? = null /* = 1.0 */
)

/**
 *
 * Size handles the size of the item
 *
 * @param width The value specifies the view's width
 * @param height The value specifies the view's height
 * @param maxWidth The value specifies the view's max width
 * @param maxHeight The value specifies the view's max height.
 * @param minWidth The value specifies the view's min width.
 * @param minHeight The value specifies the view's min height.
 * @param aspectRatio defined as the ratio between the width and the height of a node.
 *
 */
data class Size(
    var width: UnitValue? = null,
    var height: UnitValue? = null,
    var maxWidth: UnitValue? = null,
    var maxHeight: UnitValue? = null,
    var minWidth: UnitValue? = null,
    var minHeight: UnitValue? = null,
    var aspectRatio: Double? = null
) {

    companion object {

        fun box(
            width: Int,
            height: Int
        ): Size =
            box(width = UnitValue.real(width),
                height = UnitValue.real(height))

        fun box(width: Double,
                height: Double): Size =
            box(width = UnitValue.real(width),
                height = UnitValue.real(height))

        fun box(width: UnitValue,
                height: UnitValue) =
            Size(width = width,
                height = height)
    }
}

/**
 *
 * specify the offset the edge of the item should have from it’s closest sibling (item) or parent (container)
 *
 * @param left
 *              specify the offset the left edge of the item should have from
 *              it’s closest sibling (item) or parent (container).
 * @param top
 *              specify the offset the top edge of the item should have from
 *              it’s closest sibling (item) or parent (container).
 * @param right
 *              specify the offset the right edge of the item should have from
 *              it’s closest sibling (item) or parent (container).
 * @param bottom
 *               specify the offset the bottom edge of the item should have from
 *               it’s closest sibling (item) or parent (container).
 * @param horizontal
 *               specify the offset the horizontal edge of the item should have from
 *               it’s closest sibling (item) or parent (container).
 * @param vertical
 *               specify the offset the vertical edge of the item should have from
 *               it’s closest sibling (item) or parent (container).
 * @param all
 *               specify the offset the all edge of the item should have from
 *               it’s closest sibling (item) or parent (container).
 */
data class EdgeValue(
    val left: UnitValue? = null,
    val top: UnitValue? = null,
    val right: UnitValue? = null,
    val bottom: UnitValue? = null,
    val horizontal: UnitValue? = null,
    val vertical: UnitValue? = null,
    val all: UnitValue? = null
) {

    companion object {

        private fun createUnitValueIfValueNotNull(value: Double?, unitType: UnitType): UnitValue? {
            if (value == null) return null

            return UnitValue(value, unitType)
        }

        /**
         *
         * Creates edge value with only field set non-null
         *
         */
        @Suppress("LongParameterList")
        fun only(
            left: Int? = null,
            unitTypeLeft: UnitType = UnitType.REAL,
            top: Int? = null,
            unitTypeTop: UnitType = UnitType.REAL,
            right: Int? = null,
            unitTypeRight: UnitType = UnitType.REAL,
            bottom: Int? = null,
            unitTypeBottom: UnitType = UnitType.REAL,
        ): EdgeValue {
            return EdgeValue(
                left = createUnitValueIfValueNotNull(left?.toDouble(), unitTypeLeft),
                top = createUnitValueIfValueNotNull(top?.toDouble(), unitTypeTop),
                right = createUnitValueIfValueNotNull(right?.toDouble(), unitTypeRight),
                bottom = createUnitValueIfValueNotNull(bottom?.toDouble(), unitTypeBottom)
            )
        }

        /**
         *
         * Creates edge value with only field set non-null
         *
         */
        @Suppress("LongParameterList")
        fun only(
            left: Double? = null,
            unitTypeLeft: UnitType = UnitType.REAL,
            top: Double? = null,
            unitTypeTop: UnitType = UnitType.REAL,
            right: Double? = null,
            unitTypeRight: UnitType = UnitType.REAL,
            bottom: Double? = null,
            unitTypeBottom: UnitType = UnitType.REAL,
        ): EdgeValue {
            return EdgeValue(
                left = createUnitValueIfValueNotNull(left, unitTypeLeft),
                top = createUnitValueIfValueNotNull(top, unitTypeTop),
                right = createUnitValueIfValueNotNull(right, unitTypeRight),
                bottom = createUnitValueIfValueNotNull(bottom, unitTypeBottom)
            )
        }

        /**
         *
         * Creates edge value where field all will be set
         *
         */
        fun all(all: Int,
                unitType: UnitType = UnitType.REAL): EdgeValue =
            EdgeValue(all = UnitValue(all.toDouble(), unitType))

        /**
         *
         * Creates edge value where field all will be set
         *
         */
        fun all(all: Double,
                unitType: UnitType = UnitType.REAL): EdgeValue =
            EdgeValue(all = UnitValue(all, unitType))

        /**
         *
         * Creates edge value where field horizontal will be set
         *
         */
        fun horizontal(horizontal: Int,
                       unitType: UnitType = UnitType.REAL): EdgeValue =
            EdgeValue(horizontal = UnitValue(horizontal.toDouble(), unitType))

        /**
         *
         * Creates edge value where field horizontal will be set
         *
         */
        fun horizontal(horizontal: Double,
                       unitType: UnitType = UnitType.REAL): EdgeValue =
            EdgeValue(horizontal = UnitValue(horizontal, unitType))

        /**
         *
         * Creates edge value where field vertical will be set
         *
         */
        fun vertical(vertical: Int,
                     unitType: UnitType = UnitType.REAL): EdgeValue =
            EdgeValue(vertical = UnitValue(vertical.toDouble(), unitType))

        /**
         *
         * Creates edge value where field vertical will be set
         *
         */
        fun vertical(vertical: Double,
                     unitType: UnitType = UnitType.REAL): EdgeValue =
            EdgeValue(vertical = UnitValue(vertical, unitType))
    }
}

/**
 *  controls the direction in which the children of a node are laid out. This is also referred to as the main axis.
 *  The cross axis is the axis perpendicular to the main axis, or the axis which the wrapping lines are laid out in.
 *
 * @property COLUMN
 * @property ROW
 * @property COLUMN_REVERSE
 * @property ROW_REVERSE
 */
enum class FlexDirection {
    /**
     * Align children from top to bottom. If wrapping is enabled,
     * then the next line will start to the right of the first item on the top of the container
     */
    COLUMN,

    /**
     *  Align children from left to right. If wrapping is enabled,
     *  then the next line will start under the first item on the left of the container.
     */
    ROW,

    /**
     *  Align children from bottom to top. If wrapping is enabled,
     *  then the next line will start to the right of the first item on the bottom of the container.
     */
    COLUMN_REVERSE,

    /**
     *  Align children from right to left. If wrapping is enabled,
     *  then the next line will start under the first item on the right of the container.
     */
    ROW_REVERSE
}

/**
 * FlexWrap is set on containers and it controls what happens when children overflow
 * the size of the container along the main axis.
 * By default, children are forced into a single line (which can shrink elements).
 * If wrapping is allowed, items are wrapped into multiple lines along the main axis if needed.
 *
 * @property NO_WRAP
 * @property WRAP
 * @property WRAP_REVERSE
 */
enum class FlexWrap {
    /**
     *  The flex items are laid out in a single line which may cause the flex container to overflow.
     *  The cross-start is either equivalent to start or before depending flex-direction value.
     *  This is the default value.
     */
    NO_WRAP,

    /**
     *  The flex items break into multiple lines.
     *  The cross-start is either equivalent to start or
     *  before depending flex-direction value and the cross-end is the opposite of the specified cross-start.
     */
    WRAP,

    /**
     *  Behaves the same as wrap but cross-start and cross-end are permuted.
     */
    WRAP_REVERSE
}

/**
 * Describes how to align children within the main axis of their container.
 * For example, you can use this property to center a child horizontally within a container with flexDirection
 * set to row or vertically within a container with flexDirection set to column.
 *
 * @property FLEX_START
 * @property CENTER
 * @property FLEX_END
 * @property SPACE_BETWEEN
 * @property SPACE_AROUND
 * @property SPACE_EVENLY
 *
 *
 */
enum class JustifyContent {
    /**
     * Align children of a container to the start of the container's main axis.
     */
    FLEX_START,

    /**
     * Align children of a container in the center of the container's main axis.
     */
    CENTER,

    /**
     * Align children of a container to the end of the container's main axis.
     */
    FLEX_END,

    /**
     * Evenly space off children across the container's main axis,
     * distributing the remaining space between the children.
     */
    SPACE_BETWEEN,

    /**
     * Evenly space off children across the container's main axis,
     * distributing the remaining space around the children.
     * Compared to space-between, using space-around will result in space
     * being distributed to the beginning of the first child and end of the last child.
     */
    SPACE_AROUND,

    /**
     *  evenly distribute children within the alignment container along the main axis.
     *  The spacing between each pair of adjacent items,
     *  the main-start edge and the first item, and the main-end edge and the last item, are all exactly the same.
     */
    SPACE_EVENLY
}

/**
 * Describes how to align distribution of lines along the transverse axis of the container.
 * For example, you can use this property to center child lines horizontally
 * inside a container with flexDirection defined as a column or vertically inside a container
 * with flexDirection defined as a row.
 *
 * @property FLEX_START
 * @property CENTER
 * @property FLEX_END
 * @property SPACE_BETWEEN
 * @property SPACE_AROUND
 * @property STRETCH
 */
enum class AlignContent {
    /**
     * Align wrapped lines to the start of the container's cross axis.
     */
    FLEX_START,

    /**
     * Align wrapped lines in the center of the container's cross axis.
     */
    CENTER,

    /**
     * Align wrapped lines to the end of the container's cross axis.
     */
    FLEX_END,

    /**
     * Evenly space wrapped lines across the container's main axis,
     * distributing the remaining space between the lines.
     */
    SPACE_BETWEEN,

    /**
     * Evenly space wrapped lines across the container's main axis, distributing the remaining space around the lines.
     * Compared to space-between, using space-around will result in space being
     * distributed to the beginning of the first line and the end of the last line.
     */
    SPACE_AROUND,

    /**
     * Stretch wrapped lines to match the height of the container's cross axis.
     */
    STRETCH
}

/**
 *Describes how to align the children on the container's cross axis.
 * Align self replaces any parent-defined options with align items.
 *For example, you can use this property to center a child horizontally
 *inside a container with flexDirection set to column or vertically inside a container with flexDirection set to row.
 *
 * @property FLEX_START
 * @property CENTER
 * @property FLEX_END
 * @property BASELINE
 * @property AUTO
 * @property STRETCH
 */
enum class AlignSelf {
    /**
     * Align wrapped lines to the start of the container's cross axis.
     */
    FLEX_START,

    /**
     * Align wrapped lines in the center of the container's cross axis.
     */
    CENTER,

    /**
     * Align wrapped lines to the end of the container's cross axis.
     */
    FLEX_END,

    /**
     * Align children of a container along a common baseline.
     * Individual children can be set to be the reference baseline for their parents.
     */
    BASELINE,

    /**
     * Computes to the parent's
     */
    AUTO,

    /**
     * Stretch wrapped lines to match the height of the container's cross axis.
     */
    STRETCH
}

/**
 * Describes how to align the children on the cross axis of the container.
 * For example, you can use this property to center a child horizontally
 * inside a container with flexDirection set to column or vertically inside a container with flexDirection set to row.
 *
 * @property FLEX_START
 * @property CENTER
 * @property FLEX_END
 * @property BASELINE
 * @property STRETCH
 */
enum class AlignItems {
    /**
     * Align wrapped lines to the start of the container's cross axis.
     */
    FLEX_START,

    /**
     * Align wrapped lines in the center of the container's cross axis.
     */
    CENTER,

    /**
     * Align wrapped lines to the end of the container's cross axis.
     */
    FLEX_END,

    /**
     * Align children of a container along a common baseline.
     * Individual children can be set to be the reference baseline for their parents.
     */
    BASELINE,

    /**
     * Stretch wrapped lines to match the height of the container's cross axis.
     */
    STRETCH
}

