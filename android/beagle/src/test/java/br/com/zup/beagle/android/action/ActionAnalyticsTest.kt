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

package br.com.zup.beagle.android.action

import android.view.View
import br.com.zup.beagle.analytics2.ActionAnalyticsConfig
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given an action analytics")
internal class ActionAnalyticsTest : BaseTest() {

    @DisplayName("When call execute from Action")
    @Nested
    inner class CallActionExecute{

        @DisplayName("Then should call execute passing originComponent as null")
        @Test
        fun testExecuteActionShouldCallExecutePassingOriginCOmponentAsNull(){
            //given
            val action = FakeActionAnalytics()
            val origin : View = mockk()
            //when
            action.execute(rootView, origin)

            //then
            assertEquals(rootView,action.rootView)
            assertEquals(origin,action.origin)
            assertEquals(null,action.originComponent)
        }
    }

}

internal class FakeActionAnalytics : ActionAnalytics() {

    override var analytics: ActionAnalyticsConfig? = null
    var rootView: RootView? = null
    var origin: View? = null
    var originComponent: ServerDrivenComponent? = null


    override fun execute(rootView: RootView, origin: View, originComponent: ServerDrivenComponent?) {
        this.rootView = rootView
        this.origin = origin
        this.originComponent = originComponent
    }

}
