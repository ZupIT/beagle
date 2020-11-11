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
import br.com.zup.beagle.android.action.AddChildren
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsServiceTest : BaseTest() {

    private lateinit var analyticsProviderImpl: AnalyticsProviderImpl
    private val action: ActionAnalytics = mockk(relaxed = true, relaxUnitFun = true)
    private lateinit var analyticsService: AnalyticsService
    private val view: View = mockk()

    @Test
    fun `GIVEN an AnalyticsProvider WHEN init SHOULD call startSession and getConfig`() {
        //GIVEN
        analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))

        //WHEN
        AnalyticsService(analyticsProviderImpl)

        //THEN
        assertTrue(analyticsProviderImpl.sessionStarted)
        assertTrue(analyticsProviderImpl.configCalled)
    }

    @Test
    fun `GIVEN an AnalyticsConfigImpl with enableScreenAnalytics = true  WHEN createScreenRecord SHOULD call createRecord`() {
        //GIVEN
        analyticsProviderImpl = AnalyticsProviderImpl(AnalyticsConfigImpl(actions = hashMapOf()))
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //wHEN
        analyticsService.createScreenRecord(false, "url")

        //THEN
        assertTrue(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN an AnalyticsConfigImpl with enableScreenAnalytics = false  WHEN createScreenRecord SHOULD not call createRecord`() {
        //GIVEN
        analyticsProviderImpl = AnalyticsProviderImpl(
            AnalyticsConfigImpl(enableScreenAnalytics = false, actions = hashMapOf())
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)


        //wHEN
        analyticsService.createScreenRecord(false, "url")

        //THEN
        assertFalse(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN a isLocalScreen = false  WHEN createScreenRecord SHOULD call ScreenReportCreator createScreenRemoteReport`() {
        //GIVEN
        mockkObject(ScreenReportCreator)
        every { ScreenReportCreator.createScreenRemoteReport("url") } returns mockk()
        analyticsProviderImpl = AnalyticsProviderImpl(
            AnalyticsConfigImpl(actions = hashMapOf())
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //wHEN
        analyticsService.createScreenRecord(false, "url")

        //THEN
        verify(exactly = 1) { ScreenReportCreator.createScreenRemoteReport("url") }

    }

    @Test
    fun `GIVEN a isLocalScreen = true  WHEN createScreenRecord SHOULD call ScreenReportCreator createScreenLocalReport`() {
        //GIVEN
        mockkObject(ScreenReportCreator)
        every { ScreenReportCreator.createScreenLocalReport("screenId") } returns mockk()
        analyticsProviderImpl = AnalyticsProviderImpl(
            AnalyticsConfigImpl(actions = hashMapOf())
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //wHEN
        analyticsService.createScreenRecord(true, "screenId")

        //THEN
        verify(exactly = 1) { ScreenReportCreator.createScreenLocalReport("screenId") }
    }

    @Test
    fun `GIVEN a action with ActionAnalyticsConfig enable WHEN createActionRecord SHOULD  call createRecord`() {
        //GIVEN
        analyticsProviderImpl = AnalyticsProviderImpl(
            AnalyticsConfigImpl(actions = hashMapOf())
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)
        every { action.analytics?.enable } returns true
        //WHEN
        analyticsService.createActionRecord(rootView, view, action)

        //THEN
        assertTrue(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN a action with ActionAnalyticsConfig disable and action on hashmap WHEN createActionRecord SHOULD not call createRecord`() {
        //GIVEN
        val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("action" to listOf()))
        analyticsProviderImpl = AnalyticsProviderImpl(
            analyticsConfig
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)
        every { action.analytics?.enable } returns false

        //WHEN
        analyticsService.createActionRecord(rootView, view, action)

        //THEN
        assertFalse(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN a action on analyticsConfig WHEN createActionRecord SHOULD call createRecord`() {
        //GIVEN
        val action: ActionAnalytics = AddChildren("id", listOf(), type = "custom:AddChildren")
        val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("custom:AddChildren" to listOf()))
        analyticsProviderImpl = AnalyticsProviderImpl(
            analyticsConfig
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //WHEN
        analyticsService.createActionRecord(rootView, view, action)

        //THEN
        assertTrue(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN a action on analyticsConfig WHEN createActionRecord SHOULD call ActionRecordCreator createRecord passing an ActionAnalyticsConfig with attributes listed on config and enable = true`() {
        //GIVEN
        mockkObject(ActionRecordCreator)
        every { ActionRecordCreator.createRecord(any(), any(), any(), any(), any()) } returns mockk()
        val action: ActionAnalytics = AddChildren("id", listOf(), type = "custom:AddChildren")
        val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("custom:AddChildren" to listOf("componentId")))
        analyticsProviderImpl = AnalyticsProviderImpl(
            analyticsConfig
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //WHEN
        analyticsService.createActionRecord(rootView, view, action)

        //THEN
        verify(exactly = 1) { ActionRecordCreator.createRecord(rootView, view, ActionAnalyticsConfig(enable = true, attributes = listOf("componentId")), action, null) }
    }

    @Test
    fun `GIVEN a action with ActionAnalyticsConfig WHEN createActionRecord SHOULD call ActionRecordCreator createRecord passing the sameActionAnalyticsConfig`() {
        //GIVEN
        mockkObject(ActionRecordCreator)
        every { ActionRecordCreator.createRecord(any(), any(), any(), any(), any()) } returns mockk()

        val actionAnalyticsConfig = ActionAnalyticsConfig(enable = true, attributes = listOf("componentId"))
        val action: ActionAnalytics = AddChildren("id", listOf(), type = "custom:AddChildren", analytics = actionAnalyticsConfig)
        analyticsProviderImpl = AnalyticsProviderImpl(
            AnalyticsConfigImpl(actions = hashMapOf())
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //WHEN
        analyticsService.createActionRecord(rootView, view, action)

        //THEN
        verify(exactly = 1) { ActionRecordCreator.createRecord(rootView, view, actionAnalyticsConfig, action, null) }

    }

    class AnalyticsProviderImpl(val config: AnalyticsConfig) : AnalyticsProvider {

        var sessionStarted: Boolean = false
        var configCalled: Boolean = false
        var createRecordCalled: Boolean = false

        override fun getConfig(config: (analyticConfig: AnalyticsConfig) -> Unit) {
            configCalled = true
            config.invoke(this.config)
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