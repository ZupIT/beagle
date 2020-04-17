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

package br.com.zup.beagle.engine.renderer.ui

import android.content.Context
import android.widget.ImageView
import br.com.zup.beagle.BaseTest
import br.com.zup.beagle.engine.mapper.ViewMapper
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.extensions.once
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.testutil.RandomData
import br.com.zup.beagle.view.BeagleImageView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.ui.Image
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

class ImageViewRendererTest : BaseTest() {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var viewMapper: ViewMapper
    @RelaxedMockK
    private lateinit var imageView: BeagleImageView
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var rootView: RootView
    @RelaxedMockK
    private lateinit var image: Image

    private val scaleTypeSlot = slot<ImageView.ScaleType>()

    @InjectMockKs
    private lateinit var imageViewRenderer: ImageViewRenderer

    override fun setUp() {
        super.setUp()

        every { rootView.getContext() } returns context
        every { viewFactory.makeImageView(context) } returns imageView
        every { BeagleEnvironment.beagleSdk.designSystem?.image(any()) } returns IMAGE_RES
    }

    @Test
    fun build_should_return_a_image_view_instance_and_set_data() {
        // Given
        every { viewMapper.toScaleType(any()) } returns mockk()

        // When
        val view = imageViewRenderer.build(rootView)

        // Then
        assertTrue(view is ImageView)
    }

    @Test
    fun build_with_image_should_set_fit_center_when_content_mode_is_null_and_design_system_is_not_null() {
        // Given
        val scaleType = ImageView.ScaleType.FIT_CENTER
        every { image.contentMode } returns null
        every { viewMapper.toScaleType(any()) } returns scaleType
        every { image.name } returns "imageName"
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

        // When
        imageViewRenderer.build(rootView)

        // Then
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
        verify(exactly = once()) { imageView.setImageResource(IMAGE_RES) }
    }

    @Test
    fun build_with_image_should_set_desired_scaleType_and_design_system_is_null() {
        // Given
        val scaleType = ImageView.ScaleType.CENTER_CROP
        every { image.contentMode } returns ImageContentMode.CENTER_CROP
        every { viewMapper.toScaleType(any()) } returns scaleType
        every { BeagleEnvironment.beagleSdk.designSystem } returns null
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

        // When
        imageViewRenderer.build(rootView)

        // Then
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
        verify(exactly = 0) { imageView.setImageResource(IMAGE_RES) }
    }
}
