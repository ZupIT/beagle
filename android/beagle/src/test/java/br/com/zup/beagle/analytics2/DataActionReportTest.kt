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
import br.com.zup.beagle.android.action.ActionAnalytics
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given DataActionReport")
internal class DataActionReportTest : BaseTest() {

    @DisplayName("When report")
    @Nested
    inner class Report {

        @Test
        @DisplayName("Then should call AnalyticsService.createActionRecord")
        fun testReportCallActionReport() {
            //GIVEN
            mockkObject(AnalyticsService)
            val action = mockk<ActionAnalytics>()
            val dataActionReport = DataActionReport(
                attributes = hashMapOf(),
                action = action,
                actionType = ""
            )
            every { AnalyticsService.reportActionIfShould(dataActionReport) } just Runs

            //WHEN
            dataActionReport.report()

            //THEN
            verify(exactly = 1) { AnalyticsService.reportActionIfShould(dataActionReport) }
        }
    }
}
