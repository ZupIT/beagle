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

import br.com.zup.beagle.expression.valueExpr
import br.com.zup.beagle.sample.constants.MODEL_ENDPOINT
import br.com.zup.beagle.sample.constants.MODEL_ENDPOINT2
import br.com.zup.beagle.sample.widget.WidgetBindingSample
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.layout.Container

object SampleBindingComponent : ComposeComponent() {
    override fun build() = this.createComplexWidget()

    internal fun createWidget() = Container(children = listOf(
        WidgetBindingSample(
            intValue = valueExpr(1, "@{b}"),
            stringValue = valueExpr("Loading 1", "@{a}")
        )
    )
    )

    internal fun createComplexWidget() = Container(
        children = listOf(
            Container(
                children = listOf(
                    WidgetBindingSample(
                        intValue = valueExpr(1, "@{b}"),
                        stringValue = valueExpr("Loading 1", "@{a}")
                    ),
                    WidgetBindingSample(
                        intValue = valueExpr(2, "@{b}"),
                        stringValue = valueExpr("Loading 2", "@{a}")
                    ).setModelPath(MODEL_ENDPOINT2)
                )
            )
        )
    ).setModelPath(MODEL_ENDPOINT)
}
