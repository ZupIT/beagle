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

package br.com.zup.beagle.android.compiler.registrar

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import java.util.UUID

internal open class RegistrarGeneratorBaseTest {
    @TempDir
    protected lateinit var tempPath: Path

    private val mockUUID = mockk<UUID>()

    @BeforeAll
    fun setUp() {
        mockkStatic(UUID::class)
        every { mockUUID.toString() } returns "Test"
        every { UUID.randomUUID() } returns mockUUID
    }

    @AfterAll
    fun tearDown() {
        unmockkAll()
    }

    companion object {
        internal const val FILE_NAME = "File1.kt"
        internal val REGEX_REMOVE_SPACE = "\\s".toRegex()
        internal const val REGISTRAR_SUFFIX = "RegistrarTest"
        internal const val KAPT_OPTION_NAME = "beagle.moduleName"
    }
}