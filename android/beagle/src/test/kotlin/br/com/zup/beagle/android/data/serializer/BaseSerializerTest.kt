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

package br.com.zup.beagle.android.data.serializer

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.mockdata.TypeAdapterResolverImpl
import br.com.zup.beagle.android.testutil.withoutWhiteSpaces
import br.com.zup.beagle.core.ServerDrivenComponent
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class BaseSerializerTest<T>(private val clazz: Class<T>) : BaseTest() {

    lateinit var moshi: Moshi

    @BeforeAll
    override fun setUp() {
        super.setUp()

        every { beagleSdk.formLocalActionHandler } returns mockk(relaxed = true)
        every { beagleSdk.registeredWidgets() } returns listOf()
        every { beagleSdk.registeredActions() } returns listOf()
        every { beagleSdk.typeAdapterResolver } returns TypeAdapterResolverImpl()

        moshi = BeagleMoshi.createMoshi()
    }

    @AfterAll // Needed. We are overriding the annotation of the super class.
    override fun tearDown() {
        super.tearDown()
    }

    fun serialize(anObject: T): String = moshi.adapter(clazz).toJson(anObject)
    fun deserialize(json: String) = moshi.adapter(clazz).fromJson(json)

    fun testDeserializeJson(json: String, expectedComponent: T) {
        // When
        val deserializedComponent = deserialize(json)

        // Then
        Assertions.assertNotNull(deserializedComponent)
        Assertions.assertEquals(expectedComponent, deserializedComponent)
    }

    fun testSerializeObject(expectedJson: String, objectComponent: T) {
        // When
        val serializedJson = serialize(objectComponent)

        // Then
        Assertions.assertNotNull(serializedJson)
        Assertions.assertEquals(expectedJson.withoutWhiteSpaces(), serializedJson.withoutWhiteSpaces())
    }
}