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