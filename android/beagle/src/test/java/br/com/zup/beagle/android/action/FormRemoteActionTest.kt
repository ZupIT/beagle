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

package br.com.zup.beagle.android.action

import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.components.form.core.FormResult
import br.com.zup.beagle.android.components.form.core.FormSubmitter
import br.com.zup.beagle.android.extensions.once
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

class FormRemoteActionTest : BaseTest() {

    private val formSubmitCallbackSlot = slot<(formResult: FormResult) -> Unit>()
    private val formParamsSlot = slot<Map<String, String>>()

    override fun setUp() {
        super.setUp()
        mockkConstructor(FormSubmitter::class)
        every {
            anyConstructed<FormSubmitter>().submitForm(any(), capture(formParamsSlot),
                capture(formSubmitCallbackSlot))
        } just Runs
    }

    @Test
    fun `should execute action`() {
        // Given
        val resultListener: ResultListener = mockk()
        val action = FormRemoteAction("", FormMethodType.GET)
        action.formsValue = mapOf()
        action.resultListener = resultListener
        val formResult: FormResult.Success = mockk()
        every { resultListener(any()) } just Runs

        // When
        action.execute(mockk())
        formSubmitCallbackSlot.captured(formResult)


        // Then
        verify(exactly = once()) {
            anyConstructed<FormSubmitter>().submitForm(action, action.formsValue, any())
        }
        verify(exactly = once()) { resultListener(formResult) }
    }
}