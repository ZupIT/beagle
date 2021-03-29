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

/**
 *  The Styled is a helper to apply style in your component
 *
 * @param self the component will apply style
 *
 */
@Suppress("FunctionNaming")
fun <T : StyleComponent> Styled(self: T, block: Style.() -> Unit): T {
    self.setStyle(block)
    return self
}

/**
 *  The StyleBuilder is a helper to set style options in your component.
 *  with this method you don't need instance any object, just set fields
 *
 */
fun <T : StyleComponent> T.setStyle(block: Style.() -> Unit): T {
    this.style = this.style ?: Style()
    this.style?.block()
    return this
}