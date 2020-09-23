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
 * This defines a flex container;
 * inline or block depending on the given value. It enables a flex context for all its direct children.
 *
 * @property FLEX
 * @property NONE
 */
enum class Display {
    /**
     * Apply the flex properties.
     */
    FLEX,

    /**
     * No flex properties will be applied to the element.
     */
    NONE
}