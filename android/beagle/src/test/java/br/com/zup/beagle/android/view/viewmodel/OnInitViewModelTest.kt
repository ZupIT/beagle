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

package br.com.zup.beagle.android.view.viewmodel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Given OnInit ViewModel")
class OnInitViewModelTest {

    private val onInitViewModel = OnInitViewModel()

    @DisplayName("When set OnInit action status")
    @Nested
    inner class SetOnInitStatus {

        @DisplayName("Then should set to onInitStatusByComponent id the given value")
        @Test
        fun setOnInitActionStatus() {
            // Given
            val onInitiableComponentId = 10
            val onInitCalled = true

            // When
            onInitViewModel.setOnInitActionStatus(onInitiableComponentId, onInitCalled)
            val result = onInitViewModel.getOnInitActionStatus(onInitiableComponentId)

            // Then
            assertEquals(onInitCalled, result)
        }
    }

    @DisplayName("When get OnInit action status not found")
    @Nested
    inner class GetOnInitStatus {

        @DisplayName("Then should return false")
        @Test
        fun getOnInitActionStatus() {
            // Given
            val onInitiableComponentId = 10

            // When
            val result = onInitViewModel.getOnInitActionStatus(onInitiableComponentId)

            // Then
            assertFalse(result)
        }
    }
}
