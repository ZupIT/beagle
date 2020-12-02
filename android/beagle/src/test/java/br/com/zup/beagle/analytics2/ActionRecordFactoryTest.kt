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
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.logger.BeagleMessageLogs
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
import org.junit.jupiter.api.assertThrows

@DisplayName("Given a Action Record Creator")
internal class ActionRecordFactoryTest : BaseTest() {

    private val origin: View = mockk()

    @BeforeEach
    fun setup() {
        every { origin.x } returns 300f
        every { origin.y } returns 400f
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
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
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
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
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

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsServerDrivenComponent() {
            //GIVEN
            val action: ActionAnalytics = mockk()
            val originComponent: WidgetView = Text("test")
            val componentReport = hashMapOf<String, Any>(
                "position" to hashMapOf("x" to 300f, "y" to 400f),
                "type" to "beagle:Text"
            )
            every { rootView.getScreenId() } returns ""
            every { origin.x } returns 300f
            every { origin.y } returns 400f
            every { action.analytics } returns null

            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(originComponent = originComponent)
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf())
            )

            //THEN
            Assert.assertEquals(componentReport, report.attributes["component"])
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsWidgetViewWithAnId() {
            //GIVEN
            val action: ActionAnalytics = mockk()
            val originComponent: WidgetView = Text("test")
            originComponent.id = "text-id"
            val componentReport = hashMapOf<String, Any>(
                "type" to "beagle:button",
                "id" to "text-id",
                "position" to hashMapOf("x" to 300f, "y" to 400f),
                "type" to "beagle:Text"
            )
            every { rootView.getScreenId() } returns ""
            every { origin.x } returns 300f
            every { origin.y } returns 400f
            every { action.analytics } returns null

            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(originComponent = originComponent)
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf())
            )

            //THEN
            Assert.assertEquals(componentReport, report.attributes["component"])
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsWidgetViewWithoutId() {
            //GIVEN
            val action: ActionAnalytics = mockk()
            val originComponent: WidgetView = Text("test")
            val componentReport = hashMapOf<String, Any>(
                "type" to "beagle:Text",
                "position" to hashMapOf("x" to 300f, "y" to 400f)
            )
            originComponent.id = null
            every { rootView.getScreenId() } returns ""
            every { origin.x } returns 300f
            every { origin.y } returns 400f
            every { action.analytics } returns null

            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(originComponent = originComponent)
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf())
            )

            //THEN
            Assert.assertEquals(componentReport, report.attributes["component"])
        }
    }

    @DisplayName("When create record")
    @Nested
    inner class ActionAttribute {
        private val url = "/url"
        private val route = Route.Remote(url = "/url")
        private val actionType = "beagle:TestActionAnalytics"
        private val action: ActionAnalytics = TestActionAnalytics(route = route)

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testSimpleActionAttribute() {
            //GIVEN
            every { rootView.getScreenId() } returns ""

            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(analyticsValue = "onPress")
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
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
            //GIVEN
            every { rootView.getScreenId() } returns ""

            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(analyticsValue = "onPress")
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
                dataActionReport,
                ActionAnalyticsConfig(enable = true, attributes = listOf("route.url", "route.shouldPrefetch"))
            )

            //THEN
            commonAsserts(report)
            Assert.assertEquals(url, report.attributes["route.url"])
            Assert.assertEquals(false, report.attributes["route.shouldPrefetch"])
        }

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testWrongComposeActionAttribute() {
            //GIVEN
            every { rootView.getScreenId() } returns ""

            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(analyticsValue = "onPress")
            )
            val report = ActionRecordFactory.generateActionAnalyticsConfig(
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
        private val action: ActionAnalytics = TestActionAnalytics(route = route)

        @DisplayName("Then should create correct data action report")
        @Test
        fun testPreGenerateActionAnalyticsConfigCreateCorrectDataActionReport() {
            //GIVEN
            every { rootView.getScreenId() } returns ""
            val expectedDataReport = DataActionReport(
                originX = 300f,
                originY = 400f,
                attributes = hashMapOf(
                    "route.url.length" to 4,
                    "route.shouldPrefetch" to false,
                    "route" to route,
                    "route.url" to url,
                    "list" to listOf<ServerDrivenComponent>()
                ),
                id = null,
                type = null,
                analyticsValue = "onPress",
                action = action,
                screenId = "",
                actionType = "beagle:TestActionAnalytics"
            )
            //WHEN
            val dataActionReport = ActionRecordFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                AnalyticsHandleEvent(analyticsValue = "onPress")
            )

            //THEN
            Assert.assertEquals(expectedDataReport, dataActionReport)
        }
    }

    internal data class TestActionAnalytics(
        val route: Route,
        var list: List<ServerDrivenComponent> = listOf(),
        override var analytics: ActionAnalyticsConfig? = null
    ) : ActionAnalytics() {
        override fun execute(rootView: RootView, origin: View, originComponent: ServerDrivenComponent?) {
        }
    }
}
