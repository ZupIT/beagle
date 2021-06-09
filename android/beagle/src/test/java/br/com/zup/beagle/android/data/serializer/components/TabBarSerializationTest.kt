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

package br.com.zup.beagle.android.data.serializer.components

import br.com.zup.beagle.android.action.SetContext
import br.com.zup.beagle.android.components.ImagePath
import br.com.zup.beagle.android.components.TabBar
import br.com.zup.beagle.android.components.TabBarItem
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.core.ServerDrivenComponent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class TabBarSerializationTest : BaseComponentSerializationTest() {

    @DisplayName("When try to deserialize json TabBar")
    @Nested
    inner class DeserializeJsonTabBarTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testDeserializeJsonTabBar() {
            // Given
            val expectedComponent = makeObjectTabBar()
            val json = makeTabBarJson()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedComponent, actual)
        }
    }

    @DisplayName("When try serialize object TabBar")
    @Nested
    inner class SerializeObjectTabBarTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonTabBar() {
            // Given
            val expectedJson = makeTabBarJson().replace("\\s".toRegex(), "")
            val component = makeObjectTabBar()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(component)
            val actualJson = actual.replace("\\s".toRegex(), "")

            // Then
            Assertions.assertNotNull(actual)
            Assertions.assertEquals(expectedJson, actualJson)
        }
    }

    private fun makeTabBarJson() = """
    {
       "_beagleComponent_":"beagle:tabbar",
       "items":[
          ${makeTabBarItemJson()},${makeTabBarItemJson()},${makeTabBarItemJson()}
       ],
       "currentTab":"@{contextTab}",
       "onTabSelection":[
          {
             "_beagleAction_":"beagle:setcontext",
             "contextId":"contextTab",
             "value":"@{onTabSelection}"
          }
       ]
    }
"""

    private fun makeTabBarItemJson() = """
    {
       "title":"Tab 1",
       "icon":{
          "mobileId":"beagle"
       }
    }
"""

    private fun makeObjectTabBar() = TabBar(
        onTabSelection = listOf(
            SetContext(
                contextId = "contextTab",
                value = "@{onTabSelection}",
            )
        ),
        currentTab = expressionOf("@{contextTab}"),
        items = listOf(
            makeObjectTabBarItem(),
            makeObjectTabBarItem(),
            makeObjectTabBarItem(),
        )
    )

    private fun makeObjectTabBarItem() = TabBarItem(
        title = "Tab 1",
        icon = ImagePath.Local(
            mobileId = "beagle"
        )
    )
}