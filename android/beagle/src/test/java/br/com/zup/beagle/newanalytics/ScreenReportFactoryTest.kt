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

package br.com.zup.beagle.newanalytics

import br.com.zup.beagle.android.BaseTest
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Screen Report Factory")
class ScreenReportFactoryTest : BaseTest() {

    @DisplayName("When GenerateScreenAnalyticsRecord")
    @Nested
    inner class GenerateScreenAnalyticsRecord {

        @Test
        @DisplayName("Then should create local screen report correctly")
        fun testCreateScreenReportShouldCreateScreenReportCorrectly() {
            //Given
            val timestamp = System.currentTimeMillis()

            //WHEN
            val result = ScreenReportFactory.generateScreenAnalyticsRecord("screenId", timestamp)

            //THEN
            assertEquals("android", result.platform)
            assertEquals("screen", result.type)
            assertEquals("screenId", result.screen)
            assertEquals(null, result.additionalEntries)
            assertEquals(null, result.attributes)
            assertEquals(null, result.component)
            assertEquals(null, result.beagleAction)
            assertEquals(null, result.event)
            assertEquals(timestamp, result.timestamp)
        }
    }
}
