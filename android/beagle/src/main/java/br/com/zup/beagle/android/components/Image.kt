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

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.ImageContentMode
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@RegisterWidget
data class Image(
    val path: Bind<ImagePath>,
    val mode: ImageContentMode? = null
) : WidgetView() {
    constructor(
        path: ImagePath,
        mode: ImageContentMode? = null
    ) : this(
        valueOf(path),
        mode
    )

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    @Transient
    private val viewFactory = ViewFactory()

    override fun buildView(rootView: RootView): View {
        val imageView: RoundedImageView = getImageView(rootView)
        observeBindChanges(rootView, imageView, path) { pathType ->
            when (pathType) {
                is ImagePath.Local -> {
                    loadLocalImage(imageView, pathType)
                }
                is ImagePath.Remote -> {
                    loadRemoteImage(imageView, pathType)
                }
            }
        }
        return imageView
    }

    private fun getImageView(rootView: RootView) = viewFactory.makeImageView(rootView.getContext(),
        style?.cornerRadius?.radius ?: 0.0).apply {
        scaleType = viewMapper.toScaleType(mode ?: ImageContentMode.FIT_CENTER)
    }

    private fun loadLocalImage(imageView: ImageView, pathType: ImagePath.Local) {
        imageView.apply {
            getImage(pathType.mobileId)?.let {
                try {
                    setImageResource(it)
                } catch (ex: Exception) {
                    BeagleMessageLogs.errorWhileTryingToSetInvalidImage(pathType.mobileId, ex)
                }
            }
        }
    }

    private fun loadRemoteImage(imageView: ImageView, pathType: ImagePath.Remote) {
        val placeholder = pathType.placeholder?.mobileId
        setPlaceHolder(placeholder, imageView)
        imageView.loadImage(pathType)
    }

    private fun setPlaceHolder(placeholder: String?, imageView: ImageView) {
        getImage(placeholder)?.let {
            imageView.setImageResource(it)
        }
    }

    private fun ImageView.loadImage(
        path: ImagePath.Remote) {
        Glide.with(this)
            .load(path.url.formatUrl())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    this@loadImage.setImageDrawable(resource)
                    this@loadImage.requestLayout()
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })

    }

    private fun getImage(imagePath: String?): Int? =
        imagePath?.let {
            BeagleEnvironment.beagleSdk.designSystem?.image(it)
        }

}

sealed class ImagePath {
    data class Local(val mobileId: String) : ImagePath()
    data class Remote(val url: String, val placeholder: Local? = null) : ImagePath()
}
