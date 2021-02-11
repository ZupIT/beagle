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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


@DisplayName("Given a Data Report")
internal class DataReportTest : BaseTest() {

    @DisplayName("When create")
    @Nested
    inner class Create {

        @Test
        @DisplayName("Then should create the timestamp with correct time")
        fun testWhenCreateADataReportTestThenShouldGetTheTimestampCorrectly() {
            //Given
            val time1 = System.currentTimeMillis()
            val dataReport = object : DataReport() {
                override fun report(analyticsConfig: AnalyticsConfig): AnalyticsRecord? {
                    return null
                }
            }
            val time2 = System.currentTimeMillis()
            assertTrue(dataReport.timestamp >= time1)
            assertTrue(dataReport.timestamp <= time2)
        }
    }
}
