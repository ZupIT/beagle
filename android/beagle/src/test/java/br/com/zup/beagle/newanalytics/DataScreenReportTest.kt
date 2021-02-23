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
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given DataScreenReport")
internal class DataScreenReportTest : BaseTest() {

    private val SCREEN_IDENTIFIER = "screenIdentifier"

    @DisplayName("When report")
    @Nested
    inner class ReportWithEnableScreenAnalyticsTrue {
        private val analyticsConfig: AnalyticsConfig = mockk()

        @BeforeEach
        fun setup() {
            every { analyticsConfig.enableScreenAnalytics } returns true
        }

        @Test
        @DisplayName("Then should call ScreenReportFactory.generateScreenAnalyticsRecord")
        fun testReportScreenWithScreenReportEnabledShouldCallGenerateScreenAnalyticsRecord() {
            //GIVEN
            mockkObject(ScreenReportFactory)
            val dataScreenReport = DataScreenReport(screenIdentifier = SCREEN_IDENTIFIER)
            val timestamp = dataScreenReport.timestamp

            //WHEN
            dataScreenReport.report(analyticsConfig)

            //THEN
            verify(exactly = 1) { ScreenReportFactory.generateScreenAnalyticsRecord(SCREEN_IDENTIFIER, timestamp) }

        }
    }

    @DisplayName("When report")
    @Nested
    inner class ReportWithEnableScreenAnalyticsFalse {
        private val analyticsConfig: AnalyticsConfig = mockk()

        @BeforeEach
        fun setup() {
            every { analyticsConfig.enableScreenAnalytics } returns false
        }

        @Test
        @DisplayName("Then should not call ScreenReportFactory.generateScreenAnalyticsRecord and return null")
        fun testReportScreenWithScreenReportDisabledShouldNotCallGenerateScreenAnalyticsRecordAndReturnNull() {
            //GIVEN
            mockkObject(ScreenReportFactory)
            val dataScreenReport = DataScreenReport(screenIdentifier = SCREEN_IDENTIFIER)

            //WHEN
            val result = dataScreenReport.report(analyticsConfig)

            //THEN
            verify(exactly = 0) { ScreenReportFactory.generateScreenAnalyticsRecord(any(), any()) }
            assertEquals(null, result)

        }
    }
}
