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

package br.com.zup.beagle.android.mockdata

import android.view.ViewGroup
import br.com.zup.beagle.R
import br.com.zup.beagle.android.context.ContextBinding
import br.com.zup.beagle.android.testutil.RandomData
import io.mockk.*

fun createViewForContext(parentView: ViewGroup? = null): ViewGroup {
    return mockk {
        val viewId = RandomData.int()
        val contextBindingSlot = slot<ContextBinding>()
        every { id } returns viewId
        every { parent } returns parentView
        every { setTag(R.id.beagle_context_view, capture(contextBindingSlot)) } just Runs
        every { getTag(R.id.beagle_context_view) } answers {
            if (contextBindingSlot.isCaptured) {
                contextBindingSlot.captured
            } else {
                null
            }
        }
        every { setTag(R.id.beagle_context_view_parent, any()) } just Runs
        every { getTag(R.id.beagle_context_view_parent) } answers { null }
    }
}
