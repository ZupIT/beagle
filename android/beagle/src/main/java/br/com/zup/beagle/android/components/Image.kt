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
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.view.custom.BeagleFlexView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.Style
import br.com.zup.beagle.widget.core.ImageContentMode
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@RegisterWidget
data class Image(
    val path: Bind<PathType>,
    val mode: ImageContentMode? = null) : WidgetView(){
    constructor(
        path: PathType,
        mode: ImageContentMode? = null) : this(
        valueOf(path),
        mode
    )

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        var imageView:View = getImageView(rootView)
        observeBindChanges(rootView, path){ pathyType->
            when (pathyType) {
                is PathType.Local -> {
                    imageView = getImageView(rootView).apply {
                        BeagleEnvironment.beagleSdk.designSystem?.image(pathyType.mobileId)?.let {
                            this.setImageResource(it)
                        }
                    }
                }
                is PathType.Remote -> {
                    imageView = if (style?.size != null) {
                        getImageView(rootView).apply {
                            Glide.with(this).load(pathyType.url).into(this)
                        }
                    } else {
                        viewFactory.makeBeagleFlexView(rootView.getContext()).also {
                            it.addView(getImageView(rootView).apply {
                                this.loadImage(pathyType, it)
                            }, style ?: Style())
                        }
                    }
                }
            }
        }
        return imageView
    }

    private fun getImageView(rootView: RootView) = viewFactory.makeImageView(rootView.getContext(),
        style?.cornerRadius?.radius ?: 0.0).apply {
        scaleType = viewMapper.toScaleType(mode ?: ImageContentMode.FIT_CENTER)
    }

    private fun ImageView.loadImage(path: PathType.Remote, beagleFlexView: BeagleFlexView) {
        Glide.with(this).asBitmap().load(path.url).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                this@loadImage.setImageBitmap(resource)
                beagleFlexView.setViewHeight(this@loadImage, resource.height)
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })
    }

}

sealed class PathType {
    data class Local(val mobileId: String) : PathType()
    data class Remote(val url: String) : PathType()
}
