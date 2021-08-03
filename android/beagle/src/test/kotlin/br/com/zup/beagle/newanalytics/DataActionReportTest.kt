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
import br.com.zup.beagle.android.action.AnalyticsAction
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given DataActionReport")
internal class DataActionReportTest : BaseTest() {

    @BeforeEach
    fun setup() {
        mockkObject(ActionReportFactory)
        every { ActionReportFactory.generateAnalyticsRecord(any()) } returns mockk()
    }

    private val ROUTE_URL_CONSTANT = "route.url"
    private val ROUTE_SHOULD_PREFETCH_CONSTANT = "route.shouldPrefetch"
    private val url = "/url"
    private val route = Route.Remote(url = url)
    private val action: AnalyticsAction = Navigate.PushView(route = route)

    private val dataActionReport = DataActionReport(
        originX = 300f,
        originY = 400f,
        attributes = hashMapOf(
            "route.url.length" to 4,
            ROUTE_SHOULD_PREFETCH_CONSTANT to false,
            "route" to route,
            ROUTE_URL_CONSTANT to url
        ),
        id = null,
        type = null,
        analyticsValue = "onPress",
        action = action,
        screenId = "",
        actionType = "beagle:pushView"
    )

    @DisplayName("When report")
    @Nested
    inner class ReportAction {

        @Test
        @DisplayName("Then should call ActionReportFactory.GenerateActionAnalyticsConfig")
        fun testReportAttributesOnActionParametersShouldCallActionReportFactoryGenerateActionAnalyticsConfigWthRightAdditionalEntriesAndAttribute() {
            //Given
            val attributesHashMap = hashMapOf<String, Any>(
                "route" to route,
                "route.url" to url
            )
            action.apply {
                analytics = ActionAnalyticsConfig.Enabled(
                    ActionAnalyticsProperties(listOf("route", "route.url"), hashMapOf("additionalEntries" to "additional"))
                )
            }
            //When
            dataActionReport.report(mockk())

            //then
            verify(exactly = 1) { ActionReportFactory.generateAnalyticsRecord(dataActionReport) }
            assertEquals(attributesHashMap.size, dataActionReport.attributes.size)
            assertEquals(attributesHashMap["route"], dataActionReport.attributes["route"])
            assertEquals(attributesHashMap["route.url"], dataActionReport.attributes["route.url"])
            assertEquals("additional", dataActionReport.additionalEntries?.get("additionalEntries"))
            assertEquals(1, dataActionReport.additionalEntries?.size)
        }

        @Test
        @DisplayName("Then should call ActionReportFactory.GenerateActionAnalyticsConfig")
        fun testReportAttributesWithAnalyticsEnabledOnConfigShouldCallActionReportFactoryGenerateActionAnalyticsConfigWthRightAdditionalEntriesAndAttribute() {
            //Given
            val attributesHashMap = hashMapOf<String, Any>(
                "route" to route,
                "route.url" to url
            )
            action.apply {
                analytics = ActionAnalyticsConfig.Enabled()
            }
            val analyticsConfig: AnalyticsConfig = mockk()
            every { analyticsConfig.actions } returns hashMapOf("beagle:pushView" to listOf("route", "route.url"))

            //When
            dataActionReport.report(analyticsConfig)

            //then
            verify(exactly = 1) { ActionReportFactory.generateAnalyticsRecord(dataActionReport) }
            assertEquals(attributesHashMap.size, dataActionReport.attributes.size)
            assertEquals(attributesHashMap["route"], dataActionReport.attributes["route"])
            assertEquals(attributesHashMap["route.url"], dataActionReport.attributes["route.url"])
            assertEquals(null, dataActionReport.additionalEntries)
        }

        @Test
        @DisplayName("Then should call ActionReportFactory.GenerateActionAnalyticsConfig")
        fun testReportActionWithoutAttributesOnAnalyticsAndConfigShouldCallGenerateActionAnalyticsConfigWithEmptyAttributesOnDataActionReport() {
            //Given
            val analyticsConfig: AnalyticsConfig = mockk()
            action.analytics = ActionAnalyticsConfig.Enabled()
            every { analyticsConfig.actions } returns null

            //When
            dataActionReport.report(analyticsConfig)

            //then
            verify(exactly = 1) { ActionReportFactory.generateAnalyticsRecord(dataActionReport) }
            assertEquals(0, dataActionReport.attributes.size)
        }

        @Test
        @DisplayName("Then should call ActionReportFactory.GenerateActionAnalyticsConfig")
        fun testReportAttributesOnActionAndConfigShouldPrevailAnalyticsFromAction() {
            //Given
            val attributesHashMap = hashMapOf<String, Any>(
                "route" to route,
                "route.url" to url
            )
            action.apply {
                analytics = ActionAnalyticsConfig.Enabled(ActionAnalyticsProperties(listOf("route", "route.url")))
            }
            val analyticsConfig: AnalyticsConfig = mockk()
            every { analyticsConfig.actions } returns hashMapOf("beagle:pushView" to listOf("route.shouldPrefetch"))

            //When
            dataActionReport.report(analyticsConfig)

            //then
            verify(exactly = 1) { ActionReportFactory.generateAnalyticsRecord(dataActionReport) }
            assertEquals(attributesHashMap.size, dataActionReport.attributes.size)
            assertEquals(attributesHashMap["route"], dataActionReport.attributes["route"])
            assertEquals(attributesHashMap["route.url"], dataActionReport.attributes["route.url"])
            assertEquals(null, dataActionReport.additionalEntries)
        }

        @Test
        @DisplayName("Then should call ActionReportFactory.GenerateActionAnalyticsConfig")
        fun testReportAttributesOnConfigAndAdditionalEntriesOnAnalyticsShouldReportWithAnalyticsAndAddtionalEntries() {
            //Given
            val attributesHashMap = hashMapOf<String, Any>(
                "route" to route,
                "route.url" to url
            )
            action.apply {
                analytics = ActionAnalyticsConfig.Enabled(
                    ActionAnalyticsProperties(null, hashMapOf("additionalEntries" to "additional"))
                )
            }
            val analyticsConfig: AnalyticsConfig = mockk()
            every { analyticsConfig.actions } returns hashMapOf("beagle:pushView" to listOf("route", "route.url"))

            //When
            dataActionReport.report(analyticsConfig)

            //then
            verify(exactly = 1) { ActionReportFactory.generateAnalyticsRecord(dataActionReport) }
            assertEquals(attributesHashMap.size, dataActionReport.attributes.size)
            assertEquals(attributesHashMap["route"], dataActionReport.attributes["route"])
            assertEquals(attributesHashMap["route.url"], dataActionReport.attributes["route.url"])
            assertEquals("additional", dataActionReport.additionalEntries?.get("additionalEntries"))
            assertEquals(1, dataActionReport.additionalEntries?.size)
        }

        @Test
        @DisplayName("Then should return null")
        fun testReportWithoutAnalyticsAndActionNotInConfigShouldReturnNull() {
            //Given
            val analyticsConfig: AnalyticsConfig = mockk()
            action.analytics = null
            every { analyticsConfig.actions } returns null

            //When
            val result = dataActionReport.report(analyticsConfig)

            //then
            assertEquals(null, result)
        }
    }
}
