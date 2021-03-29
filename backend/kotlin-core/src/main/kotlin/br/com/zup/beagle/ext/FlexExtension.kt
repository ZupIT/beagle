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

package br.com.zup.beagle.ext

import br.com.zup.beagle.core.Style
import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.widget.core.Flex

/**
 *  The Flexible is a helper to apply Flex in your component
 *
 * @param self the component will apply flex
 *
 */
@Suppress("FunctionNaming")
fun <T : StyleComponent> Flexible(self: T, block: Flex.() -> Unit): T {
    return self.setFlex(block)
}

/**
 *  Helper to set Flex options in your component.
 *  with this method you don't need to instantiate any object, just set fields
 *
 */
fun <T : StyleComponent> T.setFlex(block: Flex.() -> Unit): T {
    this.style = this.style ?: Style()
    this.style?.flex?.block()
    return this
}
