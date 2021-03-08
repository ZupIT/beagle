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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import br.com.zup.beagle.android.networking.RequestData
import br.com.zup.beagle.android.view.ServerDrivenActivity
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

private const val KEY = "FIRST_SCREEN_REQUEST_KEY"

@DisplayName("Given a Context")
@RunWith(RobolectricTestRunner::class)
internal class ServerDrivenFactoryKtTest {

    @DisplayName("When create new server driven intent Then should add parameter in bundle")
    @Test
    fun testCreateIntentWithCorrectBundle() {
        // Given
        val requestData = mockk<RequestData>()
        val context = ApplicationProvider.getApplicationContext<Context>()

        // When
        val result = context.newServerDrivenIntent<ServerDrivenActivity>(requestData)

        // Then
        val expected = Intent()
        val bundle = Bundle()
        bundle.putParcelable(KEY, requestData)
        expected.putExtras(bundle)

        assertEquals(expected.extras!!.size(), result.extras!!.size())
        assertEquals(expected.extras!!.get(KEY), result.extras!!.get(KEY))
    }
}