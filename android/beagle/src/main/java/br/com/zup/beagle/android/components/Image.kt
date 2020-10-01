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
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.cache.imagecomponent.ImageDownloader
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import br.com.zup.beagle.android.utils.observeBindChanges
import br.com.zup.beagle.android.view.ViewFactory
import br.com.zup.beagle.android.widget.RootView
import br.com.zup.beagle.android.widget.WidgetView
import br.com.zup.beagle.annotation.RegisterWidget
import br.com.zup.beagle.widget.core.ImageContentMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@RegisterWidget
data class Image constructor(
    val path: Bind<ImagePath>,
    val mode: ImageContentMode? = null
) : WidgetView() {

    constructor(path: ImagePath, mode: ImageContentMode? = null) : this(valueOf(path), mode)

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    @Transient
    private val imageDownloader: ImageDownloader = ImageDownloader()

    @Transient
    private val viewFactory = ViewFactory()

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

    private fun getImageView(rootView: RootView) = viewFactory.makeImageView(rootView.getContext(),
        style?.cornerRadius?.radius ?: 0.0).apply {
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
        pathType.placeholder?.let { local ->
            loadLocalImage(rootView, imageView, local)
        }

        observeBindChanges(rootView, imageView, pathType.url) { url ->
            imageView.loadImage(url ?: "")
        }

    }

    private fun ImageView.loadImage(url: String) {
        val view = this@loadImage

        view.post {
            CoroutineScope(CoroutineDispatchers.IO).launch {

                val bitmap = try {
                    imageDownloader.getRemoteImage(url.formatUrl(), this@loadImage.width, this@loadImage.height)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                withContext(CoroutineDispatchers.Main) {
                    view.setImageBitmap(bitmap)
                }
            }
        }
    }

    private fun getImage(imagePath: String?): Int? =
        imagePath?.let {
            BeagleEnvironment.beagleSdk.designSystem?.image(it)
        }

}

sealed class ImagePath {
    data class Local(val mobileId: Bind<String>) : ImagePath() {
        constructor(mobileId: String) : this(expressionOrValueOf(mobileId))
    }

    data class Remote(val url: Bind<String>, val placeholder: Local? = null) : ImagePath() {
        constructor(url: String, placeholder: Local? = null) : this(expressionOrValueOf(url), placeholder)
    }
}
