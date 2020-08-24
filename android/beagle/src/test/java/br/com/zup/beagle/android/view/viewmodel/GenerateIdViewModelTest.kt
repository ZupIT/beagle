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
import br.com.zup.beagle.android.testutil.getPrivateField
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.util.LinkedList
import kotlin.test.assertEquals

class GenerateIdViewModelTest {

    private lateinit var viewModel: GenerateIdViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(View::class)
        every { View.generateViewId() } returns 10
        viewModel = GenerateIdViewModel()
    }

    @Test
    fun `should create new view when call`() {
        // When
        viewModel.createIfNotExisting(1)

        // Then
        val views = viewModel.getPrivateField<MutableMap<Int, LocalView>>("views")
        assertEquals(views.size, 1)
        assertEquals(LocalView(), views[1])
    }

    @Test
    fun `should not create new view when call twice`() {
        // When
        viewModel.createIfNotExisting(1)
        viewModel.createIfNotExisting(1)

        // Then
        val views = viewModel.getPrivateField<MutableMap<Int, LocalView>>("views")
        assertEquals(1, views.size)
        assertEquals(LocalView(), views[1])
    }

    @Test
    fun `should change fields when set view created`() {
        // When
        viewModel.createIfNotExisting(1)
        viewModel.getViewId(1)
        viewModel.setViewCreated(1)

        // Then
        val views = viewModel.getPrivateField<MutableMap<Int, LocalView>>("views")
        val listGenerated = listOf(10)
        val expected = LocalView(ids = listGenerated.toMutableList(),
            temporaryIds = LinkedList(listGenerated), created = true)
        assertEquals(expected, views[1])
    }

    @Test
    fun `should generate id when all views not created`() {
        // When
        viewModel.createIfNotExisting(1)
        val result = viewModel.getViewId(1)

        // Then
        assertEquals(10, result)
        verify(exactly = 1) { View.generateViewId() }
    }

    @Test
    fun `should get local id when all views was created`() {
        // When
        viewModel.createIfNotExisting(1)
        viewModel.getViewId(1)
        viewModel.setViewCreated(1)
        val result = viewModel.getViewId(1)

        // Then
        assertEquals(10, result)
        verify(exactly = 1) { View.generateViewId() }
    }

    @Test
    fun `should throw exception when call get id without parent id existing`() {
        // When
        var actual: Exception? = null
        try {
            viewModel.getViewId(1)
        } catch (exception: Exception) {
            actual = exception
        }

        assertEquals(PARENT_ID_NOT_FOUND, actual!!.message)
    }

    @Test
    fun `should throw exception when set view created without parent id`() {
        // When
        var actual: Exception? = null
        try {
            viewModel.setViewCreated(1)
        } catch (exception: Exception) {
            actual = exception
        }

        assertEquals(PARENT_ID_NOT_FOUND, actual!!.message)
    }

    @Test
    fun `should throw exception when get id with list empty`() {
        // When
        var actual: Exception? = null
        try {
            viewModel.createIfNotExisting(1)
            viewModel.setViewCreated(1)
            viewModel.getViewId(1)
        } catch (exception: Exception) {
            actual = exception
        }

        assertEquals("temporary ids can't be empty", actual!!.message)
    }
}