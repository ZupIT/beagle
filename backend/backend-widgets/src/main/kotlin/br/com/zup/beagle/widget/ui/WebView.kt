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
import br.com.zup.beagle.widget.context.Bind
import br.com.zup.beagle.widget.context.valueOf

/**
 * A WebView widget will define a WebView natively using the server driven information received through Beagle.
 *
 * @param url
 *              define the initial page that the WebView will load when presented .
 *              This attribute must be declared and it cannot be null.
 *
 */

data class WebView(
    val url: Bind<String>
) : Widget() {
    constructor(url: String) : this(valueOf(url))
}
