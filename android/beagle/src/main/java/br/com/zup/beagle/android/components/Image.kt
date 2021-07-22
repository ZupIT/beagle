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

import android.view.View
import android.widget.ImageView
import br.com.zup.beagle.android.components.utils.RoundedImageView
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.android.context.expressionOrValueOf
import br.com.zup.beagle.android.context.valueOf
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.imagedownloader.DefaultImageDownloader
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.core.BeagleJson
import br.com.zup.beagle.core.CornerRadius
import br.com.zup.beagle.widget.core.ImageContentMode

/**
 * Define an image view using the server driven information received through Beagle.
 *
 * @param path defines where the source of the image is
 * @param mode defines how the declared image will fit the view.
 */
@RegisterWidget("image")
data class Image constructor(
    val path: Bind<ImagePath>,
    val mode: ImageContentMode? = null,
) : WidgetView() {

    constructor(path: ImagePath, mode: ImageContentMode? = null) : this(valueOf(path), mode)

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    override fun buildView(rootView: RootView): View {
        val imageView: RoundedImageView = getImageView(rootView)

        observeBindChanges(rootView, imageView, path) { pathType ->
            when (pathType) {
                is ImagePath.Local -> {
                    loadLocalImage(rootView, imageView, pathType)
                }
                is ImagePath.Remote -> {
                    loadRemoteImage(rootView, imageView, pathType)
                }
            }
        }

        return imageView
    }

    private fun getImageView(rootView: RootView) = ViewFactory.makeImageView(
        context = rootView.getContext(),
        cornerRadius = style?.cornerRadius ?: CornerRadius(),
    ).apply {
        style?.size?.let { size ->
            if (size.width == null || size.height == null) {
                adjustViewBounds = true
            }
        }
        scaleType = viewMapper.toScaleType(mode ?: ImageContentMode.FIT_CENTER)
    }

    private fun loadLocalImage(rootView: RootView, imageView: ImageView, pathType: ImagePath.Local) {
        imageView.apply {
            observeBindChanges(rootView, imageView, pathType.mobileId) { mobileId ->
                getImage(mobileId)?.let {
                    try {
                        setImageResource(it)
                    } catch (ex: Exception) {
                        BeagleMessageLogs.errorWhileTryingToSetInvalidImage(mobileId ?: "", ex)
                    }
                }
            }
        }
    }

    private fun loadRemoteImage(rootView: RootView, imageView: ImageView, pathType: ImagePath.Remote) {
        loadPlaceholder(pathType, rootView, imageView)

        observeBindChanges(rootView, imageView, pathType.url) { url ->
            loadPlaceholder(pathType, rootView, imageView) {
                imageView.setImageDrawable(null)
            }
            downloadImage(imageView, url ?: "", rootView)
        }
    }

    private fun loadPlaceholder(
        pathType: ImagePath.Remote,
        rootView: RootView,
        imageView: ImageView,
        fallback: (() -> Unit)? = null,
    ) {
        pathType.placeholder?.let { local ->
            loadLocalImage(rootView, imageView, local)
        } ?: fallback?.invoke()
    }

    private fun downloadImage(imageView: ImageView, url: String, rootView: RootView) =
        BeagleEnvironment.beagleSdk.imageDownloader?.download(url, imageView, rootView)
            ?: DefaultImageDownloader().download(url, imageView, rootView)

    private fun getImage(imagePath: String?): Int? =
        imagePath?.let {
            BeagleEnvironment.beagleSdk.designSystem?.image(it)
        }
}

/**
 * Define the source of image data to populate the image view.
 * */

@BeagleJson
sealed class ImagePath {
    /**
     * Define an image whose data is local to the client app.
     *
     * @param mobileId reference an image natively in your mobile app local styles file.
     * */
    @BeagleJson
    data class Local(
        val mobileId: Bind<String>,
    ) : ImagePath() {
        constructor(mobileId: String) : this(expressionOrValueOf(mobileId))
    }

    /**
     * Define an image whose data needs to be downloaded from a source external to the client app.
     *
     * @param url reference the path where the image should be fetched from.
     * @param placeholder reference an image natively in your mobile app local styles file to be used as placeholder.
     * */
    @BeagleJson
    data class Remote(
        val url: Bind<String>,
        val placeholder: Local? = null,
    ) : ImagePath() {
        constructor(url: String, placeholder: Local? = null) : this(expressionOrValueOf(url), placeholder)
    }
}
