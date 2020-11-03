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

package br.com.zup.beagle.android.utils

import android.view.View
import br.com.zup.beagle.android.components.Text
import br.com.zup.beagle.android.components.layout.Container
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.viewmodel.GenerateIdViewModel
import br.com.zup.beagle.android.view.viewmodel.ListViewIdViewModel
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.ext.setId
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GenerateIdManagerTest {

    private val rootView = mockk<RootView>(relaxed = true)
    private val generateIdViewModel = mockk<GenerateIdViewModel>(relaxed = true)
    private val listViewIdViewModel = mockk<ListViewIdViewModel>(relaxed = true)
    private val view = mockk<BeagleFlexView>()
    private val generatedId = 1
    private lateinit var generateIdManager: GenerateIdManager

    @BeforeEach
    fun setUp() {
        mockkStatic(View::class)
        every { View.generateViewId() } returns generatedId
        every { rootView.generateViewModelInstance<GenerateIdViewModel>() } returns generateIdViewModel
        every { rootView.generateViewModelInstance<ListViewIdViewModel>() } returns listViewIdViewModel

        generateIdManager = GenerateIdManager(rootView, generateIdViewModel, listViewIdViewModel)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(View::class)
    }

    @Test
    fun `GIVEN a rootView with parentId WHEN createSingleManagerByRootViewId is called THEN should call createIfNotExisting`() {
        // Given
        val parentId = 10
        every { rootView.getParentId() } returns parentId

        // When
        generateIdManager.createSingleManagerByRootViewId()

        // Then
        verify(exactly = 1) { generateIdViewModel.createIfNotExisting(parentId) }
    }

    @Test
    fun `GIVEN a rootView with parentId WHEN onViewDetachedFromWindow is called THEN should call setViewCreated and prepareToReuseIds`() {
        // Given
        val parentId = 10
        every { rootView.getParentId() } returns parentId

        // When
        generateIdManager.onViewDetachedFromWindow(view)

        // Then
        verify(exactly = 1) { generateIdViewModel.setViewCreated(parentId) }
        verify(exactly = 1) { listViewIdViewModel.prepareToReuseIds(view) }
    }

    @Test
    fun `GIVEN a component it is not an IdentifierComponent WHEN manageId THEN should not generateViewId`() {
        // Given
        val component = mockk<ServerDrivenComponent>()

        // When
        generateIdManager.manageId(component, view)

        // Then
        verify(exactly = 0) { View.generateViewId() }
    }

    @Test
    fun `GIVEN a IdentifierComponent with id WHEN manageId THEN should not generateViewId`() {
        // Given
        val component = Container(listOf()).setId("stub")

        // When
        generateIdManager.manageId(component, view)

        // Then
        verify(exactly = 0) { View.generateViewId() }
    }

    @Test
    fun `GIVEN a IdentifierComponent without id and isAutoGenerateIdEnabled WHEN manageId THEN should getViewId`() {
        // Given
        val component = Container(listOf())
        every { view.isAutoGenerateIdEnabled() } returns true
        val recoveryId = 1
        every { generateIdViewModel.getViewId(any()) } returns recoveryId

        // When
        generateIdManager.manageId(component, view)

        // Then
        verify(exactly = 1) { generateIdViewModel.getViewId(rootView.getParentId()) }
        assertEquals(recoveryId.toString(), component.id)
    }

    @Test
    fun `GIVEN a IdentifierComponent without id and isAutoGenerateIdEnabled WHEN manageId THEN should try getViewId`() {
        // Given
        val component = Container(listOf())
        every { view.isAutoGenerateIdEnabled() } returns true
        every { generateIdViewModel.getViewId(any()) } throws Exception()

        // When
        generateIdManager.manageId(component, view)

        // Then
        verify(exactly = 1) { View.generateViewId() }
        assertEquals(generatedId.toString(), component.id)
    }

    @Test
    fun `GIVEN a IdentifierComponent without id and auto generate id disabled WHEN manageId THEN should markEachNestedComponentAsNoIdIfNeeded`() {
        // Given
        val componentWithoutId = Text("stub")
        val children = listOf<ServerDrivenComponent>(componentWithoutId)
        val component = Container(children)
        every { view.isAutoGenerateIdEnabled() } returns false

        // When
        generateIdManager.manageId(component, view)

        // Then
        assertEquals(COMPONENT_NO_ID, component.id)
        assertEquals(COMPONENT_NO_ID, componentWithoutId.id)
    }

    @Test
    fun `GIVEN a IdentifierComponent without id and auto generate id disabled WHEN manageId THEN should keep previous ids`() {
        // Given
        val previousId = "id"
        val componentWithId = Text("stub").setId(previousId)
        val children = listOf<ServerDrivenComponent>(componentWithId)
        val component = Container(children)
        every { view.isAutoGenerateIdEnabled() } returns false

        // When
        generateIdManager.manageId(component, view)

        // Then
        assertEquals(COMPONENT_NO_ID, component.id)
        assertEquals(previousId, componentWithId.id)
    }
}
