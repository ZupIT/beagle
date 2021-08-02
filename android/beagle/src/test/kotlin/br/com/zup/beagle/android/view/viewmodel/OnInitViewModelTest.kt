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

import br.com.zup.beagle.android.testutil.getPrivateField
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

        @DisplayName("Then should set to onInitStatusByViewId id the onInitCalled value")
        @Test
        fun setOnInitCalled() {
            // Given
            val onInitiableViewId = 10
            val onInitCalled = true
            val onInitFinished = false
            val status = onInitViewModel.getPrivateField<MutableMap<Int, OnInitStatus>>("onInitStatusByViewId")

            // When
            onInitViewModel.setOnInitCalled(onInitiableViewId, onInitCalled)

            // Then
            assertEquals(onInitCalled, status[onInitiableViewId]?.onInitCalled)
            assertEquals(onInitFinished, status[onInitiableViewId]?.onInitFinished)
        }

        @DisplayName("Then should set to onInitStatusByViewId id the onInitCalled value with previous value")
        @Test
        fun setOnInitCalledWithPreviousValue() {
            // Given
            val onInitiableViewId = 10
            val onInitCalled = true
            val onInitFinished = true
            val status = onInitViewModel.getPrivateField<MutableMap<Int, OnInitStatus>>("onInitStatusByViewId")
            status[onInitiableViewId] = OnInitStatus(onInitFinished = onInitFinished)

            // When
            onInitViewModel.setOnInitCalled(onInitiableViewId, onInitCalled)

            // Then
            assertEquals(onInitCalled, status[onInitiableViewId]?.onInitCalled)
            assertEquals(onInitFinished, status[onInitiableViewId]?.onInitFinished)
        }

        @DisplayName("Then should set to onInitStatusByViewId id the onInitFinished value")
        @Test
        fun setOnInitFinished() {
            // Given
            val onInitiableViewId = 10
            val onInitFinished = true
            val onInitCalled = false
            val status = onInitViewModel.getPrivateField<MutableMap<Int, OnInitStatus>>("onInitStatusByViewId")

            // When
            onInitViewModel.setOnInitFinished(onInitiableViewId, onInitFinished)

            // Then
            assertEquals(onInitFinished, status[onInitiableViewId]?.onInitFinished)
            assertEquals(onInitCalled, status[onInitiableViewId]?.onInitCalled)
        }

        @DisplayName("Then should set to onInitStatusByViewId id the onInitFinished value with previous value")
        @Test
        fun setOnInitFinishedWithPreviousValue() {
            // Given
            val onInitiableViewId = 10
            val onInitFinished = true
            val onInitCalled = true
            val status = onInitViewModel.getPrivateField<MutableMap<Int, OnInitStatus>>("onInitStatusByViewId")
            status[onInitiableViewId] = OnInitStatus(onInitCalled = onInitCalled)

            // When
            onInitViewModel.setOnInitFinished(onInitiableViewId, onInitFinished)

            // Then
            assertEquals(onInitFinished, status[onInitiableViewId]?.onInitFinished)
            assertEquals(onInitCalled, status[onInitiableViewId]?.onInitCalled)
        }
    }

    @DisplayName("When get OnInit action status not found")
    @Nested
    inner class GetOnInitCalledStatus {

        @DisplayName("Then should return false")
        @Test
        fun isOnInitCalled() {
            // Given
            val onInitiableViewId = 10

            // When
            val result = onInitViewModel.isOnInitCalled(onInitiableViewId)

            // Then
            assertFalse(result)
        }

        @DisplayName("Then should return false")
        @Test
        fun isOnInitFinished() {
            // Given
            val onInitiableViewId = 10

            // When
            val result = onInitViewModel.isOnInitFinished(onInitiableViewId)

            // Then
            assertFalse(result)
        }
    }

    @DisplayName("When onInitFinished is false")
    @Nested
    inner class MarkToRerun {

        @DisplayName("Then should set onInitCalled to false")
        @Test
        fun markToRerun() {
            // Given
            val onInitiableViewId = 10
            val onInitFinished = false
            val onInitCalled = false
            val status = onInitViewModel.getPrivateField<MutableMap<Int, OnInitStatus>>("onInitStatusByViewId")
            status[onInitiableViewId] = OnInitStatus(onInitCalled = true, onInitFinished = onInitFinished)

            // When
            onInitViewModel.markToRerun()

            // Then
            assertEquals(onInitCalled, status[onInitiableViewId]?.onInitCalled)
        }

        @DisplayName("Then should not set onInitCalled to false if finished")
        @Test
        fun markToRerunOnlyForNotFinished() {
            // Given
            val onInitiableViewId = 10
            val onInitFinished = true
            val onInitCalled = true
            val status = onInitViewModel.getPrivateField<MutableMap<Int, OnInitStatus>>("onInitStatusByViewId")
            status[onInitiableViewId] = OnInitStatus(onInitCalled = onInitCalled, onInitFinished = onInitFinished)

            // When
            onInitViewModel.markToRerun()

            // Then
            assertEquals(onInitCalled, status[onInitiableViewId]?.onInitCalled)
        }
    }
}
