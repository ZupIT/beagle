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
import br.com.zup.beagle.widget.core.AlignSelf
import br.com.zup.beagle.widget.core.UnitValue

/**
 *  The Expanded it is a helper to apply grow in your component
 *
 * @param self the component will apply grow
 *
 */
@Suppress("FunctionNaming")
fun <T : StyleComponent> Expanded(self: T): T {
    return Styled(self, {
        size.width = UnitValue.percent(100)
        size.height = UnitValue.percent(100)
        this.flex.alignSelf = AlignSelf.STRETCH
        this.flex.grow = 1.0
    })
}