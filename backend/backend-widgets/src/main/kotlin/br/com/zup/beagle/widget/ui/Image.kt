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
 * Define an image view natively using the server driven information received through Beagle.
 *
 * @param name reference an image natively in your local styles file to be applied on this image widget.
 * @param contentMode defines how the declared image will fit the view.
 */
data class Image(
    val name: String,
    val contentMode: ImageContentMode? = null /* = ImageContentMode.FIT_CENTER */
) : Widget()