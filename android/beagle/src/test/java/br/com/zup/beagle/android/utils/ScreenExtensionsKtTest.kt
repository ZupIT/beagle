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

package br.com.zup.beagle.android.utils

import br.com.zup.beagle.analytics.ScreenEvent
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.NavigationBar
import br.com.zup.beagle.android.components.layout.SafeArea
import br.com.zup.beagle.android.components.layout.Screen
import br.com.zup.beagle.android.components.layout.ScreenComponent
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.core.Style
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a ScreenExtension")
internal class ScreenExtensionsKtTest : BaseTest() {

    @DisplayName("When call Screen to ScreenComponent")
    @Nested
    inner class ScreenToScreenComponent {

        private val identifier = "identifier"
        private val safeArea = SafeArea()
        private val navigationBar = NavigationBar("navigationBar")
        private val child by lazy { Text("Test component") }
        private val style = Style()
        private val screenAnalyticsEvent = ScreenEvent("ScreenName")
        private val context = ContextData("id", false)
        private val id = "id"


        @DisplayName("Then should map correctly")
        @Test
        fun testScreenWithIdToScreenComponentMapCorrectly() {
            //given
            val screen = Screen(
                safeArea,
                navigationBar,
                child,
                style,
                screenAnalyticsEvent,
                context,
                id
            )

            //when
            val screenComponent = screen.toComponent()

            //then
            commonAssert(screenComponent)
            assertEquals(id, screenComponent.id)
            assertEquals(null, screenComponent.identifier)
        }

        @DisplayName("Then should map correctly")
        @Test
        fun testScreenWithIdentifierToScreenComponentMapCorrectly() {
            //given
            val screen = Screen(
                identifier,
                safeArea,
                navigationBar,
                child,
                style,
                screenAnalyticsEvent,
                context
            )

            //when
            val screenComponent = screen.toComponent()

            //then
            commonAssert(screenComponent)
            assertEquals(identifier, screenComponent.identifier)
            assertEquals(null, screenComponent.id)
        }

        private fun commonAssert(screenComponent: ScreenComponent) {
            assertEquals(safeArea, screenComponent.safeArea)
            assertEquals(navigationBar, screenComponent.navigationBar)
            assertEquals(child, screenComponent.child)
            assertEquals(style, screenComponent.style)
            assertEquals(screenAnalyticsEvent, screenComponent.screenAnalyticsEvent)
            assertEquals(context, screenComponent.context)
        }
    }
}
