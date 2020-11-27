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
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given Analytics Service")
class AnalyticsServiceTest : BaseTest() {

    private lateinit var analyticsProviderImpl: AnalyticsProviderImpl
    private val action: ActionAnalytics = mockk(relaxed = true)
    private val view: View = mockk()
    private val dataActionReport = DataActionReport(attributes = hashMapOf(), action = action)

    @BeforeEach
    fun setup() {
        every { rootView.activity } returns mockk()
        mockkObject(ScreenReportFactory)
        mockkObject(ActionRecordFactory)
        mockkObject(BeagleMessageLogs)
        every { ActionRecordFactory.preGenerateActionAnalyticsConfig(any(), any(), any(), any()) } returns dataActionReport
        every { ActionRecordFactory.generateActionAnalyticsConfig(any(), any()) } returns mockk()
        every { BeagleMessageLogs.analyticsQueueIsFull(any()) } just Runs

    }

    @AfterEach
    fun teardown() {
        unmockkObject(ScreenReportFactory)
        unmockkObject(ActionRecordFactory)
        unmockkObject(BeagleMessageLogs)
    }

    private fun initAnalyticsService() {
        AnalyticsService.initialConfig(analyticsProviderImpl)
        analyticsProviderImpl.startSession?.invoke()
    }

    @DisplayName("When init the config")
    @Nested
    inner class InitConfig {

        @Test
        @DisplayName("Then should call start session and get the config")
        fun testInitialConfigCallCorrectFunctions() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))

            //WHEN
            initAnalyticsService()

            //THEN
            assertTrue(analyticsProviderImpl.sessionStarted)
            assertTrue(analyticsProviderImpl.configCalled)
        }

        @Test
        @DisplayName("Then should report the items on queue")
        fun testInitialConfigCallReportOnQueue() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))

            //WHEN
            AnalyticsService.initialConfig(analyticsProviderImpl)
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createActionRecord(rootView, view, action)
            analyticsProviderImpl.startSession?.invoke()

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
            verifyOrder {
                ScreenReportFactory.generateRemoteScreenAnalyticsRecord("url")
                ScreenReportFactory.generateRemoteScreenAnalyticsRecord("url")
                AnalyticsService.reportActionIfShould(dataActionReport)
            }
        }

        @Test
        @DisplayName("Then should not report the items on queue")
        fun testAnalyticsProviderNullInitialConfigNotCallReportOnQueue() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))

            //WHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createActionRecord(rootView, view, action)
            initAnalyticsService()

            //THEN
            assertFalse(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should report the the last five itens on queue")
        fun testInitialConfigCallReportOnQueueForJustFiveItems() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))

            //WHEN
            AnalyticsService.initialConfig(analyticsProviderImpl)
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createActionRecord(rootView, view, action)
            AnalyticsService.createActionRecord(rootView, view, action)
            AnalyticsService.createActionRecord(rootView, view, action)
            AnalyticsService.createActionRecord(rootView, view, action)
            AnalyticsService.createActionRecord(rootView, view, action)
            analyticsProviderImpl.startSession?.invoke()

            //THEN
            verify(exactly = 0){
                AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            }
            verifyOrder {
                AnalyticsService.reportActionIfShould(dataActionReport)
                AnalyticsService.reportActionIfShould(dataActionReport)
                AnalyticsService.reportActionIfShould(dataActionReport)
                AnalyticsService.reportActionIfShould(dataActionReport)
                AnalyticsService.reportActionIfShould(dataActionReport)
            }
            verify(exactly = 2){
                BeagleMessageLogs.analyticsQueueIsFull(5)
            }
        }
    }

    @DisplayName("When create screen record")
    @Nested
    inner class ReportScreenIsEnable() {

        @Test
        @DisplayName("Then should create screen record")
        fun testCreateScreenRecordEnableScreenAnalyticsIsTrueCallCreateRecord() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))
            initAnalyticsService()

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should create remote screen record")
        fun testCreateScreenRecordEnableScreenAnalyticsIsTueAndIsNotLocalScreenCallCreateRecord() {
            //GIVEN
            every { ScreenReportFactory.generateRemoteScreenAnalyticsRecord("url") } returns mockk()
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            initAnalyticsService()

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))

            //THEN
            verify(exactly = 1) { ScreenReportFactory.generateRemoteScreenAnalyticsRecord("url") }

        }

        @Test
        @DisplayName("Then should create local screen record")
        fun testCreateScreenRecordEnableScreenAnalyticsIsTueAndIsLocalScreenCallCreateRecord() {

            //GIVEN
            every { ScreenReportFactory.generateLocalScreenAnalyticsRecord("screenId") } returns mockk()
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            initAnalyticsService()

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(true, "screenId"))

            //THEN
            verify(exactly = 1) { ScreenReportFactory.generateLocalScreenAnalyticsRecord("screenId") }
        }
    }

    @DisplayName("When create screen record")
    @Nested
    inner class ReportScreenIsNotEnabled() {

        @Test
        @DisplayName("Then shouldn't create screen report")
        fun testCreateScreenRecordScreenAnalyticsIsNotEnableNotCallCreateRecord() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(enableScreenAnalytics = false, actions = hashMapOf())
            )
            initAnalyticsService()

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))

            //THEN
            assertFalse(analyticsProviderImpl.createRecordCalled)
        }
    }

    @DisplayName("When create action record")
    @Nested
    inner class ReportAction {

        @Test
        @DisplayName("Then should create record")
        fun testActionWithAnaActionAnalyticsConfigCallCreateRecord() {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            initAnalyticsService()
            every { action.analytics?.enable } returns true
            //WHEN
            AnalyticsService.createActionRecord(rootView, view, action)

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should create record")
        fun testActionOnAnalyticsConfigCallCreateRecord() {
            //GIVEN
            val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("custom:AddChildren" to listOf()))
            analyticsProviderImpl = AnalyticsProviderImpl(
                analyticsConfig
            )
            every { action.analytics } returns null
            every { action.type } returns "custom:AddChildren"
            initAnalyticsService()

            //WHEN
            AnalyticsService.createActionRecord(rootView, view, action)

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should create record with right parrameters")
        fun testActionWithAttributesOnActionAnalyticsConfigCallCreateRecordWithCorrectParameters() {
            //GIVEN
            every { ActionRecordFactory.generateActionAnalyticsConfig(any(), any()) } returns mockk()

            val actionAnalyticsConfig = ActionAnalyticsConfig(enable = true, attributes = listOf("componentId"))
            every { action.analytics } returns actionAnalyticsConfig
            every { action.type } returns "custom:AddChildren"
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            initAnalyticsService()
            var dataReport =  ActionRecordFactory.preGenerateActionAnalyticsConfig(rootView, view, action)
            //WHEN
            AnalyticsService.createActionRecord(rootView, view, action)

            //THEN
            verify(exactly = 1) { ActionRecordFactory.generateActionAnalyticsConfig(dataReport, actionAnalyticsConfig) }

        }

        @Test
        @DisplayName("Then should create record with right parrameters")
        fun testActionWithAttributesOnAnalyticsConfigCallCreateRecordWithCorrectParameters() {
            //GIVEN
            every { ActionRecordFactory.generateActionAnalyticsConfig(any(), any()) } returns mockk()
            every { action.analytics } returns null
            every { action.type } returns "custom:AddChildren"

            val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("custom:AddChildren" to listOf("componentId")))
            analyticsProviderImpl = AnalyticsProviderImpl(
                analyticsConfig
            )
            initAnalyticsService()

            //WHEN
            AnalyticsService.createActionRecord(rootView, view, action)

            //THEN
            verify(exactly = 1) { ActionRecordFactory.generateActionAnalyticsConfig(any(), ActionAnalyticsConfig(enable = true, attributes = listOf("componentId"))) }
        }
    }

    @DisplayName("When create action record")
    @Nested
    inner class NotReportAction {
        @Test
        @DisplayName("Then shouldn't create recort")
        fun testActionWithActionAnalyticsConfigDisableAndActionOnHashmapDontCallCreateRecord() {
            //GIVEN
            val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("action" to listOf()))
            analyticsProviderImpl = AnalyticsProviderImpl(
                analyticsConfig
            )
            initAnalyticsService()
            every { action.analytics?.enable } returns false

            //WHEN
            AnalyticsService.createActionRecord(rootView, view, action)

            //THEN
            assertFalse(analyticsProviderImpl.createRecordCalled)
        }

    }

    class AnalyticsProviderImpl(val config: AnalyticsConfig) : AnalyticsProvider {

        var sessionStarted: Boolean = false
        var configCalled: Boolean = false
        var createRecordCalled: Boolean = false
        var startSession: (() -> Unit)? = null
        override fun getConfig(config: (analyticConfig: AnalyticsConfig) -> Unit) {
            config.invoke(this.config)
            configCalled = true
        }

        override fun startSession(startSession: () -> Unit) {
            sessionStarted = true
            this.startSession = startSession
        }

        override fun createRecord(record: AnalyticsRecord) {
            createRecordCalled = true
        }

        override fun getMaximumItemsInQueue() = 5
    }

    class AnalyticsConfigImpl(
        override var enableScreenAnalytics: Boolean? = true,
        override var actions: Map<String, List<String>>
    ) : AnalyticsConfig

    internal data class TestActionAnalytics(
        val route: Route,
        override var analytics: ActionAnalyticsConfig? = null,
        override val type: String?
    ) : ActionAnalytics() {
        override fun execute(rootView: RootView, origin: View, originComponent: ServerDrivenComponent?) {
        }
    }
}
