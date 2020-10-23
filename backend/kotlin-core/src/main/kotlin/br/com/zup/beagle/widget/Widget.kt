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

package br.com.zup.beagle.widget

import br.com.zup.beagle.core.Accessibility
import br.com.zup.beagle.core.AccessibilityComponent
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent

/**
 * Base of all widgets
 *
 */
abstract class Widget : StyleComponent, AccessibilityComponent,
    IdentifierComponent {

    override var id: String? = null
    override var style: Style? = null
    override var accessibility: Accessibility? = null
}
