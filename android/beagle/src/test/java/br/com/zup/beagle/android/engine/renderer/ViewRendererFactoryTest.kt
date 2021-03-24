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

package br.com.zup.beagle.android.engine.renderer

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.widget.ViewConvertable
import org.junit.Assert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a ViewRendererFactory")
class ViewRendererFactoryTest : BaseTest() {

    private val component by lazy { Text("") }
    private val viewRendererFactory = ViewRendererFactory()

    @DisplayName("When a component is requested")
    @Nested
    inner class ComponentConversion {

        @DisplayName("Then the component should be rendered correctly")
        @Test
        fun checkComponentCreation() {
            // When
            val actual = viewRendererFactory.make(component)

            // Then
            Assert.assertEquals(component as ViewConvertable, actual.component)
        }
    }
}
