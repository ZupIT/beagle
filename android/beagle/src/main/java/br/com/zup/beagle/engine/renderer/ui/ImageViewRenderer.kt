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

package br.com.zup.beagle.engine.renderer.ui

import android.view.View
import android.widget.ImageView
import br.com.zup.beagle.engine.mapper.ViewMapper
import br.com.zup.beagle.engine.renderer.RootView
import br.com.zup.beagle.engine.renderer.UIViewRenderer
import br.com.zup.beagle.setup.BeagleEnvironment
import br.com.zup.beagle.view.ViewFactory
import br.com.zup.beagle.widget.core.ImageContentMode
import br.com.zup.beagle.widget.ui.Image

internal class ImageViewRenderer(
    override val component: Image,
    private val viewFactory: ViewFactory = ViewFactory(),
    private val viewMapper: ViewMapper = ViewMapper()
) : UIViewRenderer<Image>() {

    override fun buildView(rootView: RootView): View {
        return viewFactory.makeImageView(rootView.getContext()).apply {
            setData(component, viewMapper)
        }
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
