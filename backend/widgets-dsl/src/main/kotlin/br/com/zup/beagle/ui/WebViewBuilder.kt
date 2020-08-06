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
import br.com.zup.beagle.widget.ui.WebView
import kotlin.properties.Delegates

fun webView(block: WebViewBuilder.() -> Unit) = WebViewBuilder().apply(block).build()

class WebViewBuilder: BeagleBuilder<WebView> {
    var url: Bind<String> by Delegates.notNull()

    fun url(url: Bind<String>) = this.apply { this.url = url }

    fun url(block: () -> Bind<String>) {
        url(block.invoke())
    }

    override fun build() = WebView(url)
}