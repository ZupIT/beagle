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