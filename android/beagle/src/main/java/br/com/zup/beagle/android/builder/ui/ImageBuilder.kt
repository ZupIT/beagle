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

package br.com.zup.beagle.android.builder.ui

import br.com.zup.beagle.android.components.Image
import br.com.zup.beagle.android.components.PathType
import br.com.zup.beagle.android.context.Bind
import br.com.zup.beagle.builder.BeagleBuilder
import br.com.zup.beagle.widget.core.ImageContentMode
import kotlin.properties.Delegates

fun image(block: ImageBuilder.() -> Unit) = ImageBuilder().apply(block).build()

class ImageBuilder : BeagleBuilder<Image> {
    var path: Bind<PathType> by Delegates.notNull()
    var mode: ImageContentMode? = null

    fun path(path: Bind<PathType>) = this.apply { this.path = path }
    fun mode(mode: ImageContentMode?) = this.apply { this.mode = mode }

    fun path(block: () -> Bind<PathType>) {
        path(block.invoke())
    }

    fun mode(block: () -> ImageContentMode?) {
        mode(block.invoke())
    }

    override fun build() = Image(path, mode)
}

fun pathTypeLocal(block: PathTypeLocalBuilder.() -> Unit) = PathTypeLocalBuilder().apply(block).build()
fun pathTypeRemote(block: PathTypeRemoteBuilder.() -> Unit) = PathTypeRemoteBuilder().apply(block).build()

interface ImagePathBuilderHelper {
    var imagePath: PathType

    fun imagePath(imagePath: PathType) = this.apply { this.imagePath = imagePath }

    fun imagePath(block: () -> PathType){
        imagePath(block.invoke())
    }

    fun imagePathLocal(block: PathTypeLocalBuilder.() -> Unit) {
        imagePath(PathTypeLocalBuilder().apply(block).build())
    }

    fun imagePathRemote(block: PathTypeRemoteBuilder.() -> Unit){
        imagePath(PathTypeRemoteBuilder().apply(block).build())
    }

}

class PathTypeRemoteBuilder: BeagleBuilder<PathType.Remote> {
    var url: String by Delegates.notNull()
    var placeholder: PathType.Local? = null

    fun url(url: String) = this.apply { this.url = url }
    fun placeholder(placeholder: PathType.Local?) = this.apply { this.placeholder = placeholder }

    fun url(block: () -> String){
        url(block.invoke())
    }

    fun placeholder(block: () -> String){
        placeholder(PathType.Local(block.invoke()))
    }

    override fun build() = PathType.Remote(url, placeholder)

}

class PathTypeLocalBuilder : BeagleBuilder<PathType.Local> {
    var mobileId: String by Delegates.notNull()

    fun mobileId(mobileId: String) = this.apply { this.mobileId = mobileId }

    fun mobileId(block: () -> String) {
        mobileId(block.invoke())
    }

    override fun build() = PathType.Local(mobileId)
}