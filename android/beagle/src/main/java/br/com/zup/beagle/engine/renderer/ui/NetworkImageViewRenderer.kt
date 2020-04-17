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

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import br.com.zup.beagle.engine.mapper.ViewMapper
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.UIViewRenderer
import br.com.zup.beagle.utils.ComponentStylization
import br.com.zup.beagle.view.BeagleFlexView
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.ui.NetworkImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

internal class NetworkImageViewRenderer(
    override val component: NetworkImage,
    private val viewFactory: ViewFactory = ViewFactory(),
    private val viewMapper: ViewMapper = ViewMapper(),
    private val componentStylization: ComponentStylization<NetworkImage> = ComponentStylization()
) : UIViewRenderer<NetworkImage>() {

    override fun buildView(rootView: RootView): View {
        return if (component.flex?.size != null) {
            makeImageView(rootView).apply {
                Glide.with(this).load(component.path).into(this)
            }
        } else {
            viewFactory.makeBeagleFlexView(rootView.getContext()).also {
                it.addView(makeImageView(rootView).apply {
                    loadImage(this, it)
                }, component.flex ?: Flex())
            }
        }
    }

    private fun makeImageView(rootView: RootView) =
        viewFactory.makeImageView(rootView.getContext()).apply {
            val contentMode = component.contentMode ?: ImageContentMode.FIT_CENTER
            scaleType = viewMapper.toScaleType(contentMode)
        }

    private fun loadImage(imageView: ImageView, beagleFlexView: BeagleFlexView) {
        Glide.with(imageView).asBitmap().load(component.path).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageView.setImageBitmap(resource)
                beagleFlexView.setViewHeight(imageView, resource.height)
                componentStylization.apply(imageView, component)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }
}
