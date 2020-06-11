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
 * The style class will enable a few visual options to be changed.
 *
 * @param backgroundColor
 *                          Using a String parameter it sets the background color on this visual component.
 *                          It must be listed as an Hexadecimal color format without the "#".
 *                          For example, for a WHITE background type in "FFFFFF".
 * @param cornerRadius Using a Double parameters it sets the corner of your view to make it round.
 */
data class Style(val backgroundColor: String? = null,
                 val cornerRadius: CornerRadius? = null)

/**
 * The corner radius change the appearance of view
 *
 * @param radius define size of radius
 */
data class CornerRadius(
    val radius: Double = 0.0
)