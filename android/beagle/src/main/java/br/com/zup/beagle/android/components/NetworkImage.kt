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
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.components.utils.ComponentStylization
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ImageContentMode
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

data class NetworkImage(
    val path: String,
    val contentMode: ImageContentMode? = null
) : WidgetView() {

    @Transient
    private val componentStylization: ComponentStylization<NetworkImage> = ComponentStylization()

    @Transient
    private val viewFactory = ViewFactory()

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    override fun buildView(rootView: RootView): View {
        return if (flex?.size != null) {
            makeImageView(rootView).apply {
                Glide.with(this).load(path).into(this)
            }
        } else {
            viewFactory.makeBeagleFlexView(rootView.getContext()).also {
                it.addView(makeImageView(rootView).apply {
                    loadImage(this, it)
                }, flex ?: Flex())
            }
        }
    }

    private fun makeImageView(rootView: RootView) =
        viewFactory.makeImageView(rootView.getContext(),
            style?.cornerRadius?.radius ?: 0.0).apply {
            val contentMode = contentMode ?: ImageContentMode.FIT_CENTER
            scaleType = viewMapper.toScaleType(contentMode)
        }

    private fun loadImage(imageView: ImageView, beagleFlexView: BeagleFlexView) {
        Glide.with(imageView).asBitmap().load(path).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageView.setImageBitmap(resource)
                beagleFlexView.setViewHeight(imageView, resource.height)
                componentStylization.apply(imageView, this@NetworkImage)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }
}