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