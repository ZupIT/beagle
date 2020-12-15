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

import android.app.Application
import android.text.TextUtils
import android.view.Gravity
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.zup.beagle.R
import br.com.zup.beagle.android.components.layout.NavigationBar
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(AndroidJUnit4::class)
class ToolbarTextManagerTest {

    private val toolbarTextManager = ToolbarTextManager
    val application = ApplicationProvider.getApplicationContext() as Application
    private val navigationBarMock = mockk<NavigationBar>()
    private val textAppearanceMock = 1

    @Test
    fun `Given a title when I call the title generator then check if the id passed corresponds to the standard id of the textView`() {
        // GIVEN
        every { navigationBarMock.title } returns "title"

        // WHEN
        val generatedTextView = toolbarTextManager.generateTitle(application, navigationBarMock, textAppearanceMock)

        // THEN
        Assertions.assertEquals(generatedTextView.id, R.id.beagle_toolbar_text)
    }

    @Test
    fun `Given a title when it is called or title manager, check if the layout passed corresponds to the layout defined for textView`() {
        // GIVEN
        every { navigationBarMock.title } returns "title"

        // WHEN
        val generatedTextView = toolbarTextManager.generateTitle(application, navigationBarMock, textAppearanceMock, R.id.beagle_toolbar_text)

        // THEN
        Assertions.assertEquals(generatedTextView.gravity, Gravity.CENTER)
        Assertions.assertEquals(generatedTextView.maxLines, 1)
        Assertions.assertEquals(generatedTextView.ellipsize, TextUtils.TruncateAt.END)
    }

    @Test
    fun `Given a title when you are called or title manager, check if the title passed corresponds to the title defined for the textView`() {
        //  GIVEN
        every { navigationBarMock.title } returns "title"

        // WHEN
        val generatedTextView = toolbarTextManager.generateTitle(application, navigationBarMock, textAppearanceMock, R.id.beagle_toolbar_text)

        // THEN
        Assertions.assertEquals(generatedTextView.text, "title")
    }
}
