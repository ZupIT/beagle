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

package br.com.zup.beagle.android.components

import android.widget.ImageView
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.ImageContentMode
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertTrue

private val IMAGE_RES = RandomData.int()

class ImageViewRendererTest : BaseComponentTest() {

    private val imageView: RoundedImageView = mockk(relaxed = true)

    private val scaleTypeSlot = slot<ImageView.ScaleType>()

    private lateinit var image: Image

    override fun setUp() {
        super.setUp()

        every { anyConstructed<ViewFactory>().makeImageView(rootView.getContext()) } returns imageView
        every { BeagleEnvironment.beagleSdk.designSystem?.image(any()) } returns IMAGE_RES

        image = Image("")
    }

    @Test
    fun build_should_return_a_image_view_instance_and_set_data() {
        // When
        val view = image.buildView(rootView)

        // Then
        assertTrue(view is ImageView)
    }

    @Test
    fun build_with_image_should_set_fit_center_when_content_mode_is_null_and_design_system_is_not_null() {
        // Given
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs
        image = image.copy("imageName", ImageContentMode.FIT_CENTER)

        // When
        image.buildView(rootView)

        // Then
        val scaleType = ImageView.ScaleType.FIT_CENTER
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
        verify(exactly = once()) { imageView.setImageResource(IMAGE_RES) }
    }

    @Test
    fun build_with_image_should_set_desired_scaleType_and_design_system_is_null() {
        // Given
        every { BeagleEnvironment.beagleSdk.designSystem } returns null
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs
        image = image.copy("", ImageContentMode.CENTER_CROP)

        // When
        image.buildView(rootView)

        // Then
        val scaleType = ImageView.ScaleType.CENTER_CROP
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
        verify(exactly = 0) { imageView.setImageResource(IMAGE_RES) }
    }
}