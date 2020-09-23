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

package br.com.zup.beagle.core

/**
 * The position type of an element defines how it is positioned within its parent.
 *
 * @property ABSOLUTE
 * @property RELATIVE
 */
enum class PositionType {

    /**
     * This means an element is positioned according to the normal flow of the layout,
     * and then offset relative to that position based on the values of top, right, bottom, and left.
     * The offset does not affect the position of any sibling or parent elements.
     */
    ABSOLUTE,

    /**
     * When positioned absolutely an element doesn't take part in the normal layout flow.
     * It is instead laid out independent of its siblings.
     * The position is determined based on the top, right, bottom, and left values.
     */
    RELATIVE
}