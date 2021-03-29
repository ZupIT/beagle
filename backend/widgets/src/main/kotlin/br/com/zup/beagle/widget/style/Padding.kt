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

package br.com.zup.beagle.widget.style

import br.com.zup.beagle.core.StyleComponent
import br.com.zup.beagle.ext.Styled
import br.com.zup.beagle.widget.core.EdgeValue

/**
 *  The Padding is a helper to apply padding in your component
 *
 * @param self the component will apply padding
 *
 */
@Suppress("FunctionNaming")
fun <T : StyleComponent> Padding(padding: EdgeValue, self: T): T {
    return Styled(self, {
        this.padding = padding
    })
}
