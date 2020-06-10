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
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.widget.core.ImageContentMode
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

data class NetworkImage(
    override val path: String,
    override val contentMode: ImageContentMode? = null
) : br.com.zup.beagle.widget.ui.NetworkImage(path, contentMode), ViewConvertable {

    @Transient
    private val componentStylization: ComponentStylization<NetworkImage> = ComponentStylization()

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    override fun buildView(rootView: RootView): View {
        val imageView = ImageView(rootView.getContext())

        if (flex?.size != null) {
            imageView.setData(this, viewMapper)
            Glide.with(rootView.getContext()).load(path).into(imageView)
        } else {
            loadImage(imageView)
        }

        return imageView
    }

    private fun ImageView.setData(widget: NetworkImage, viewMapper: ViewMapper) {
        val contentMode = widget.contentMode ?: ImageContentMode.FIT_CENTER
        scaleType = viewMapper.toScaleType(contentMode)
    }

    private fun loadImage(imageView: ImageView) {
        Glide.with(imageView).asBitmap().load(path).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                imageView.setImageBitmap(resource)
                componentStylization.apply(imageView, this@NetworkImage)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }
}