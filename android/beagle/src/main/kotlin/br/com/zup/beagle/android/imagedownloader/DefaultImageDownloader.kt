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

package br.com.zup.beagle.android.imagedownloader

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import br.com.zup.beagle.android.cache.imagecomponent.ImageDownloader
import br.com.zup.beagle.android.data.formatUrl
import br.com.zup.beagle.android.logger.BeagleMessageLogs
import br.com.zup.beagle.android.utils.CoroutineDispatchers
import br.com.zup.beagle.android.widget.RootView
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class DefaultImageDownloader : BeagleImageDownloader {

    private val imageDownloader: ImageDownloader = ImageDownloader()

    override fun download(url: String, imageView: ImageView, rootView: RootView) {
        imageView.post {
            rootView.getLifecycleOwner().lifecycleScope.launch(CoroutineDispatchers.IO) {
                try {
                    val formattedUrl = url.formatUrl().takeIf { it.isNotEmpty() } ?: url
                    val bitmap = imageDownloader.getRemoteImage(
                        formattedUrl,
                        imageView.width,
                        imageView.height,
                        rootView.getContext(),
                    )
                    setImage(imageView, bitmap)
                } catch (e: Exception) {
                    BeagleMessageLogs.errorWhileTryingToDownloadImage(url, e)
                }
            }
        }
    }

    private suspend fun setImage(view: ImageView, bitmap: Bitmap?) {
        withContext(CoroutineDispatchers.Main) {
            view.setImageBitmap(bitmap)
        }
    }
}
