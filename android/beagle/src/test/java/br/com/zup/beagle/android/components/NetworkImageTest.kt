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

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import br.com.zup.beagle.android.components.utils.ComponentStylization
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.ext.applyFlex
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val DEFAULT_URL = "http://teste.com/test.png"

class NetworkImageTest : BaseComponentTest() {

    private val imageView: RoundedImageView = mockk(relaxed = true, relaxUnitFun = true)
    private val requestManager: RequestManager = mockk()
    private val requestBuilder: RequestBuilder<Bitmap> = mockk()
    private val requestBuilderDrawable: RequestBuilder<Drawable> = mockk()
    private val bitmap: Bitmap = mockk()

    private val scaleType = ImageView.ScaleType.FIT_CENTER
    private val flex = Flex(size = Size(width = 100.unitReal(), height = 100.unitReal()))

    private val onRequestListenerSlot = slot<CustomTarget<Bitmap>>()

    private lateinit var networkImage: NetworkImage

    override fun setUp() {
        super.setUp()

        mockkStatic(Glide::class)
        mockkConstructor(ComponentStylization::class)

        every { anyConstructed<ViewFactory>().makeImageView(rootView.getContext()) } returns imageView
        every { Glide.with(any<View>()) } returns requestManager
        every { requestManager.asBitmap() } returns requestBuilder
        every { requestBuilder.load(any<String>()) } returns requestBuilder
        every { requestManager.load(any<String>()) } returns requestBuilderDrawable
        every { requestBuilderDrawable.into(any()) } returns mockk()
        every { requestBuilder.into(capture(onRequestListenerSlot)) } returns mockk()
        every { BeagleEnvironment.beagleSdk.designSystem } returns mockk()

        every { anyConstructed<ComponentStylization<NetworkImage>>().apply(any(), any()) } just Runs

        networkImage = NetworkImage(DEFAULT_URL).applyFlex(flex)
    }

    @Test
    fun build_should_return_a_ImageView_with_desired_scaleType() {
        // Given
        val scaleTypeSlot = slot<ImageView.ScaleType>()
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

        // When
        val view = networkImage.buildView(rootView)

        // Then
        assertTrue(view is ImageView)
        assertEquals(scaleType, scaleTypeSlot.captured)
    }

    @Test
    fun build_should_set_url_to_Glide() {
        networkImage.buildView(rootView)

        verify(exactly = once()) { Glide.with(imageView) }
        verify(exactly = once()) { requestManager.load(DEFAULT_URL) }
        verify(exactly = once()) { requestBuilderDrawable.into(imageView) }
    }

    @Test
    fun build_should_call_makeBeagleFlexView_when_component_has_not_flex() {
        // Given
        networkImage.flex = null

        // When
        val view = networkImage.buildView(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
        verify(exactly = once()) { anyConstructed<ViewFactory>().makeBeagleFlexView(any()) }
        verify(exactly = once()) { beagleFlexView.addView(any(), any<Flex>()) }
    }

    @Test
    fun build_should_call_setImageBitmap_reloadNetworkImageView_when_component_has_not_flex() {
        // Given
        val height = 100
        every { bitmap.height } returns height
        networkImage.flex = null

        // When
        callBuildAndRequest()

        // Then
        verify(exactly = once()) { imageView.setImageBitmap(bitmap) }
        verify(exactly = once()) { beagleFlexView.setViewHeight(imageView, height) }
        verify(exactly = once()) {
            anyConstructed<ComponentStylization<NetworkImage>>()
                .apply(imageView, networkImage)
        }
    }

    private fun callBuildAndRequest() {
        networkImage.buildView(rootView)
        onRequestListenerSlot.captured.onResourceReady(bitmap, mockk())
    }
}