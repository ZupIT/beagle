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
import br.com.zup.beagle.R
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.action.Action
import br.com.zup.beagle.android.action.ActionAnalytics
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterAction
import br.com.zup.beagle.core.BeagleJson
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given a Action Report Factory")
internal class ActionReportFactoryTest : BaseTest() {

    private val origin: View = mockk(relaxed = true)
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
    inner class ScreenKeyValues {

        @Test
        @DisplayName("Then should return hash map with rootview screenId")
        fun testRootViewWithScreenIdReturnHashMapWithScreenKey() {
            //Given
            val action: ActionAnalytics = mockk()
            every { rootView.getScreenId() } returns "/screen"
            every { action.analytics } returns null

            //When
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport
            )

            //Then
            Assert.assertEquals("/screen", report.values["screen"])
        }
    }

    @DisplayName("When create record")
    @Nested
    inner class PlatformAndTypeKey {

        @Test
        @DisplayName("Then should return platform as android and type as action")
        fun testPlatformAnTypeWithCorrectValue() {
            //Given
            val action: ActionAnalytics = mockk()
            every { rootView.getScreenId() } returns ""
            every { action.analytics } returns null

            //When
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport
            )

            //Then
            Assert.assertEquals("android", report.platform)
            Assert.assertEquals("action", report.type)
        }
    }

    @DisplayName("When create record")
    @Nested
    inner class ComponentValues {

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
            //Given
            val componentReport = generateComponentReport()
            val dataActionReport = generateDataActionReport()

            //When
            val report = reportDataAction(dataActionReport)

            //Then
            Assert.assertEquals(componentReport, report.values["component"])
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsWidgetViewWithAnId() {
            //Given
            every { origin.getTag(R.id.beagle_component_id) } returns "text-id"
            val componentReport = hashMapOf<String, Any>("type" to "beagle:button", "id" to "text-id")
            val componentReportAux = generateComponentReport()
            componentReport.putAll(componentReportAux)
            val dataActionReport = generateDataActionReport()

            //When
            val report = reportDataAction(dataActionReport)

            //Then
            Assert.assertEquals(componentReport, report.values["component"])
        }

        @Test
        @DisplayName("Then should return correct value to component key without crash")
        fun testOriginComponentAsWidgetViewWithoutId() {
            //Given
            val componentReport = generateComponentReport()
            originComponent.id = null
            val dataActionReport = generateDataActionReport()

            //When
            val report = reportDataAction(dataActionReport)

            //Then
            Assert.assertEquals(componentReport, report.values["component"])
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
            dataActionReport
        )
    }

    @DisplayName("When create record")
    @Nested
    inner class ActionAttributeAndAdditionalEntries {
        private val url = "/url"
        private val route = Route.Remote(url = "/url")
        private val actionType = "beagle:pushView"
        private val action: ActionAnalytics = Navigate.PushView(route = route)

        @BeforeEach
        fun setup() {
            every { rootView.getScreenId() } returns ""

        }

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testSimpleActionAttribute() {
            //When
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )
            dataActionReport.additionalEntries = hashMapOf("additionalEntries" to true)
            val report = ActionReportFactory.generateActionAnalyticsConfig(dataActionReport)

            //Then
            commonAsserts(report, dataActionReport)
            Assert.assertEquals(route, report.values["route"])
            assertTrue(report.values["additionalEntries"] as Boolean)
        }

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testComposeActionAttribute() {
            //When
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport
            )

            //Then
            commonAsserts(report, dataActionReport)
            Assert.assertEquals(url, report.values[ROUTE_URL_CONSTANT])
            Assert.assertEquals(false, report.values[ROUTE_SHOULD_PREFETCH_CONSTANT])
        }

        @Test
        @DisplayName("Then should return correct value to action attribute key without crash")
        fun testWrongComposeActionAttribute() {
            //When
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )
            val report = ActionReportFactory.generateActionAnalyticsConfig(
                dataActionReport,
            )
            print(report)
            //Then
            commonAsserts(report, dataActionReport)
            Assert.assertEquals(null, report.values["route.a"])
        }

        private fun commonAsserts(report: AnalyticsRecord, dataReport: DataReport) {
            Assert.assertEquals(dataReport.timestamp, report.timestamp)
            Assert.assertEquals("onPress", report.values["event"])
            Assert.assertEquals(actionType, report.values["beagleAction"])
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
            //Given
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
            //When
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )

            //Then
            Assert.assertEquals(expectedDataReport, dataActionReport)
        }
    }

    @DisplayName("when preGenerateActionAnalyticsConfig")
    @Nested
    inner class ProGuardTest {
        @BeforeEach
        fun setup() {
            every { rootView.getScreenId() } returns ""
            every { origin.x } returns 300f
            every { origin.y } returns 400f
        }

        @DisplayName("Then should get name from annotation")
        @Test
        fun testPreGenerateActionAnalyticsConfigOfBeagleJsonActionShouldGetNameFromAnnotation() {
            //given
            val action = BeagleJsonActionWithName()

            //when
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )

            //then
            Assert.assertEquals("beagle:actioName", dataActionReport.actionType)
        }

        @DisplayName("Then should get name from class")
        @Test
        fun testPreGenerateActionAnalyticsConfigOfBeagleJsonActionShouldGetNameFromClass() {
            //given
            val action = BeagleJsonActionWithoutName()

            //when
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )

            //then
            Assert.assertEquals("beagle:beagleJsonActionWithoutName", dataActionReport.actionType)
        }

        @DisplayName("Then should get name from annotation")
        @Test
        fun testPreGenerateActionAnalyticsConfigOfRegisterActionShouldGetNameFromAnnotation() {
            //given
            val action = RegisterActionWithName()
            every { beagleSdk.registeredActions() } returns listOf(action::class.java as Class<Action>)

            //when
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )

            //then
            Assert.assertEquals("custom:actioName", dataActionReport.actionType)
        }

        @DisplayName("Then should get name from class")
        @Test
        fun testPreGenerateActionAnalyticsConfigOfRegisterActionShouldGetNameFromClass() {
            //given
            val action = RegisterActionWithoutName()
            every { beagleSdk.registeredActions() } returns listOf(action::class.java as Class<Action>)

            //when
            val dataActionReport = ActionReportFactory.preGenerateActionAnalyticsConfig(
                rootView,
                origin,
                action,
                analyticsValue = "onPress"
            )

            //then
            Assert.assertEquals("custom:registerActionWithoutName", dataActionReport.actionType)
        }
    }
}

@BeagleJson(name = "actionName")
internal class BeagleJsonActionWithName(override var analytics: ActionAnalyticsConfig? = null) : ActionAnalytics {
    override fun execute(rootView: RootView, origin: View) {

    }
}

@BeagleJson
internal class BeagleJsonActionWithoutName(override var analytics: ActionAnalyticsConfig? = null) : ActionAnalytics {
    override fun execute(rootView: RootView, origin: View) {

    }
}

@RegisterAction
internal class RegisterActionWithoutName(override var analytics: ActionAnalyticsConfig? = null) : ActionAnalytics {
    override fun execute(rootView: RootView, origin: View) {

    }
}

@RegisterAction(name = "actionName")
internal class RegisterActionWithName(override var analytics: ActionAnalyticsConfig? = null) : ActionAnalytics {
    override fun execute(rootView: RootView, origin: View) {

    }
}