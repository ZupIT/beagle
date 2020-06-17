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

import br.com.zup.beagle.widget.core.Flex

/**
 * Component that hold the flex
 * @property flex
 *                  is a Layout component that will handle your visual component positioning at the screen.
 *                  Internally Beagle uses a Layout engine called Yoga Layout to position elements on screen.
 *                  In fact it will use the HTML Flexbox  properties applied on the visual components and its children.
 */
interface FlexComponent : ServerDrivenComponent {
    val flex: Flex?
}