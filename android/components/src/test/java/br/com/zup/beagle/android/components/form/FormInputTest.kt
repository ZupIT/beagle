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

package br.com.zup.beagle.android.components.form

import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.extensions.once
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class FormInputTest : BaseComponentTest() {

    private val inputWidget: InputWidget = mockk(relaxed = true)

    private lateinit var formInput: FormInput

    override fun setUp() {
        super.setUp()

        formInput = FormInput("", child = inputWidget)
    }

    @Test
    fun `should make child when build view`() {
        // WHEN
        val actual = formInput.buildView(rootView)

        // THEN
        assertEquals(view, actual)
    }

    @Test
    fun `should set tag when build view`() {
        // WHEN
        formInput.buildView(rootView)

        // THEN
        verify(exactly = once()) { view.tag = formInput }
    }
}