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

import android.view.View
import br.com.zup.beagle.android.exception.BeagleException
import br.com.zup.beagle.android.testutil.getPrivateField
import br.com.zup.beagle.android.utils.toAndroidId
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.LinkedList

class ListViewIdViewModelTest {

    private val listViewIdViewModel = ListViewIdViewModel()
    private val internalIdsByListId = listViewIdViewModel.getPrivateField<MutableMap<Int, LocalListView>>("internalIdsByListId")
    private val linkedListId = LinkedList<Int>().apply {
        add(20)
        add(30)
    }
    private val recyclerViewId = 10
    private val generatedViewId = 100

    @BeforeEach
    fun setUp() {
        mockkStatic(View::class)
        every { View.generateViewId() } returns generatedViewId
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(View::class)
    }

    @Test
    fun `GIVEN an empty recyclerViewId WHEN createSingleManagerByListViewId THEN should fail`() {
        // Given
        val recyclerViewId = View.NO_ID

        // When
        val exception = assertThrows<IllegalArgumentException> {
            listViewIdViewModel.createSingleManagerByListViewId(recyclerViewId)
        }

        // Then
        assertEquals(NO_ID_RECYCLER, exception.message)
    }

    @Test
    fun `GIVEN a new recyclerView id WHEN createSingleManagerByListViewId THEN should be added`() {
        // Given When
        val recyclerView = createManagerAndReturnLocalList()

        // Then
        assertNotNull(recyclerView)
    }

    @Test
    fun `GIVEN a previous inserted recyclerView id WHEN createSingleManagerByListViewId THEN should set completelyLoaded false`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.completelyLoaded = true

        // When
        listViewIdViewModel.createSingleManagerByListViewId(recyclerViewId)

        // Then
        assertFalse { recyclerView?.completelyLoaded ?: true }
    }

    @Test
    fun `GIVEN a previous loaded recyclerView WHEN createSingleManagerByListViewId THEN should markToReuse`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.temporaryIdsByAdapterPosition = mutableMapOf(1 to linkedListId)
        recyclerView?.idsByAdapterPosition?.set(2, linkedListId)

        // When
        listViewIdViewModel.createSingleManagerByListViewId(recyclerViewId, false)

        // Then
        assertTrue { recyclerView?.reused ?: false }
        assertEquals(recyclerView?.idsByAdapterPosition, recyclerView?.temporaryIdsByAdapterPosition)
    }

    @Test
    fun `GIVEN a reused recyclerView WHEN createSingleManagerByListViewId THEN should markToReuse`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.reused = true
        recyclerView?.temporaryIdsByAdapterPosition = mutableMapOf(1 to linkedListId)
        recyclerView?.idsByAdapterPosition?.set(2, linkedListId)

        // When
        listViewIdViewModel.createSingleManagerByListViewId(recyclerViewId)

        // Then
        assertTrue { recyclerView?.reused ?: false }
        assertEquals(recyclerView?.idsByAdapterPosition, recyclerView?.temporaryIdsByAdapterPosition)
    }

    @Test
    fun `GIVEN a previously loaded and reused recyclerView WHEN createSingleManagerByListViewId THEN should not markToReuse`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()

        // When
        listViewIdViewModel.createSingleManagerByListViewId(recyclerViewId)

        // Then
        assertFalse { recyclerView?.reused ?: true }
        assertTrue { recyclerView?.idsByAdapterPosition?.isEmpty() ?: false }
    }

    @Test
    fun `GIVEN an empty recyclerViewId WHEN setViewId THEN should fail`() {
        // Given
        val recyclerViewId = View.NO_ID
        val position = 0
        val viewId = 0

        // When
        val exception = assertThrows<IllegalArgumentException> {
            listViewIdViewModel.setViewId(recyclerViewId, position, viewId)
        }

        // Then
        assertEquals(NO_ID_RECYCLER, exception.message)
    }

    @Test
    fun `GIVEN a recyclerViewId not reused WHEN setViewId THEN should addIdToLocalListView`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.idsByAdapterPosition?.set(1, linkedListId)
        val position = 1
        val viewId = 40

        // When
        val resultId = listViewIdViewModel.setViewId(recyclerViewId, position, viewId)

        // Then
        assert(recyclerView?.idsByAdapterPosition?.get(position)?.last == viewId)
        assertEquals(viewId, resultId)
    }

    @Test
    fun `GIVEN a recyclerViewId not reused WHEN setViewId THEN should not add the same viewId`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.idsByAdapterPosition?.set(1, linkedListId)
        val position = 1
        val viewId = 30

        // When
        val resultId = listViewIdViewModel.setViewId(recyclerViewId, position, viewId)

        // Then
        assert(recyclerView?.idsByAdapterPosition?.get(position)?.size == 2)
        assertEquals(viewId, resultId)
    }

    @Test
    fun `GIVEN a recyclerViewId not reused and empty WHEN setViewId THEN should addIdToLocalListView`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        val position = 1
        val viewId = 30

        // When
        val resultId = listViewIdViewModel.setViewId(recyclerViewId, position, viewId)

        // Then
        assert(recyclerView?.idsByAdapterPosition?.get(position)?.last == 30)
        assertEquals(viewId, resultId)
    }

    @Test
    fun `GIVEN a reused and loaded recyclerViewId WHEN setViewId THEN should pollOrGenerateANewId`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.temporaryIdsByAdapterPosition?.set(1, linkedListId)
        recyclerView?.reused = true
        val first = linkedListId.first
        val position = 1
        val viewId = View.NO_ID

        // When
        val resultId = listViewIdViewModel.setViewId(recyclerViewId, position, viewId)

        // Then
        assertEquals(first, resultId)
    }

    @Test
    fun `GIVEN a reused and empty recyclerViewId WHEN setViewId THEN should pollOrGenerateANewId`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.reused = true
        val position = 1
        val viewId = View.NO_ID

        // When
        val resultId = listViewIdViewModel.setViewId(recyclerViewId, position, viewId)

        // Then
        assertEquals(generatedViewId, resultId)
        assert(recyclerView?.idsByAdapterPosition?.get(position)?.last == generatedViewId)
    }

    @Test
    fun `GIVEN a reused, empty and completelyLoaded recyclerViewId WHEN setViewId THEN should fail`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.reused = true
        recyclerView?.completelyLoaded = true
        val position = 1
        val viewId = View.NO_ID

        // When
        val exception = assertThrows<BeagleException> {
            listViewIdViewModel.setViewId(recyclerViewId, position, viewId)
        }

        // Then
        assertEquals("Temporary ids can't be empty", exception.message)
    }

    @Test
    fun `GIVEN a reused and empty recyclerViewId with id not found WHEN setViewId THEN should fail`() {
        // Given
        val position = 1
        val viewId = View.NO_ID

        // When
        val exception = assertThrows<BeagleException> {
            listViewIdViewModel.setViewId(recyclerViewId, position, viewId)
        }

        // Then
        assertEquals("The list id $recyclerViewId which this view in position $position belongs to, was not found", exception.message)
    }

    @Test
    fun `GIVEN an empty recyclerViewId WHEN getViewId THEN should fail`() {
        // Given
        val recyclerViewId = View.NO_ID
        val position = 0

        // When
        val exception = assertThrows<IllegalArgumentException> {
            listViewIdViewModel.getViewId(recyclerViewId, position)
        }

        // Then
        assertEquals(NO_ID_RECYCLER, exception.message)
    }

    @Test
    fun `GIVEN a not reused and empty recyclerViewId WHEN getViewId THEN should generateViewId`() {
        // Given
        createManagerAndReturnLocalList()
        val position = 1

        // When
        val resultId = listViewIdViewModel.getViewId(recyclerViewId, position)

        // Then
        assertEquals(generatedViewId, resultId)
    }

    @Test
    fun `GIVEN an empty recyclerViewId with suffix WHEN getViewId THEN should fail`() {
        // Given
        val recyclerViewId = View.NO_ID
        val position = 0
        val componentId = ""
        val itemSuffix = ""

        // When
        val exception = assertThrows<IllegalArgumentException> {
            listViewIdViewModel.getViewId(recyclerViewId, position, componentId, itemSuffix)
        }

        // Then
        assertEquals(NO_ID_RECYCLER, exception.message)
    }

    @Test
    fun `GIVEN a not reused and empty recyclerViewId with suffix WHEN getViewId THEN should generateViewId`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        val position = 1
        val componentId = "id"
        val itemSuffix = "suffix"
        val generatedId = "$componentId:$itemSuffix".toAndroidId()

        // When
        val resultId = listViewIdViewModel.getViewId(recyclerViewId, position, componentId, itemSuffix)

        // Then
        assertEquals(generatedId, resultId)
        assert(recyclerView?.idsByAdapterPosition?.get(position)?.last == generatedId)
    }

    @Test
    fun `GIVEN a recyclerView inside a viewBeingDestroyed WHEN prepareToReuseIds THEN should markToReuse`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()
        recyclerView?.idsByAdapterPosition?.set(1, linkedListId)
        val viewBeingDestroyed = mockk<View>(relaxed = true)
        every { viewBeingDestroyed.id } returns recyclerViewId

        // When
        listViewIdViewModel.prepareToReuseIds(viewBeingDestroyed)

        // Then
        assertTrue { recyclerView?.reused ?: false }
        assertEquals(recyclerView?.idsByAdapterPosition, recyclerView?.temporaryIdsByAdapterPosition)
    }

    @Test
    fun `GIVEN an empty recyclerViewId WHEN markHasCompletelyLoaded THEN should fail`() {
        // Given
        val recyclerViewId = View.NO_ID

        // When
        val exception = assertThrows<IllegalArgumentException> {
            listViewIdViewModel.markHasCompletelyLoaded(recyclerViewId)
        }

        // Then
        assertEquals(NO_ID_RECYCLER, exception.message)
    }

    @Test
    fun `GIVEN a recyclerViewId WHEN markHasCompletelyLoaded THEN should set completelyLoaded true`() {
        // Given
        val recyclerView = createManagerAndReturnLocalList()

        // When
        listViewIdViewModel.markHasCompletelyLoaded(recyclerViewId)

        // Then
        assertTrue { recyclerView?.completelyLoaded ?: false }
    }

    private fun createManagerAndReturnLocalList(): LocalListView? {
        listViewIdViewModel.createSingleManagerByListViewId(recyclerViewId)
        return internalIdsByListId[recyclerViewId]
    }
}
