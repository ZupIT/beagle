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

package br.com.zup.beagle.android.engine.renderer.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import br.com.zup.beagle.android.BaseTest
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.engine.renderer.RootView
import br.com.zup.beagle.ext.unitReal
import br.com.zup.beagle.android.extensions.once
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.components.utils.ComponentStylization
import br.com.zup.beagle.android.view.BeagleFlexView
import br.com.zup.beagle.android.view.BeagleImageView
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.ui.NetworkImage
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private const val DEFAULT_URL = "http://teste.com/test.png"

class NetworkImageViewRendererTest : BaseTest() {

    @MockK
    private lateinit var viewFactory: ViewFactory
    @MockK
    private lateinit var viewMapper: ViewMapper
    @RelaxedMockK
    private lateinit var imageView: BeagleImageView
    @MockK
    private lateinit var context: Context
    @MockK
    private lateinit var requestManager: RequestManager
    @MockK
    private lateinit var requestBuilder: RequestBuilder<Bitmap>
    @MockK
    private lateinit var requestBuilderDrawable: RequestBuilder<Drawable>
    @MockK
    private lateinit var rootView: RootView
    @RelaxedMockK
    private lateinit var networkImage: NetworkImage
    @RelaxedMockK
    private lateinit var beagleFlexView: BeagleFlexView
    @MockK
    private lateinit var bitmap: Bitmap
    @MockK
    private lateinit var componentStylization: ComponentStylization<NetworkImage>

    private val scaleType = ImageView.ScaleType.FIT_CENTER
    private val flex = Flex(size = Size(width = 100.unitReal(), height = 100.unitReal()))

    @InjectMockKs
    private lateinit var networkImageViewRenderer: NetworkImageViewRenderer

    private val onRequestListenerSlot = slot<CustomTarget<Bitmap>>()

    override fun setUp() {
        super.setUp()

        mockkStatic(Glide::class)

        every { Glide.with(any<View>()) } returns requestManager
        every { requestManager.asBitmap() } returns requestBuilder
        every { requestBuilder.load(any<String>()) } returns requestBuilder
        every { requestManager.load(any<String>()) } returns requestBuilderDrawable
        every { requestBuilderDrawable.into(any()) } returns mockk()
        every { requestBuilder.into(capture(onRequestListenerSlot)) } returns mockk()
        every { BeagleEnvironment.beagleSdk.designSystem } returns mockk()
        every { rootView.getContext() } returns context
        every { context.applicationContext } returns mockk()
        every { viewFactory.makeImageView(context) } returns imageView
        every { viewFactory.makeBeagleFlexView(context) } returns beagleFlexView
        every { viewMapper.toScaleType(any()) } returns scaleType
        every { imageView.scaleType = any() } just Runs
        every { networkImage.path } returns DEFAULT_URL
        every { networkImage.flex } returns flex
        every { componentStylization.apply(any(), any()) } just Runs
    }

    override fun tearDown() {
        super.tearDown()
        unmockkAll()
    }

    @Test
    fun build_should_return_a_ImageView_with_desired_scaleType() {
        // Given
        val scaleTypeSlot = slot<ImageView.ScaleType>()
        every { imageView.scaleType = capture(scaleTypeSlot) } just Runs

        // When
        val view = networkImageViewRenderer.build(rootView)

        // Then
        assertTrue(view is ImageView)
        assertEquals(scaleType, scaleTypeSlot.captured)
    }

    @Test
    fun build_should_set_url_to_Glide() {
        networkImageViewRenderer.build(rootView)

        verify(exactly = once()) { Glide.with(imageView) }
        verify(exactly = once()) { requestManager.load(DEFAULT_URL) }
        verify(exactly = once()) { requestBuilderDrawable.into(imageView) }
    }

    @Test
    fun build_should_call_makeBeagleFlexView_when_component_has_not_flex() {
        // Given
        every { networkImage.flex } returns null

        // When
        val view = networkImageViewRenderer.build(rootView)

        // Then
        assertTrue(view is BeagleFlexView)
        verify(exactly = once()) { viewFactory.makeBeagleFlexView(context) }
        verify(exactly = once()) { beagleFlexView.addView(any(), any<Flex>()) }
    }

    @Test
    fun build_should_call_setImageBitmap_reloadNetworkImageView_when_component_has_not_flex() {
        // Given
        val height = 100
        every { networkImage.flex } returns null
        every { bitmap.height } returns height

        // When
        callBuildAndRequest()

        // Then
        verify(exactly = once()) { imageView.setImageBitmap(bitmap) }
        verify(exactly = once()) { beagleFlexView.setViewHeight(imageView, height) }
        verify(exactly = once()) { componentStylization.apply(imageView, networkImage) }
    }

    private fun callBuildAndRequest() {
        networkImageViewRenderer.build(rootView)
        onRequestListenerSlot.captured.onResourceReady(bitmap, mockk())
    }
}
