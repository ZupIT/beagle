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
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.testutil.RandomData
import br.com.zup.beagle.android.utils.dp
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.ext.applyStyle
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.core.Size
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

private val IMAGE_RES = RandomData.int()
private const val DEFAULT_URL = "http://teste.com/test.png"

@DisplayName("Given an Image")
internal class ImageTest : BaseComponentTest() {

    private val imageView: RoundedImageView = mockk(relaxed = true, relaxUnitFun = true)
    private val scaleTypeSlot = slot<ImageView.ScaleType>()
    private val style = Style(size = Size(width = 100.unitReal(), height = 100.unitReal()),
        cornerRadius = CornerRadius(radius = 10.0))

    private lateinit var imageLocal: Image
    private lateinit var imageRemote: Image
    private val scaleType = ImageView.ScaleType.FIT_CENTER

    @BeforeEach
    override fun setUp() {
        super.setUp()

        mockkStatic("br.com.zup.beagle.android.utils.NumberExtensionsKt")

        every { ViewFactory.makeImageView(rootView.getContext(), any()) } returns imageView
        every { beagleSdk.designSystem } returns mockk(relaxed = true)
        every { beagleSdk.designSystem!!.image(any()) } returns IMAGE_RES
        every { 10.0.dp() } returns 20.0

        imageLocal = Image(ImagePath.Local("imageName"))
        imageRemote = Image(ImagePath.Remote(DEFAULT_URL, ImagePath.Local("imageName"))).applyStyle(style)
    }

    @DisplayName("When an imageView is built")
    @Nested
    inner class BuildingImageViews {

        @Test
        @DisplayName("Then it should return a imageView if imagePath is local")
        fun testsIfViewIsBuiltAsImageViewWhenImagePathIsLocal() {
            // When
            val view = imageLocal.buildView(rootView)

            // Then
            assertTrue(view is ImageView)
        }

        @Test
        @DisplayName("Then it should return a imageView if imagePath is remote")
        fun testsIfViewIsBuiltAsImageViewWhenImagePathIsRemote() {
            // Given
            imageRemote = Image(ImagePath.Remote(""))

            // When
            val view = imageRemote.buildView(rootView)

            // Then
            assertTrue(view is ImageView)
        }

        @Test
        @DisplayName("Then it should clear drawable if placeholder is null")
        fun testsIfClearDrawableWhenPlaceholderIsNull() {
            // Given
            imageRemote = Image(ImagePath.Remote(url = ""))

            // When
            val view = imageRemote.buildView(rootView)

            // Then
            verify(exactly = 1) { (view as ImageView).setImageDrawable(null) }
        }

        @Test
        @DisplayName("Then it should not clear drawable with placeholder")
        fun testsIfDrawableNullWasNotCalledWithPlaceholder() {
            // Given
            imageRemote = Image(ImagePath.Remote(url = "", placeholder = ImagePath.Local("imageName")))

            // When
            val view = imageRemote.buildView(rootView)

            // Then
            verify(exactly = 0) { (view as ImageView).setImageDrawable(null) }
        }
    }

    @DisplayName("When setting imageView properties")
    @Nested
    inner class SettingProperties {

        @Test
        @DisplayName("Then scaleType should be FIT_CENTER if content mode is NULL and design system is NOT_NULL")
        fun testsIfTheScaleTypeSetIsFitCenter() {
            // Given
            every { imageView.scaleType = capture(scaleTypeSlot) } just Runs
            imageLocal = imageLocal.copy(mode = ImageContentMode.FIT_CENTER)

            // When
            imageLocal.buildView(rootView)

            // Then
            assertEquals(scaleType, scaleTypeSlot.captured)
            verify(exactly = 1) { imageView.setImageResource(IMAGE_RES) }
        }

        @Test
        @DisplayName("Then adjustViewBounds should be TRUE if there is size")
        fun testsIfTheAdjustViewBoundsIsSetTrue() {
            // Given
            val image = imageLocal.applyStyle(Style(size = Size(width = 100.unitReal())))
            val adjustViewBoundsSlot = slot<Boolean>()
            every { imageView.adjustViewBounds = capture(adjustViewBoundsSlot) } just Runs

            // When
            image.buildView(rootView)

            // Then
            assertEquals(true, adjustViewBoundsSlot.captured)
        }

        @Test
        @DisplayName("Then adjustViewBounds should not be set when both width and height are not null")
        fun testsIfTheAdjustViewBoundsIsNotSet() {
            // Given
            val image = imageLocal.applyStyle(
                Style(
                    size = Size(
                        width = 100.unitReal(),
                        height = 100.unitReal(),
                    )
                )
            )

            // When
            image.buildView(rootView)

            // Then
            verify(exactly = 0) { imageView.adjustViewBounds = any() }
        }

        @Test
        @DisplayName("Then adjustViewBounds should be set before scaleType")
        fun testsIfTheAdjustViewBoundsIsSetBeforeScaleType() {
            // Given
            val image = imageLocal.applyStyle(Style(size = Size(width = 100.unitReal())))
            every { imageView.scaleType = any() } just Runs

            // When
            image.buildView(rootView)

            // Then
            verifyOrder {
                imageView.adjustViewBounds = any()
                imageView.scaleType = any()
            }
        }

        @Test
        @DisplayName("Then scaleType should be set as desired if design system is NULL")
        fun testsTheScaleTypeSetIfDesignSystemIsNull() {
            // Given
            val scaleType = ImageView.ScaleType.CENTER_CROP
            every { BeagleEnvironment.beagleSdk.designSystem } returns null
            every { imageView.scaleType = capture(scaleTypeSlot) } just Runs
            imageLocal = imageLocal.copy(mode = ImageContentMode.CENTER_CROP)

            // When
            imageLocal.buildView(rootView)

            // Then
            assertEquals(scaleType, scaleTypeSlot.captured)
            verify(exactly = 0) { imageView.setImageResource(IMAGE_RES) }
        }

        @Test
        @DisplayName("Then the scale type should be set as requested")
        fun testsTheScaleTypeSet() {
            // Given
            val scaleTypeSlot = slot<ImageView.ScaleType>()
            every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

            // When
            imageRemote.buildView(rootView)

            // Then
            assertEquals(scaleType, scaleTypeSlot.captured)
        }

        @Test
        @DisplayName("Then the placeHolder for a remote image should set a Local Image path")
        fun testsIfTheSetImageResourceForALocalImageIsCalled() {
            //Given
            imageRemote = Image(ImagePath.Remote("", ImagePath.Local("imageName")))

            // When
            imageRemote.buildView(rootView)

            // Then
            verify(atLeast = 1) { imageView.setImageResource(IMAGE_RES) }
        }

        @Test
        @DisplayName("Then set corner radius correctly")
        fun testCornerRadius() {
            // When
            imageRemote.buildView(rootView)

            // Then
            verify {
                ViewFactory.makeImageView(rootView.getContext(), CornerRadius(radius = 10.0))
            }
        }
    }
}