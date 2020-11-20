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
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.action.AddChildren
import br.com.zup.beagle.android.engine.renderer.ActivityRootView
import br.com.zup.beagle.android.testutil.CoroutinesTestExtension
import br.com.zup.beagle.android.testutil.InstantExecutorExtension
import io.mockk.*
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@DisplayName("Given Analytics Service")
@ExtendWith(CoroutinesTestExtension::class)
class AnalyticsServiceTest {

    private lateinit var analyticsProviderImpl: AnalyticsProviderImpl
    private val action: ActionAnalytics = mockk(relaxed = true, relaxUnitFun = true)
    private val view: View = mockk()
    protected val rootView = mockk<ActivityRootView>(relaxed = true, relaxUnitFun = true)

    @BeforeEach
    fun setUp() {
        every { rootView.activity } returns mockk()
        mockkObject(ScreenReportFactory)
        mockkObject(ActionRecordFactory)
    }

    @DisplayName("When init the config")
    @Nested
    inner class InitConfig {

        @Test
        @DisplayName("Then should call start session and get the config")
        fun testInitialConfigCallCorrectFunctions() = runBlockingTest {
            //GIVEN
            val analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))

            //WHEN
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //THEN
            assertTrue(analyticsProviderImpl.sessionStarted)
            assertTrue(analyticsProviderImpl.configCalled)
        }

        @Test
        @DisplayName("Then should report the action on queue")
        fun testInitialConfigCallReportOnQueue() = runBlockingTest {
            //GIVEN
            val analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))
            //WHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))
            AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))


            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
            verifyOrder{
                ScreenReportFactory.createScreenRemoteReport("url")
                ScreenReportFactory.createScreenRemoteReport("url")
                AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))

            }
        }

    }

    @DisplayName("When create screen record")
    @Nested
    inner class ReportScreenIsEnable() {

        @Test
        @DisplayName("Then should create screen record")
        fun testCreateScreenRecordEnableScreenAnalyticsIsTrueCallCreateRecord() = runBlockingTest {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should create remote screen record")
        fun testCreateScreenRecordEnableScreenAnalyticsIsTueAndIsNotLocalScreenCallCreateRecord() = runBlockingTest {
            //GIVEN
            mockkObject(ScreenReportFactory)
            every { ScreenReportFactory.createScreenRemoteReport("url") } returns mockk()
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(false, "url"))

            //THEN
            verify(exactly = 1) { ScreenReportFactory.createScreenRemoteReport("url") }

        }

        @Test
        @DisplayName("Then should create local screen record")
        fun testCreateScreenRecordEnableScreenAnalyticsIsTueAndIsLocalScreenCallCreateRecord() = runBlockingTest {

            //GIVEN
            mockkObject(ScreenReportFactory)
            every { ScreenReportFactory.createScreenLocalReport("screenId") } returns mockk()
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //wHEN
            AnalyticsService.createScreenRecord(DataScreenReport(true, "screenId"))

            //THEN
            verify(exactly = 1) { ScreenReportFactory.createScreenLocalReport("screenId") }
        }
    }

    @DisplayName("When create screen record")
    @Nested
    inner class ReportScreenIsNotEnabled() {

        @Test
        @DisplayName("Then shouldn't create screen report")
        fun testCreateScreenRecordScreenAnalyticsIsNotEnableNotCallCreateRecord() = runBlockingTest {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(enableScreenAnalytics = false, actions = hashMapOf())
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)


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
        fun testActionWithAnaActionAnalyticsConfigCallCreateRecord() = runBlockingTest {
            //GIVEN
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)
            every { action.analytics?.enable } returns true
            //WHEN
            AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should create record")
        fun testActionOnAnalyticsConfigCallCreateRecord() = runBlockingTest {
            //GIVEN
            val action: ActionAnalytics = mockk()
            val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("custom:AddChildren" to listOf()))
            analyticsProviderImpl = AnalyticsProviderImpl(
                analyticsConfig
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //WHEN
            AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))

            //THEN
            assertTrue(analyticsProviderImpl.createRecordCalled)
        }

        @Test
        @DisplayName("Then should create record with right parrameters")
        fun testActionWithAttributesOnActionAnalyticsConfigCallCreateRecordWithCorrectParameters() = runBlockingTest {
            //GIVEN
            mockkObject(ActionRecordFactory)
            every { ActionRecordFactory.createRecord(any(), any()) } returns mockk()

            val actionAnalyticsConfig = ActionAnalyticsConfig(enable = true, attributes = listOf("componentId"))
            val action: ActionAnalytics = mockk()
            analyticsProviderImpl = AnalyticsProviderImpl(
                AnalyticsConfigImpl(actions = hashMapOf())
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //WHEN
            AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))

            //THEN
            verify(exactly = 1) { ActionRecordFactory.createRecord(DataActionReport(rootView, view, action), actionAnalyticsConfig) }

        }

        @Test
        @DisplayName("Then should create record with right parrameters")
        fun testActionWithAttributesOnAnalyticsConfigCallCreateRecordWithCorrectParameters() = runBlockingTest {
            //GIVEN
            mockkObject(ActionRecordFactory)
            every { ActionRecordFactory.createRecord(any(), any()) } returns mockk()
            val action: ActionAnalytics = mockk()
            val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("custom:AddChildren" to listOf("componentId")))
            analyticsProviderImpl = AnalyticsProviderImpl(
                analyticsConfig
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)

            //WHEN
            AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))

            //THEN
            verify(exactly = 1) { ActionRecordFactory.createRecord(DataActionReport(rootView, view, action), ActionAnalyticsConfig(enable = true, attributes = listOf("componentId"))) }
        }
    }

    @DisplayName("When create action record")
    @Nested
    inner class NotReportAction {
        @Test
        @DisplayName("Then shouldn't create recort")
        fun testActionWithActionAnalyticsConfigDisableAndActionOnHashmapDontCallCreateRecord() = runBlockingTest {
            //GIVEN
            val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("action" to listOf()))
            analyticsProviderImpl = AnalyticsProviderImpl(
                analyticsConfig
            )
            AnalyticsService.initialConfig(analyticsProviderImpl, this)
            every { action.analytics?.enable } returns false

            //WHEN
            AnalyticsService.createActionRecord(DataActionReport(rootView, view, action))

            //THEN
            assertFalse(analyticsProviderImpl.createRecordCalled)
        }

    }

    class AnalyticsProviderImpl(val config: AnalyticsConfig) : AnalyticsProvider {

        var sessionStarted: Boolean = false
        var configCalled: Boolean = false
        var createRecordCalled: Boolean = false

        override fun getConfig(config: (analyticConfig: AnalyticsConfig) -> Unit) {
            config.invoke(this.config)
            configCalled = true
        }

        override fun startSession(startSession: () -> Unit) {
            sessionStarted = true
            startSession.invoke()
        }

        override fun createRecord(record: AnalyticsRecord) {
            createRecordCalled = true
        }
    }

    class AnalyticsConfigImpl(
        override var enableScreenAnalytics: Boolean? = true,
        override var actions: Map<String, List<String>>
    ) : AnalyticsConfig
}
