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

package br.com.zup.beagle.android.view.custom

import android.content.Context
import android.graphics.Canvas
import androidx.appcompat.widget.AppCompatImageView
import br.com.zup.beagle.android.utils.applyRadius

internal class BeagleImageView(context: Context) : AppCompatImageView(context) {

    var cornerRadius = 0.0f

    override fun onDraw(canvas: Canvas?) {
        canvas?.applyRadius(cornerRadius)
        super.onDraw(canvas)
    }
}