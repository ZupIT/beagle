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

package br.com.zup.beagle.ui

import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import kotlin.properties.Delegates

fun image(block: ImageBuilder.() -> Unit) = ImageBuilder().apply(block).build()

class ImageBuilder : BeagleBuilder<Image> {
    var path: ImagePath by Delegates.notNull()
    var mode: ImageContentMode? = null

    fun path(path: ImagePath) = this.apply { this.path = path }
    fun mode(mode: ImageContentMode?) = this.apply { this.mode = mode }

    fun path(block: () -> ImagePath) {
        path(block.invoke())
    }

    fun mode(block: () -> ImageContentMode?) {
        mode(block.invoke())
    }

    override fun build() = Image(path, mode)
}

fun imagePathLocal(block: ImagePathLocalBuilder.() -> Unit) = ImagePathLocalBuilder().apply(block).build()
fun imagePathRemote(block: ImagePathRemoteBuilder.() -> Unit) = ImagePathRemoteBuilder().apply(block).build()

interface ImagePathBuilderHelper {
    var imagePath: ImagePath

    fun imagePath(imagePath: ImagePath) = this.apply { this.imagePath = imagePath }

    fun imagePath(block: () -> ImagePath) {
        imagePath(block.invoke())
    }

    fun imagePathLocal(block: ImagePathLocalBuilder.() -> Unit) {
        imagePath(ImagePathLocalBuilder().apply(block).build())
    }

    fun imagePathRemote(block: ImagePathRemoteBuilder.() -> Unit) {
        imagePath(ImagePathRemoteBuilder().apply(block).build())
    }

}

class ImagePathRemoteBuilder : BeagleBuilder<ImagePath.Remote> {
    var remoteUrl: String by Delegates.notNull()
    var placeholder: ImagePath.Local? = null

    fun remoteUrl(remoteUrl: String) = this.apply { this.remoteUrl = remoteUrl }
    fun placeholder(placeholder: ImagePath.Local?) = this.apply { this.placeholder = placeholder }

    fun remoteUrl(block: () -> String) {
        remoteUrl(block.invoke())
    }

    fun placeholder(block: ImagePathLocalBuilder.() -> Unit) {
        placeholder(ImagePathLocalBuilder().apply(block).build())
    }

    override fun build() = ImagePath.Remote(remoteUrl, placeholder)
}

class ImagePathLocalBuilder : BeagleBuilder<ImagePath.Local> {
    var webUrl: String? = null
    var mobileId: String? = null

    fun webUrl(webUrl: String) = this.apply { this.webUrl = webUrl }
    fun mobileId(mobileId: String) = this.apply { this.mobileId = mobileId }

    fun webUrl(block: () -> String) {
        webUrl(block.invoke())
    }

    fun mobileId(block: () -> String) {
        mobileId(block.invoke())
    }

    override fun build() = when {
        webUrl.isNullOrBlank() && mobileId.isNullOrBlank() -> {
            throw IllegalStateException("At least one of mobileId and webUrl must be set to a non null value")
        }
        !webUrl.isNullOrBlank() && !mobileId.isNullOrBlank() -> ImagePath.Local.both(webUrl!!, mobileId!!)
        !webUrl.isNullOrBlank() -> ImagePath.Local.justWeb(webUrl!!)
        else -> ImagePath.Local.justMobile(mobileId!!)
    }
}