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

import android.view.View
import br.com.zup.beagle.android.components.form.core.FormResult
import br.com.zup.beagle.android.components.form.core.FormSubmitter
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

class FormRemoteActionTest : BaseAsyncActionTest() {

    private val formSubmitCallbackSlot = slot<(formResult: FormResult) -> Unit>()
    private val formParamsSlot = slot<Map<String, String>>()
    private val view: View = mockk()

    @BeforeEach
    override fun setUp() {
        super.setUp()
        mockkConstructor(FormSubmitter::class)
        every {
            anyConstructed<FormSubmitter>().submitForm(any(), capture(formParamsSlot),
                capture(formSubmitCallbackSlot))
        } just Runs
    }

    @Test
    fun `GIVEN a FormRemoteAction WHEN executed THEN should call submitForm and his callback`() {
        // Given
        val resultListener: ResultListener = mockk()
        val action = FormRemoteAction("", FormMethodType.GET)
        action.formsValue = mapOf()
        action.resultListener = resultListener
        action.status.observeForever(observer)
        val formResult: FormResult.Success = mockk()
        every { resultListener(any()) } just Runs

        // When
        action.execute(mockk(), view)
        formSubmitCallbackSlot.captured(formResult)

        // Then
        verify(exactly = 1) { anyConstructed<FormSubmitter>().submitForm(action, action.formsValue, any()) }
        verify(exactly = 1) { resultListener(formResult) }
        assert(onActionFinishedWasCalled())
    }
}
