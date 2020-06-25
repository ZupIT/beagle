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

package br.com.zup.beagle.expression

import org.junit.jupiter.api.Test

internal class CyclicContextTest {
    @Test
    fun test_generated_expressions() {
        checkExpression(CyclicContext_.done, "@{done}", Boolean::class)
        checkExpression(CyclicContext_.cycle.cyclicContext.done, "@{cycle.cyclicContext.done}", Boolean::class)
        checkExpression(
            CyclicContext_.cycle.cyclicContext.cycle.cyclicContext.done,
            "@{cycle.cyclicContext.cycle.cyclicContext.done}",
            Boolean::class
        )
    }
}