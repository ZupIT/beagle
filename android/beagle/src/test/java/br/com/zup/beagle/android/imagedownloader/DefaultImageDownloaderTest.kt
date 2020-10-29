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

package br.com.zup.beagle.android.imagedownloader

import android.widget.ImageView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.widget.RootView
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class DefaultImageDownloaderTest {

    private lateinit var defaultImageDownloader: DefaultImageDownloader
    private val imageView: ImageView = mockk(relaxed = true)
    private val rootView: RootView = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        mockkObject(BeagleEnvironment)
        every { BeagleEnvironment.beagleSdk } returns mockk(relaxed = true)
        defaultImageDownloader = DefaultImageDownloader()
    }

    @Test
    fun `GIVEN url WHEN download image THEN call post image`()  {
        // Given
        val url = "https://vitafelice.com.br/wp-content/uploads/2019/01/beagle.jpg"

        // When
        defaultImageDownloader.download(url, imageView, rootView)

        // Then
        verify(exactly = once()) { imageView.post(any()) }
    }
}