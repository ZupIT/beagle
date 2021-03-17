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

package br.com.zup.beagle.widget.layout

import br.com.zup.beagle.ext.Styled
import br.com.zup.beagle.widget.core.FlexDirection
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@DisplayName("Given a Row")
internal class RowTest {

    @DisplayName("When call function")
    @Nested
    inner class RowTest {

        @Test
        @DisplayName("Then should return a container with correct direction")
        fun testRow() {
            // When
            val result = Row()

            // Then
            val expected = Styled(Container(), {
                flex.flexDirection = FlexDirection.ROW
            })

            assertEquals(expected, result)
        }
    }

    @DisplayName("When call function with reverse enabled")
    @Nested
    inner class RowReverseTest {

        @Test
        @DisplayName("Then should return a container with correct direction")
        fun testRow() {
            // When
            val result = Row(reverse = true)

            // Then
            val expected = Styled(Container(), {
                flex.flexDirection = FlexDirection.ROW_REVERSE
            })

            assertEquals(expected, result)
        }
    }
}