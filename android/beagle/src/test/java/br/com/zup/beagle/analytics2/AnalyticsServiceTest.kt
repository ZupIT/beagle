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

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.action.AddChildren
import br.com.zup.beagle.android.setup.BeagleEnvironment
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class AnalyticsServiceTest : BaseTest() {

    private lateinit var analyticsProviderImpl: AnalyticsProviderImpl
    private val action: ActionAnalytics = mockk(relaxed = true, relaxUnitFun = true)
    private lateinit var analyticsService: AnalyticsService
    private val hashMapMocked: HashMap<String, Any> = mockk()

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
        analyticsService.createScreenRecord(hashMapMocked)

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
        analyticsService.createScreenRecord(hashMapMocked)

        //THEN
        assertFalse(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN a hashMap  WHEN createScreenRecord SHOULD create record with correct record`() {
        //GIVEN
        analyticsProviderImpl = AnalyticsProviderImpl(
            AnalyticsConfigImpl(actions = hashMapOf())
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)
        val attribute: HashMap<String, Any> = hashMapOf("url" to "url")

        //wHEN
        analyticsService.createScreenRecord(attribute)

        //THEN
        assertEquals("android", analyticsProviderImpl.record?.platform)
        assertEquals("screen", analyticsProviderImpl.record?.type)
        assertEquals(hashMapOf("url" to "url"), analyticsProviderImpl.record?.attributes)
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
        analyticsService.createActionRecord(action)

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
        analyticsService.createActionRecord(action)

        //THEN
        assertFalse(analyticsProviderImpl.createRecordCalled)
    }

    @Test
    fun `GIVEN a action on analyticsConfig WHEN createActionRecord SHOULD call createRecord`() {
        //GIVEN
        val action: Action = AddChildren("id", listOf())
        every { BeagleEnvironment.beagleSdk.registeredActions() } returns listOf(action::class.java as Class<Action>)
        val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("Custom:AddChildren" to listOf()))
        analyticsProviderImpl = AnalyticsProviderImpl(
            analyticsConfig
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //WHEN
        analyticsService.createActionRecord(action)

        //THEN
        assertTrue(analyticsProviderImpl.createRecordCalled)
    }

//    @Test
//    fun `GIVEN a action with attributes WHEN createActionRecord SHOULD create record with correct record`() {
//        val action: ActionAnalytics = AddChildren(componentId = "id", value = listOf(), mode = null, analytics = ActionAnalyticsConfig(enable = true, attributes = listOf("componentId")))
//        every { BeagleEnvironment.beagleSdk.registeredActions() } returns listOf()
//        val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf())
//        analyticsProviderImpl = AnalyticsProviderImpl(
//            analyticsConfig
//        )
//        analyticsService = AnalyticsService(analyticsProviderImpl)
//
//        //WHEN
//        analyticsService.createActionRecord(action)
//
//        //THEN
//        assertEquals("android", analyticsProviderImpl.record?.platform)
//        assertEquals("action", analyticsProviderImpl.record?.type)
//        assertEquals(hashMapOf("componentId" to "id"), analyticsProviderImpl.record?.attributes)
//    }

    @Test
    fun `GIVEN a action on config with attributes WHEN createActionRecord SHOULD create record with correct record`() {
        val action: Action = AddChildren("id", listOf())
        every { BeagleEnvironment.beagleSdk.registeredActions() } returns listOf()
        val analyticsConfig: AnalyticsConfig = AnalyticsConfigImpl(actions = hashMapOf("Beagle:AddChildren" to listOf("componentId")))
        analyticsProviderImpl = AnalyticsProviderImpl(
            analyticsConfig
        )
        analyticsService = AnalyticsService(analyticsProviderImpl)

        //WHEN
        analyticsService.createActionRecord(action)

        //THEN
        assertEquals("android", analyticsProviderImpl.record?.platform)
        assertEquals("action", analyticsProviderImpl.record?.type)
        assertEquals(hashMapOf("componentId" to "id"), analyticsProviderImpl.record?.attributes)
    }

    class AnalyticsProviderImpl(val config: AnalyticsConfig) : AnalyticsProvider {

        var sessionStarted: Boolean = false
        var configCalled: Boolean = false
        var createRecordCalled: Boolean = false
        var record: AnalyticsRecord? = null

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
            this.record = record
        }
    }

    class AnalyticsConfigImpl(
        override var enableScreenAnalytics: Boolean? = true,
        override var actions: Map<String, List<String>>
    ) : AnalyticsConfig
}