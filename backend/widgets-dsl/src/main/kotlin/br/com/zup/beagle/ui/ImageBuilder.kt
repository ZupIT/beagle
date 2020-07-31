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
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ImagePath
import kotlin.properties.Delegates

fun image(block: ImageBuilder.() -> Unit) = ImageBuilder().apply(block).build()

class ImageBuilder : BeagleBuilder<Image> {
    var path: Bind<ImagePath> by Delegates.notNull()
    var mode: ImageContentMode? = null

    fun path(path: Bind<ImagePath>) = this.apply { this.path = path }
    fun mode(mode: ImageContentMode?) = this.apply { this.mode = mode }

    fun path(block: () -> Bind<ImagePath>) {
        path(block.invoke())
    }

    fun mode(block: () -> ImageContentMode?) {
        mode(block.invoke())
    }

    override fun build() = Image(path, mode)
}

fun imagePath(block: ImagePathBuilder.() -> Unit) = ImagePathBuilder().apply(block).build()

class ImagePathBuilder: BeagleBuilder<ImagePath> {
    var imagePath: ImagePath by Delegates.notNull()

    fun local(imagePath: ImagePath.Local) = this.apply { this.imagePath = imagePath }

    fun remote(imagePath: ImagePath.Remote) = this.apply { this.imagePath = imagePath }

    fun local(block: ImagePathLocalBuilder.() -> Unit) {
        local(ImagePathLocalBuilder().apply(block).build())
    }

    fun remote(block: ImagePathRemoteBuilder.() -> Unit) {
        remote(ImagePathRemoteBuilder().apply(block).build())
    }

    override fun build() = imagePath
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

class ImagePathLocalBuilder: BeagleBuilder<ImagePath.Local> {
    var imagePathLocal: ImagePath.Local by Delegates.notNull()

    fun imagePathLocal(imagePathLocal: ImagePath.Local)
        = this.apply { this.imagePathLocal = imagePathLocal }
    fun justMobile(mobileId: String) = imagePathLocal(ImagePath.Local.justMobile(mobileId))
    fun justWeb(webUrl: String) = imagePathLocal(ImagePath.Local.justWeb(webUrl))

    fun both(block: ImagePathLocalBothBuilder.() -> Unit){
        imagePathLocal(ImagePathLocalBothBuilder().apply(block).build())
    }

    fun justMobile(mobileId: () -> String){
        justMobile(mobileId.invoke())
    }

    fun justWeb(webUrl: () -> String){
        justWeb(webUrl.invoke())
    }

    override fun build() = imagePathLocal
}

class ImagePathLocalBothBuilder : BeagleBuilder<ImagePath.Local> {
    var webUrl: String by Delegates.notNull()
    var mobileId: String by Delegates.notNull()

    fun webUrl(webUrl: String) = this.apply { this.webUrl = webUrl }
    fun mobileId(mobileId: String) = this.apply { this.mobileId = mobileId }

    fun webUrl(block: () -> String) {
        webUrl(block.invoke())
    }

    fun mobileId(block: () -> String) {
        mobileId(block.invoke())
    }

    override fun build() = ImagePath.Local.both(webUrl, mobileId)
}