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

package br.com.zup.beagle.engine.renderer

import br.com.zup.beagle.core.ServerDrivenComponent
import br.com.zup.beagle.engine.renderer.layout.ComposeComponentViewRenderer
import br.com.zup.beagle.engine.renderer.layout.FormInputHiddenViewRenderer
import br.com.zup.beagle.engine.renderer.layout.FormInputViewRenderer
import br.com.zup.beagle.engine.renderer.layout.FormSubmitViewRenderer
import br.com.zup.beagle.engine.renderer.ui.ButtonViewRenderer
import br.com.zup.beagle.engine.renderer.ui.ImageViewRenderer
import br.com.zup.beagle.engine.renderer.ui.ListViewRenderer
import br.com.zup.beagle.engine.renderer.ui.NetworkImageViewRenderer
import br.com.zup.beagle.engine.renderer.ui.TabViewRenderer
import br.com.zup.beagle.engine.renderer.ui.TextViewRenderer
import br.com.zup.beagle.engine.renderer.ui.UndefinedViewRenderer
import br.com.zup.beagle.engine.renderer.ui.ViewConvertableRenderer
import br.com.zup.beagle.engine.renderer.ui.WebViewRenderer
import br.com.zup.beagle.widget.core.ComposeComponent
import br.com.zup.beagle.widget.core.ViewConvertable
import br.com.zup.beagle.widget.form.FormInput
import br.com.zup.beagle.widget.form.FormInputHidden
import br.com.zup.beagle.widget.form.FormSubmit
import br.com.zup.beagle.widget.ui.Button
import br.com.zup.beagle.widget.ui.Image
import br.com.zup.beagle.widget.ui.ListView
import br.com.zup.beagle.widget.ui.NetworkImage
import br.com.zup.beagle.widget.ui.TabView
import br.com.zup.beagle.widget.ui.Text
import br.com.zup.beagle.widget.ui.UndefinedWidget
import br.com.zup.beagle.widget.ui.WebView

internal class UIViewRendererFactory : AbstractViewRendererFactory {

    override fun make(component: ServerDrivenComponent): ViewRenderer<*> {
        return if (component is ComposeComponent) {
            ComposeComponentViewRenderer(component)
        } else {
            when (component) {
                is Button -> ButtonViewRenderer(component)
                is Text -> TextViewRenderer(component)
                is Image -> ImageViewRenderer(component)
                is NetworkImage -> NetworkImageViewRenderer(component)
                is ListView -> ListViewRenderer(component)
                is FormInput -> FormInputViewRenderer(component)
                is FormInputHidden -> FormInputHiddenViewRenderer(component)
                is FormSubmit -> FormSubmitViewRenderer(component)
                is TabView -> TabViewRenderer(component)
                is WebView -> WebViewRenderer(component)
                is ViewConvertable, !is UndefinedWidget -> ViewConvertableRenderer(component as ViewConvertable)
                else -> UndefinedViewRenderer(component)
            }
        }
    }
}