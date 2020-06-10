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

package br.com.zup.beagle.android.engine.renderer

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.android.engine.renderer.layout.ComposeComponentViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.UndefinedViewRenderer
import br.com.zup.beagle.android.engine.renderer.ui.ViewConvertableRenderer
import br.com.zup.beagle.android.widget.core.ViewConvertable
import br.com.zup.beagle.android.widget.ui.UndefinedWidget
import br.com.zup.beagle.widget.layout.ComposeComponent

internal class UIViewRendererFactory : AbstractViewRendererFactory {

    override fun make(component: ServerDrivenComponent): ViewRenderer<*> {
        return if (component is ComposeComponent) {
            ComposeComponentViewRenderer(component)
        } else {
            when (component) {
                is ViewConvertable, !is UndefinedWidget -> ViewConvertableRenderer(component as ViewConvertable)
                else -> UndefinedViewRenderer(component)
            }
        }
    }
}