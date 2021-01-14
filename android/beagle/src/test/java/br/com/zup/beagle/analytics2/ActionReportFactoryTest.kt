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
import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Action Report Factory")
internal class ActionReportFactoryTest : BaseTest() {

    private val origin: View = mockk()
    private val ROUTE_URL_CONSTANT = "route.url"
    private val ROUTE_SHOULD_PREFETCH_CONSTANT = "route.shouldPrefetch"

    @BeforeEach
    fun setup() {
        every { origin.x } returns 300f
        every { origin.y } returns 400f
        every { origin.getTag(R.id.beagle_component_type) } returns "beagle:text"
        every { origin.getTag(R.id.beagle_component_id) } returns null
    }

    @DisplayName("When create record")
    @Nested
    inner class ScreenKeyAttribute {

        @Test
        @DisplayName("Then should return hash map with rootview screenId")
        fun testRootViewWithScreenIdReturnHashMapWithScreenKey() {
            //GIVEN
            val action: ActionAnalytics = mockk()
            every { rootView.getScreenId() } returns "/screen"
            every { action.analytics } returns null

            //WHEN
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf())
            )

            //THEN
            Assert.assertEquals("/screen", report.attributes["screen"])
        }
    }

    @DisplayName("When create record")
    @Nested
    inner class PlatformAndTypeKey {

        @Test
        @DisplayName("Then should return platform as android and type as action")
        fun testPlatformAnTypeWithCorrectValue() {
            //GIVEN
            val action: ActionAnalytics = mockk()
            every { rootView.getScreenId() } returns ""
            every { action.analytics } returns null

            //WHEN
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf())
            )

            //THEN
            Assert.assertEquals("android", report.platform)
            Assert.assertEquals("action", report.type)
        }
    }

    @DisplayName("When create record")
    @Nested
    inner class ComponentAttribute {

        private val action: ActionAnalytics = mockk()
        private val originComponent: WidgetView = Text("test")

        @BeforeEach
        fun setup() {
            every { rootView.getScreenId() } returns ""
            every { origin.x } returns 300f
            every { origin.y } returns 400f
            every { action.analytics } returns null
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsServerDrivenComponent() {
            //GIVEN
            val componentReport = generateComponentReport()
            val dataActionReport = generateDataActionReport()

            //WHEN

            val report = reportDataAction(dataActionReport)

            //THEN
            Assert.assertEquals(componentReport, report.attributes["component"])
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsWidgetViewWithAnId() {
            //GIVEN
            every { origin.getTag(R.id.beagle_component_id) } returns "text-id"
            val componentReport = hashMapOf<String, Any>("type" to "beagle:button", "id" to "text-id")
            val componentReportAux = generateComponentReport()
            componentReport.putAll(componentReportAux)
            val dataActionReport = generateDataActionReport()

            //WHEN
            val report = reportDataAction(dataActionReport)

            //THEN
            Assert.assertEquals(componentReport, report.attributes["component"])
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsWidgetViewWithoutId() {
            //GIVEN
            val componentReport = generateComponentReport()
            originComponent.id = null
            val dataActionReport = generateDataActionReport()

            //WHEN
            val report = reportDataAction(dataActionReport)

            //THEN
            Assert.assertEquals(componentReport, report.attributes["component"])
        }

        private fun generateComponentReport() = hashMapOf<String, Any>(
            "position" to hashMapOf("x" to 300f, "y" to 400f),
            "type" to "beagle:text"
        )

        private fun generateDataActionReport() = ActionReportFactory.preGenerateActionAnalyticsConfig(
            rootView,
            origin,
            action,
        )

        private fun reportDataAction(
            dataActionReport: DataActionReport) = ActionReportFactory.generateActionAnalyticsConfig(
            dataActionReport,
            ActionAnalyticsConfig(enable = true, attributes = listOf())
        )
    }

    @DisplayName("When create record")
    @Nested
    inner class ActionAttribute {
        private val url = "/url"
        private val route = Route.Remote(url = "/url")
        private val actionType = "beagle:pushView"
        private val action: ActionAnalytics = Navigate.PushView(route = route)

        @BeforeEach
        fun setup(){
            every { rootView.getScreenId() } returns ""

        }
        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testSimpleActionAttribute() {
            //WHEN
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf("route"))
            )

            //THEN
            commonAsserts(report)
            Assert.assertEquals(route, report.attributes["route"])
        }

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testComposeActionAttribute() {
            //WHEN
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf(ROUTE_URL_CONSTANT, ROUTE_SHOULD_PREFETCH_CONSTANT))
            )

            //THEN
            commonAsserts(report)
            Assert.assertEquals(url, report.attributes[ROUTE_URL_CONSTANT])
            Assert.assertEquals(false, report.attributes[ROUTE_SHOULD_PREFETCH_CONSTANT])
        }

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testWrongComposeActionAttribute() {
            //WHEN
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf("route.a"))
            )
            print(report)
            //THEN
            commonAsserts(report)
            Assert.assertEquals(null, report.attributes["route.a"])
        }

        private fun commonAsserts(report: AnalyticsRecord) {
            Assert.assertEquals("onPress", report.attributes["event"])
            Assert.assertEquals(actionType, report.attributes["beagleAction"])
        }

    }

    @DisplayName("When preGenerateActionAnalyticsConfig")
    @Nested
    inner class PreGenerateActionAnalyticsConfig {
        private val url = "/url"
        private val route = Route.Remote(url = url)
        private val action: ActionAnalytics = Navigate.PushView(route = route)

        @DisplayName("Then should create correct data action report")
        @Test
        fun testPreGenerateActionAnalyticsConfigCreateCorrectDataActionReport() {
            //GIVEN
            every { rootView.getScreenId() } returns ""
            every { origin.getTag(R.id.beagle_component_type) } returns null
            val expectedDataReport = DataActionReport(
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
            //WHEN
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )

            //THEN
            Assert.assertEquals(expectedDataReport, dataActionReport)
        }
    }
}
