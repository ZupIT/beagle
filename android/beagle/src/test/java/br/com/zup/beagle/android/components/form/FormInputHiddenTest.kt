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

import android.view.View
import br.com.zup.beagle.android.components.BaseComponentTest
import br.com.zup.beagle.android.extensions.once
import io.mockk.verify
import org.junit.Test

class FormInputHiddenTest : BaseComponentTest() {

    private lateinit var formInputHidden: FormInputHidden

    override fun setUp() {
        super.setUp()

        formInputHidden = FormInputHidden("", "")
    }

    @Test
    fun `should make vie gone when build view`() {
        // WHEN
        formInputHidden.buildView(rootView)

        // THEN
        verify(exactly = once()) { beagleFlexView.visibility = View.GONE }
        verify(exactly = once()) { beagleFlexView.tag = formInputHidden }
    }
}
