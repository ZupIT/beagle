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

internal class LeafContextTest {
    @Test
    fun test_generated_expression() {
        checkExpression(LeafContext_.a, "@{a}", Any::class)
        checkExpression(LeafContext_.b, "@{b}", Boolean::class)
        checkExpression(LeafContext_.c, "@{c}", Byte::class)
        checkExpression(LeafContext_.d, "@{d}", Char::class)
        checkExpression(LeafContext_.e, "@{e}", Int::class)
        checkExpression(LeafContext_.f, "@{f}", Long::class)
        checkExpression(LeafContext_.g, "@{g}", Float::class)
        checkExpression(LeafContext_.h, "@{h}", Double::class)
        checkExpression(LeafContext_.i, "@{i}", String::class)
    }
}