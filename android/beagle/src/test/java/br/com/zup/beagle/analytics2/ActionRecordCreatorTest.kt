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
import br.com.zup.beagle.android.action.Navigate
import br.com.zup.beagle.android.action.Route
import br.com.zup.beagle.android.components.Button
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.core.IdentifierComponent
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

internal class ActionRecordCreatorTest : BaseTest() {

    private val origin: View = mockk()
    private val serverDrivenComponent : WidgetView = mockk()
    @Test
    fun test() {
        //GIVEN
        val action: ActionAnalytics = Navigate.PushView(route = Route.Remote(url = "teste"), type = "beagle:PushView")
        every { rootView.getScreenId() } returns "/screen"
        val hashMapExpected = hashMapOf(
            "screen" to "/screen",
            "event" to "onPress",
            "route.url" to "teste",
            "route.shouldPrefetch" to false,
            "beagleAction" to "beagle:PushView",
            "component" to hashMapOf<String, Any>(
                "id" to "id",
                "type" to "beagle:Button",
                "position" to hashMapOf("x" to 350f, "y" to 200f)
            )
        )

        //WHEN
        val report = ActionRecordCreator.createRecord(
            rootView,
            origin,
            ActionAnalyticsConfig(enable = true, attributes = listOf("route.url", "route.shouldPrefetch")),
            action,
            AnalyticsHandleEvent(serverDrivenComponent,"onPress")
        )
        every { (serverDrivenComponent as IdentifierComponent).id } returns "id"
        every { (serverDrivenComponent as WidgetView).beagleType } returns "beagle:Button"
        every { origin.x } returns 350f
        every { origin.y } returns 200f

        //THEN
        Assert.assertEquals("android", report.platform)
        Assert.assertEquals("action", report.type)
        Assert.assertEquals(hashMapExpected, report.attributes)
    }
}