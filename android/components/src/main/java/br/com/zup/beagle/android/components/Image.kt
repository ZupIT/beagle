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

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.logger.ComponentsMessageLogs
import br.com.zup.beagle.android.mapper.ViewMapper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ComponentsViewFactory
import br.com.zup.beagle.android.view.RoundedImageView
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.ImageContentMode
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@RegisterWidget
data class Image(
    val path: Bind<PathType>,
    val mode: ImageContentMode? = null
) : WidgetView() {
    constructor(
        path: PathType,
        mode: ImageContentMode? = null
    ) : this(
        valueOf(path),
        mode
    )

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    @Transient
    private val componentsViewFactory = ComponentsViewFactory()

    override fun buildView(rootView: RootView): View {
        val imageView: RoundedImageView = getImageView(rootView)
        observeBindChanges(rootView, path) { pathType ->
            when (pathType) {
                is PathType.Local -> {
                    imageView.apply {
                        BeagleEnvironment.beagleSdk.designSystem?.image(pathType.mobileId)?.let {
                            try {
                                setImageResource(it)
                            } catch (ex: Exception) {
                                ComponentsMessageLogs.errorWhileTryingToSetInvalidImage(pathType.mobileId, ex)
                            }
                        }
                    }
                }
                is PathType.Remote -> {
                    val placeholder = pathType.placeholder?.mobileId
                    val requestOptions = getGlideRequestOptions(placeholder)
                    imageView.loadImage(pathType, requestOptions)
                }
            }
        }
        return imageView
    }

    private fun getImageView(rootView: RootView) = componentsViewFactory.makeImageView(rootView.getContext(),
        style?.cornerRadius?.radius ?: 0.0).apply {
        scaleType = viewMapper.toScaleType(mode ?: ImageContentMode.FIT_CENTER)
    }

    private fun ImageView.loadImage(
        path: PathType.Remote,
        requestOptions: RequestOptions) {
        Glide.with(this)
            .setDefaultRequestOptions(requestOptions)
            .asBitmap()
            .load(path.url.formatUrl())
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    this@loadImage.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    @SuppressLint("CheckResult")
    private fun getGlideRequestOptions(placeholder: String?): RequestOptions {
        val requestOptions = RequestOptions()
        getPlaceholder(placeholder)?.let {
            requestOptions.placeholder(it)
        }
        return requestOptions
    }

    private fun getPlaceholder(placeholder: String?): Int? =
        placeholder?.let {
            BeagleEnvironment.beagleSdk.designSystem?.image(it)
        }

}