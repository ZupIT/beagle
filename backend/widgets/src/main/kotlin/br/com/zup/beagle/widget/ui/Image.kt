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

package br.com.zup.beagle.widget.ui

import br.com.zup.beagle.widget.Widget
import br.com.zup.beagle.widget.builder.BeagleBuilder
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf
import br.com.zup.beagle.widget.core.ImageContentMode
import kotlin.properties.Delegates

/**
 * Define an image view using the server driven information received through Beagle.
 *
 * @param path defines where the source of the image is
 * @param mode defines how the declared image will fit the view.
 */
data class Image(val path: Bind<ImagePath>, val mode: ImageContentMode? = null) : Widget() {
    constructor(path: ImagePath, mode: ImageContentMode? = null) : this(valueOf(path), mode)

    class Builder : BeagleBuilder<Image> {
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
}

fun image(block: Image.Builder.() -> Unit) = Image.Builder().apply(block).build()


/**
 * Define the source of image data to populate the image view.
 *
 * @param url the URL from which the image can be obtained
 * @param placeholder the image that will used as placeholder when set a remote image.
 * */
sealed class ImagePath(val url: String?, val placeholder: Local? = null) {
    /**
     * Define an image whose data is local to the client app.
     *
     * @param webUrl reference the path to an image in your web app files.
     * @param mobileId reference an image natively in your mobile app local styles file.
     * */
    class Local internal constructor(webUrl: String?, val mobileId: String?) : ImagePath(webUrl) {
        companion object {
            fun both(webUrl: String, mobileId: String) = Local(webUrl, mobileId)
            fun justMobile(mobileId: String) = Local(null, mobileId)
            fun justWeb(webUrl: String) = Local(webUrl, null)
        }

        class Builder : BeagleBuilder<Local> {
            var webUrl: String? = null
            var mobileId: String? = null

            fun webUrl(webUrl: String?) = this.apply { this.webUrl = webUrl }
            fun mobileId(mobileId: String?) = this.apply { this.mobileId = mobileId }

            fun webUrl(block: () -> String?) {
                webUrl(block.invoke())
            }

            fun mobileId(block: () -> String?) {
                mobileId(block.invoke())
            }

            override fun build() = Local(webUrl, mobileId)
        }
    }

    /**
     * Define an image whose data needs to be downloaded from a source external to the client app.
     *
     * @param remoteUrl reference the path where the image should be fetched from.
     * @param placeholder reference an image natively in your mobile app local styles file to be used as placeholder.
     * */
    class Remote(remoteUrl: String, placeholder: Local? = null) : ImagePath(remoteUrl, placeholder) {
        class Builder : BeagleBuilder<Remote> {
            var remoteUrl: String by Delegates.notNull()
            var placeholder: Local? = null

            fun remoteUrl(remoteUrl: String) = this.apply { this.remoteUrl = remoteUrl }
            fun placeholder(placeholder: Local?) = this.apply { this.placeholder = placeholder }

            fun remoteUrl(block: () -> String) {
                remoteUrl(block.invoke())
            }

            fun placeholder(block: () -> Local?) {
                placeholder(block.invoke())
            }

            override fun build() = Remote(remoteUrl, placeholder)
        }
    }
}

fun imagePathLocal(block: ImagePath.Local.Builder.() -> Unit)
    = ImagePath.Local.Builder().apply(block).build()
fun imagePathRemote(block: ImagePath.Remote.Builder.() -> Unit)
    = ImagePath.Remote.Builder().apply(block).build()