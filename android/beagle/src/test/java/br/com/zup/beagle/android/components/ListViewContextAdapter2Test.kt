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

package br.com.zup.beagle.android.components

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import br.com.zup.beagle.android.context.ContextBinding
import br.com.zup.beagle.android.context.ContextData
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.generateViewModelInstance
import br.com.zup.beagle.android.utils.getContextBinding
import br.com.zup.beagle.android.utils.setContextData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.view.viewmodel.ScreenContextViewModel
import br.com.zup.beagle.core.ServerDrivenComponent
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Test


class ListViewContextAdapter2Test : BaseComponentTest() {

    private val template: ServerDrivenComponent = mockk()
    private val viewFactory: ViewFactory = mockk(relaxed = true)
    private lateinit var adapter: ListViewContextAdapter2
    private val parent : ViewGroup = mockk()
    private val viewMocked : View = mockk(relaxed = true)

    private val position = 0
    private val holder = ContextViewHolderTwo(viewMocked)


    override fun setUp() {
        super.setUp()
        adapter = ListViewContextAdapter2(
            template = template,
            viewFactory = viewFactory,
            orientation = RecyclerView.VERTICAL,
            rootView = rootView,
            listItems = arrayListOf("test")
        )
        mockkStatic("br.com.zup.beagle.android.utils.ViewExtensionsKt")
        every { beagleFlexView.setContextData(any()) } just Runs
    }

    @Test
    fun getItemViewType_should_return_position() {
        // Given

        // When
        val result = adapter.getItemViewType(position)

        // Then
        assertEquals(position, result)
    }

    @Test
    fun onCreateViewHolder_should_call_setContextData() {
        // Given

        // When
        val result = adapter.onCreateViewHolder(parent, position)

        // Then
        verify(exactly = once()) { result.itemView.setContextData(ContextData("item", "test")) }
    }

    @Test
    fun onCreateViewHolder_should_call_addServerDrivenComponent() {
        // Given

        // When
        val result = adapter.onCreateViewHolder(parent, position)

        // Then
        verify(exactly = once()) { (result.itemView as BeagleFlexView).addServerDrivenComponent(template, rootView) }
    }

    @Test
    fun onCreateViewHolder_should_call_linkBindingToContext() {
        // Given
        val viewModel = commonViewModelMock()
        every { viewModel.linkBindingToContext() } just Runs

        // When
        adapter.onCreateViewHolder(parent, position)

        // Then
        verify(exactly = once()) { viewModel.linkBindingToContext() }
    }

    @Test
    fun onBindViewHolder_should_call_setContextData() {
        // Given

        // When
        adapter.onBindViewHolder(holder, position)

        // Then
        verify(exactly = once()) { viewMocked.setContextData(ContextData("item", "test")) }
    }

    @Test
    fun onBindViewHolder_should_call_notifyBindingChanges() {
        // Given
        val viewModel = commonViewModelMock()
        every { viewModel.notifyBindingChanges(any()) } just Runs

        val contextBinding : ContextBinding = mockk(relaxed = true)
        every { viewMocked.getContextBinding() } returns contextBinding

        // When
        adapter.onBindViewHolder(holder, position)

        // Then
        verify(exactly = once()) { viewModel.notifyBindingChanges(contextBinding) }
    }

    private fun commonViewModelMock() : ScreenContextViewModel{
        mockkStatic("br.com.zup.beagle.android.utils.RootViewExtensionsKt")
        val viewModel : ScreenContextViewModel = mockk(relaxed = true, relaxUnitFun = true)
        mockkConstructor(ViewModelProvider::class)
        every{anyConstructed< ViewModelProvider>().get(ScreenContextViewModel::class.java) } returns viewModel
        return viewModel
    }
}