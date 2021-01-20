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

package br.com.zup.beagle.analytics2

import android.view.View
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentLinkedQueue

@DisplayName("Given Analytics Service")
class AnalyticsServiceTest : BaseTest() {

    private val analyticsProvider: AnalyticsProvider = mockk()
    private val action: ActionAnalytics = mockk(relaxed = true)
    private val origin: View = mockk()
    private val slot = slot<AnalyticsConfig>()
    private val analyticsConfig: AnalyticsConfig = mockk()

    @BeforeEach
    fun setup() {
        mockkConstructor(DataScreenReport::class)
        mockkConstructor(DataActionReport::class)
        every { rootView.activity } returns mockk()
        mockkObject(BeagleMessageLogs)
        every { BeagleMessageLogs.analyticsQueueIsFull(any()) } just Runs
        every { beagleSdk.analyticsProvider } returns analyticsProvider
        every { analyticsProvider.getMaximumItemsInQueue() } returns 5
        every { analyticsProvider.createRecord(any()) } just Runs
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
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
        fun testCreateLocalScreenRecordShouldCallDataScreenReportReportReturningNotNullReportAndThenShouldCreateTheRecord() {
            //Given
            val analyticsRecord: AnalyticsRecord = mockk()
            every { anyConstructed<DataScreenReport>().report(capture(slot)) } returns analyticsRecord

            //When
            AnalyticsService.createScreenRecord(true, "url")

            //Then
            verify(exactly = 1) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateLocalScreenRecordShouldCallDataScreenReportReportReturningNNullReportAndThenShouldNotCreateTheRecord() {
            //Given
            val analyticsConfig: AnalyticsConfig = mockk()
            val slot = slot<AnalyticsConfig>()
            every { analyticsProvider.getConfig() } returns analyticsConfig
            every { beagleSdk.analyticsProvider } returns analyticsProvider
            every { anyConstructed<DataScreenReport>().report(capture(slot)) } returns null
            //When
            AnalyticsService.createScreenRecord(true, "url")


            //then
            verify(exactly = 0) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateRemoteScreenRecordShouldCallDataScreenReportReportReturningNotNullReportAndThenShouldCreateTheRecord() {
            //Given
            val analyticsRecord: AnalyticsRecord = mockk()
            every { anyConstructed<DataScreenReport>().report(capture(slot)) } returns analyticsRecord

            //When
            AnalyticsService.createScreenRecord(false, "url")

            //Then
            verify(exactly = 1) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateRemoteScreenRecordShouldCallDataScreenReportReportReturningNNullReportAndThenShouldNotCreateTheRecord() {
            //Given
            every { anyConstructed<DataScreenReport>().report(capture(slot)) } returns null

            //When
            AnalyticsService.createScreenRecord(false, "/url")

            //Then
            verify(exactly = 0) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }
    }

    @DisplayName("When create screen record")
    @Nested
    inner class ReportScreenWithAnalyticsConfigNull {
        @BeforeEach
        fun setUp() {
            every { analyticsProvider.getConfig() } returns null
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateLocalScreenRecordShouldReportTheQueueWhenConfigNotNull() {
            //Given
            every { anyConstructed<DataScreenReport>().report(any()) } returns mockk()

            //When
            //this is the flow to add an item on the queue and then report
            AnalyticsService.createScreenRecord(true, "url")
            every { analyticsProvider.getConfig() } returns mockk()
            AnalyticsService.createScreenRecord(true, "url")


            //Then
            verify(exactly = 2) { analyticsProvider.createRecord(any()) }
        }

        @Test
        @DisplayName("Then should call the right functions")
        fun testCreateRemoteScreenRecordShouldReportTheQueueWhenConfigNotNull() {
            //Given
            every { anyConstructed<DataScreenReport>().report(any()) } returns mockk()

            //when
            //this is the flow to add an item on the queue and then report
            AnalyticsService.createScreenRecord(false, "url")
            every { analyticsProvider.getConfig() } returns mockk()
            AnalyticsService.createScreenRecord(true, "url")


            //Then
            verify(exactly = 2) { analyticsProvider.createRecord(any()) }
        }
    }

    @DisplayName("When queue is full and try to add more items")
    @Nested
    inner class QueueIsFull {

        @BeforeEach
        fun setUp() {
            every { analyticsProvider.getConfig() } returns null
            every { analyticsProvider.getMaximumItemsInQueue() } returns 5
        }

        @Test
        @DisplayName("Then should report the queue is full and then report for the last reports")
        fun testQueueIsFullCallBeagleMessagesLogAnalyticsQueueIsFullAndReportTheLastAction() {
            //Given
            every { anyConstructed<DataScreenReport>().report(any()) } returns mockk()
            AnalyticsService.createScreenRecord(false, "url")
            AnalyticsService.createScreenRecord(false, "url")
            AnalyticsService.createScreenRecord(false, "url")
            AnalyticsService.createScreenRecord(false, "url")
            AnalyticsService.createScreenRecord(false, "url")
            //When

            AnalyticsService.createScreenRecord(false, "url")
            every { analyticsProvider.getConfig() } returns mockk()
            AnalyticsService.createScreenRecord(false, "url")

            //Then
            verify(exactly = 1) { BeagleMessageLogs.analyticsQueueIsFull(5) }
            verify(exactly = 6) { analyticsProvider.createRecord(any()) }
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
            every { ActionReportFactory.preGenerateActionAnalyticsConfig(any(), any(), any(), any()) } returns dataActionReport
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
            verify(exactly = 1) { ActionReportFactory.preGenerateActionAnalyticsConfig(any(), any(), any(), any()) }
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
            verify(exactly = 1) { ActionReportFactory.preGenerateActionAnalyticsConfig(any(), any(), any(), any()) }
            verify(exactly = 0) { analyticsProvider.createRecord(any()) }
            assertTrue(slot.isCaptured)
            assertEquals(analyticsConfig, slot.captured)
        }
    }
    //refactor, changing the name (like on ScreenReport With config null, because, in this case, we test the reportDataReport and not the screenReport)
}
