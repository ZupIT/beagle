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

package br.com.zup.beagle.android.components.utils

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import br.com.zup.beagle.android.components.utils.FLOAT_ZERO

internal fun Canvas.applyRadius(radius: Float) {
    if (radius > FLOAT_ZERO) {
        val path = Path()
        val rect = RectF(FLOAT_ZERO, FLOAT_ZERO, this.width.toFloat(), this.height.toFloat())
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        this.clipPath(path)
    }
}