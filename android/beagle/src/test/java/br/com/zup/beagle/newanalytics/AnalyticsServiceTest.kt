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

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.AnalyticsAction
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Analytics Service")
class AnalyticsServiceTest : BaseTest() {

    private val analyticsProvider: AnalyticsProvider = mockk()
    private val action: AnalyticsAction = mockk(relaxed = true)
    private val origin: View = mockk()
    private val slot = slot<AnalyticsConfig>()
    private val analyticsConfig: AnalyticsConfig = mockk()

    @BeforeEach
    fun setup() {
        mockkConstructor(DataScreenReport::class)
        mockkConstructor(DataActionReport::class)
        every { rootView.activity } returns mockk()
        mockkObject(BeagleMessageLogs)
        every { beagleSdk.analyticsProvider } returns analyticsProvider
        every { analyticsProvider.createRecord(any()) } just Runs
    }

    @DisplayName("When create screen record")
    @Nested
    inner class ReportScreenWithAnalyticsConfigNotNull {

        @BeforeEach
        fun config() {
            every { analyticsProvider.getConfig() } returns analyticsConfig
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateScreenRecordShouldCallDataScreenReportReportReturningNotNullReportAndThenShouldCreateTheRecord() {
            //Given
            val analyticsRecord: AnalyticsRecord = mockk()
            every { anyConstructed<DataScreenReport>().report(capture(slot)) } returns analyticsRecord

            //When
            AnalyticsService.createScreenRecord( "url")

            //Then
            verify(exactly = 1) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateScreenRecordShouldCallDataScreenReportReportReturningNNullReportAndThenShouldNotCreateTheRecord() {
            //Given
            val analyticsConfig: AnalyticsConfig = mockk()
            val slot = slot<AnalyticsConfig>()
            every { analyticsProvider.getConfig() } returns analyticsConfig
            every { beagleSdk.analyticsProvider } returns analyticsProvider
            every { anyConstructed<DataScreenReport>().report(capture(slot)) } returns null
            //When
            AnalyticsService.createScreenRecord( "url")


            //then
            verify(exactly = 0) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }
    }
    
    @DisplayName("When CreateActionRecord")
    @Nested
    inner class CreateActionRecordWithAnalyticsConfigDisabled {

        @BeforeEach
        fun setUp() {
            every { analyticsProvider.getConfig() } returns mockk()
        }


        @Test
        @DisplayName("Then should do nothing")
        fun testCreateActionRecordWithAnalyticsConfigDisabledShouldDoNothing() {
            //Given
            every { action.analytics } returns ActionAnalyticsConfig.Disabled()

            //When
            AnalyticsService.createActionRecord(rootView, origin, action)

            //Then
            verify(exactly = 0) { analyticsProvider.createRecord(any()) }
        }
    }

    @DisplayName("When CreateActionRecord")
    @Nested
    inner class CreateActionRecordWithAnalyticsConfigEnabled {
        private val dataActionReport: DataActionReport = mockk()

        @BeforeEach
        fun setUp() {
            mockkObject(ActionReportFactory)
            every { ActionReportFactory.generateDataActionReport(any(), any(), any(), any()) } returns dataActionReport
            every { analyticsProvider.getConfig() } returns analyticsConfig
        }


        @Test
        @DisplayName("Then should call the right funs")
        fun testCreateActionRecordWithAnalyticsConfigEnabledReportReturningNotNullReportAndThenShouldCreateTheRecord() {
            //Given
            every { action.analytics } returns ActionAnalyticsConfig.Enabled()
            val analyticsRecord: AnalyticsRecord = mockk()
            every { dataActionReport.report(capture(slot)) } returns analyticsRecord
            //When
            AnalyticsService.createActionRecord(rootView, origin, action)

            //Then
            verify(exactly = 1) { ActionReportFactory.generateDataActionReport(any(), any(), any(), any()) }
            verify(exactly = 1) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }

        @Test
        @DisplayName("Then should call the right funs")
        fun testCreateActionRecordWithAnalyticsConfigEnabledReportReturningNullReportAndThenShouldNotCreateTheRecord() {
            //Given
            every { action.analytics } returns ActionAnalyticsConfig.Enabled()
            every { dataActionReport.report(capture(slot)) } returns null
            //When
            AnalyticsService.createActionRecord(rootView, origin, action)

            //Then
            verify(exactly = 1) { ActionReportFactory.generateDataActionReport(any(), any(), any(), any()) }
            verify(exactly = 0) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }
    }
}
