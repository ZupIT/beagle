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
import br.com.zup.beagle.android.engine.mapper.ViewMapper
import br.com.zup.beagle.android.setup.BeagleEnvironment
import br.com.zup.beagle.android.widget.core.RootView
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.ui.Image

data class Image(override val name: String,
                 override val contentMode: ImageContentMode? = null /* = ImageContentMode.FIT_CENTER */)
    : br.com.zup.beagle.widget.ui.Image(name, contentMode), ViewConvertable {

    @Transient
    private val viewMapper: ViewMapper = ViewMapper()

    override fun buildView(rootView: RootView): View {
        val imageView = ImageView(rootView.getContext())
        imageView.setData(this, viewMapper)
        return imageView
    }

    private fun ImageView.setData(widget: Image, viewMapper: ViewMapper) {
        val contentMode = widget.contentMode ?: ImageContentMode.FIT_CENTER
        scaleType = viewMapper.toScaleType(contentMode)
        val designSystem = BeagleEnvironment.beagleSdk.designSystem
        designSystem?.image(widget.name)?.let {
            this.setImageResource(it)
        }
    }
}