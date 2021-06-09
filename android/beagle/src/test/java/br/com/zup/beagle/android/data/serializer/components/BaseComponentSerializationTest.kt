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
import br.com.zup.beagle.android.data.serializer.BeagleMoshi
import br.com.zup.beagle.android.mockdata.TypeAdapterResolverImpl
import com.squareup.moshi.Moshi
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach

abstract class BaseComponentSerializationTest : BaseTest() {

    protected lateinit var moshi: Moshi

    @BeforeEach
    override fun setUp() {
        super.setUp()

        every { beagleSdk.formLocalActionHandler } returns mockk(relaxed = true)
        every { beagleSdk.registeredWidgets() } returns listOf()
        every { beagleSdk.registeredActions() } returns listOf()
        every { beagleSdk.typeAdapterResolver } returns TypeAdapterResolverImpl()

        moshi = BeagleMoshi.createMoshi()
    }
}