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

package br.com.zup.beagle.android.engine.mapper

import android.widget.ImageView
import br.com.zup.beagle.widget.core.ImageContentMode

class ViewMapper {

    fun toScaleType(contentMode: ImageContentMode) = when (contentMode) {
        ImageContentMode.FIT_XY -> ImageView.ScaleType.FIT_XY
        ImageContentMode.FIT_CENTER -> ImageView.ScaleType.FIT_CENTER
        ImageContentMode.CENTER_CROP -> ImageView.ScaleType.CENTER_CROP
        ImageContentMode.CENTER -> ImageView.ScaleType.CENTER
    }
}
