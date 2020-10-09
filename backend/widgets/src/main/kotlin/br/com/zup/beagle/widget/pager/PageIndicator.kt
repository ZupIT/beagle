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

package br.com.zup.beagle.widget.pager

import br.com.zup.beagle.widget.context.Bind

/**
 *  The PageView component is a specialized container to hold pages (views) that will be displayed horizontally.
 *
 * @param selectedColor this is a string value and it must be filled as HEX (Hexadecimal)
 * @param unselectedColor this is a string value and it must be filled as HEX (Hexadecimal)
 * @param numberOfPages Numbers of pages that will contain.
 * @param currentPage Integer number that identifies that selected
 */
data class PageIndicator(
    val selectedColor: String,
    val unselectedColor: String,
    var numberOfPages: Int? = null,
    var currentPage: Bind<Int>? = null
) : PageIndicatorComponent