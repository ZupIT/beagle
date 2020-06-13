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

import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.widget.RootView
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ActionExecutorTest {

    @MockK
    private lateinit var rootView: RootView
    @MockK
    private lateinit var action: Action

    @InjectMockKs
    private lateinit var actionExecutor: ActionExecutor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `doAction with action should call execute`() {
        // Given
        every { action.execute(rootView) } just Runs

        // When
        actionExecutor.doAction(rootView, action)

        // Then
        verify(exactly = once()) { action.execute(rootView) }
    }
}
