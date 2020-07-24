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

package br.com.zup.beagle.android.components.utils

import android.app.Activity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.utils.BaseTest
import br.com.zup.beagle.android.view.custom.BeagleView
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Test

class ViewExtensionsKtTest : BaseTest() {

    @MockK
    private lateinit var activity: AppCompatActivity

    @RelaxedMockK
    private lateinit var beagleView: BeagleView

    @RelaxedMockK
    private lateinit var viewGroup: ViewGroup

    @RelaxedMockK
    private lateinit var inputMethodManager: InputMethodManager

    override fun setUp() {
        super.setUp()

        every { viewGroup.context } returns activity
        every { activity.getSystemService(Activity.INPUT_METHOD_SERVICE) } returns inputMethodManager
    }


    @Test
    fun hideKeyboard_should_call_hideSoftInputFromWindow_with_currentFocus() {
        // Given
        every { activity.currentFocus } returns beagleView

        // When
        viewGroup.hideKeyboard()

        // Then
        verify(exactly = once()) { inputMethodManager.hideSoftInputFromWindow(any(), 0) }
    }
}