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
import br.com.zup.beagle.widget.core.ImageContentMode

/**
 * Network widget will load images according to the path specified on its attribute.
 *
 * @param path
 *                  specify the URL where the image could be found.
 *                  Beagle will turn the image into an object that can be displayed natively.
 * @param contentMode defines how the image fits the view it's in.
 */
data class NetworkImage(
    val path: String,
    val contentMode: ImageContentMode? = null /* = ImageContentMode.FIT_CENTER */
) : Widget()