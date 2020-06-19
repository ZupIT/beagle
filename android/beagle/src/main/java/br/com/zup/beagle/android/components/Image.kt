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
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.widget.core.Flex
import br.com.zup.beagle.widget.core.ImageContentMode
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

data class Image(val path: PathType, val mode: ImageContentMode? = null) : WidgetView(){

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    @Transient
    private val viewFactory = ViewFactory()

    @Transient
    private val componentStylization: ComponentStylization<Image> = ComponentStylization()

    override fun buildView(rootView: RootView): View =
        when(path is PathType.Local){
            true -> {
                val imageView = viewFactory.makeImageView(rootView.getContext(), style?.cornerRadius?.radius ?: 0.0)
                imageView.setData(this, viewMapper)
                imageView
            }
            false ->{
                if (flex?.size != null) {
                    makeImageView(rootView).apply {
                        Glide.with(this).load(path.image).into(this)
                    }
                } else {
                    viewFactory.makeBeagleFlexView(rootView.getContext()).also {
                        it.addView(makeImageView(rootView).apply {
                            this.loadImage(it)
                        }, flex ?: Flex())
                    }
                }
            }
        }

    private fun ImageView.setData(widget: Image, viewMapper: ViewMapper) {
        val contentMode = widget.mode ?: ImageContentMode.FIT_CENTER
        scaleType = viewMapper.toScaleType(contentMode)
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        designSystem?.image(widget.path.image)?.let {
            this.setImageResource(it)
        }
    }

    private fun ImageView.loadImage(beagleFlexView: BeagleFlexView) {
        Glide.with(this).asBitmap().load(path.image).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                this@loadImage.setImageBitmap(resource)
                beagleFlexView.setViewHeight(this@loadImage, resource.height)
                componentStylization.apply(this@loadImage, this@Image)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }

    private fun makeImageView(rootView: RootView) =
        viewFactory.makeImageView(rootView.getContext(),
            style?.cornerRadius?.radius ?: 0.0).apply {
            scaleType = viewMapper.toScaleType(mode ?: ImageContentMode.FIT_CENTER)
        }

}

sealed class PathType(@Transient val image: String) {
    data class Local(val mobileId: String) : PathType(mobileId)
    data class Remote(val url: String) : PathType(url)
}