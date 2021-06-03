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

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.GridView
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.components.utils.Template
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.context.expressionOf
import br.com.zup.beagle.android.context.normalizeContextValue
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.mockdata.TypeAdapterResolverImpl
import br.com.zup.beagle.core.ServerDrivenComponent
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Moshi Adapter")
class GridViewTest : BaseTest() {

    private lateinit var moshi: Moshi

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { beagleSdk.formLocalActionHandler } returns mockk(relaxed = true)
        every { beagleSdk.registeredWidgets() } returns listOf()
        every { beagleSdk.registeredActions() } returns listOf()
        every { beagleSdk.typeAdapterResolver } returns TypeAdapterResolverImpl()

        moshi = BeagleMoshi.createMoshi()
    }

    @DisplayName("When try serialize json grid view")
    @Nested
    inner class SerializeJsonGridViewTest {

        @DisplayName("Then should return correct object")
        @Test
        fun testSerializeJsonGridView() {
            // Given
            val json = makeJsonGridView()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).fromJson(json)

            // Then
            assertNotNull(actual)
            assertEquals(makeObjectGridView(), actual)
        }
    }

    @DisplayName("When try serialize object grid view")
    @Nested
    inner class SerializeObjectGridViewTest {

        @DisplayName("Then should return correct json")
        @Test
        fun testSerializeJsonGridView() {
            // Given
            val gridView = makeObjectGridView()

            // When
            val actual = moshi.adapter(ServerDrivenComponent::class.java).toJson(gridView)

            // Then
            assertNotNull(actual)
            assertEquals(makeJsonGridView().replace("\\s".toRegex(), ""), actual.replace("\\s".toRegex(), ""))
        }
    }


    private fun makeObjectGridView() = GridView(
        numColumns = 2,
        dataSource = expressionOf("@{characters}"),
        templates = listOf(
            Template(
                case = expressionOf("@{eq(item.race,'Half-skaa')}"),
                view = Container(
                    children = listOf(Text("Name:@{item.name}"))
                )
            )
        )
    )

    private fun makeJsonGridView() = """
    {
      "_beagleComponent_": "beagle:gridview",
      "dataSource": "@{characters}",
      "templates": [
        {
          "case": "@{eq(item.race,'Half-skaa')}",
          "view": {
            "_beagleComponent_": "beagle:container",
            "children": [
              {
                "_beagleComponent_": "beagle:text",
                "text": "Name:@{item.name}"
              }
            ]
          }
        }
      ],
      "isScrollIndicatorVisible": false,
      "iteratorName": "item",
      "numColumns": 2
    }
"""
}