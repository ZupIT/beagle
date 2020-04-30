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

package br.com.zup.beagle.sample.compose

import br.com.zup.beagle.expression.expr
import br.com.zup.beagle.sample.constants.NESTED_MODEL_ENDPOINT
import br.com.zup.beagle.sample.model.SampleNestedModel_
import br.com.zup.beagle.sample.widget.NestedBindingWidget
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.core.Flex

object SampleNestedBindingComponent : ComposeComponent() {
    override fun build() = NestedBindingWidget(
        abc = expr(SampleNestedModel_.a.b.c),
        abd = expr(SampleNestedModel_.a.b.d),
        abe0f = expr(SampleNestedModel_.a.b.e[0].f),
        abg0 = expr(SampleNestedModel_.a.b.g[0]),
        abg1 = expr(SampleNestedModel_.a.b.g[1]),
        abg2 = expr(SampleNestedModel_.a.b.g[2])
    ).setModelPath(NESTED_MODEL_ENDPOINT).applyFlex(Flex(grow = 1.0))
}