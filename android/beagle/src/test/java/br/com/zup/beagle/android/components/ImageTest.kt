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

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.widget.ImageView
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.CoroutineTestRule
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Size
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

private val IMAGE_RES = RandomData.int()
private const val DEFAULT_URL = "http://teste.com/test.png"

class ImageViewRendererTest : BaseComponentTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val scope = CoroutineTestRule()

    private val imageView: RoundedImageView = mockk(relaxed = true, relaxUnitFun = true)
    private val scaleTypeSlot = slot<ImageView.ScaleType>()
    private val style = Style(size = Size(width = 100.unitReal(), height = 100.unitReal()))

    private lateinit var imageLocal: Image
    private lateinit var imageRemote: Image
    private val scaleType = ImageView.ScaleType.FIT_CENTER

    override fun setUp() {
        super.setUp()

        every { anyConstructed<ViewFactory>().makeImageView(rootView.getContext(), any()) } returns imageView
        every { beagleSdk.designSystem } returns mockk(relaxed = true)
        every { beagleSdk.designSystem!!.image(any()) } returns IMAGE_RES

        imageLocal = Image(ImagePath.Local("imageName"))
        imageRemote = Image(ImagePath.Remote(DEFAULT_URL, ImagePath.Local("imageName"))).applyStyle(style)
    }

    @Test
    fun build_should_return_a_image_view_instance_and_set_data_when_path_is_local() {
        // When
        val view = imageLocal.buildView(rootView)

        // Then
        assertTrue(view is ImageView)
    }

    @Test
    fun build_should_return_a_beagle_image_view_instance_and_set_data_when_path_is_remote() {
        //Given
        imageRemote = Image(ImagePath.Remote(""))

        // When
        val view = imageRemote.buildView(rootView)

        // Then
        assertTrue(view is ImageView)
    }

    @Test
    fun build_with_image_should_set_fit_center_when_content_mode_is_null_and_design_system_is_not_null() {
        // Given
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs
        imageLocal = imageLocal.copy(mode = ImageContentMode.FIT_CENTER)

        // When
        imageLocal.buildView(rootView)

        // Then
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
        verify(exactly = once()) { imageView.setImageResource(IMAGE_RES) }
    }

    @Test
    fun build_with_image_should_set_desired_scaleType_and_design_system_is_null() {
        // Given
        every { BeagleEnvironment.beagleSdk.designSystem } returns null
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs
        imageLocal = imageLocal.copy(mode = ImageContentMode.FIT_CENTER)

        // When
        imageLocal.buildView(rootView)

        // Then
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
        verify(exactly = 0) { imageView.setImageResource(IMAGE_RES) }
    }

    @Test
    fun build_should_return_a_ImageView_with_desired_scaleType() {
        // Given
        val scaleTypeSlot = slot<ImageView.ScaleType>()
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

        // When
        val view = imageRemote.buildView(rootView)

        // Then
        Assert.assertTrue(view is ImageView)
        Assert.assertEquals(scaleType, scaleTypeSlot.captured)
    }

    @Test
    fun build_should_setPlaceholder_when_imagePath_is_remote_and_placeholder_is_declared() {
        imageRemote.style = null

        // When
        imageRemote.buildView(rootView)

        // Then
        verify(exactly = once()) {
            imageView.setImageResource(any())
        }

    }

}