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
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ViewMapperTest {

    private lateinit var viewMapper: ViewMapper

    @Before
    fun setUp() {
        viewMapper = ViewMapper()
    }

    @Test
    fun toScaleType_should_return_ScaleType_FIT_XY_when_contentMode_is_FIT_XY() {
        val scaleType = viewMapper.toScaleType(ImageContentMode.FIT_XY)

        assertEquals(ImageView.ScaleType.FIT_XY, scaleType)
    }

    @Test
    fun toScaleType_should_return_ScaleType_FIT_CENTER_when_contentMode_is_FIT_CENTER() {
        val scaleType = viewMapper.toScaleType(ImageContentMode.FIT_CENTER)

        assertEquals(ImageView.ScaleType.FIT_CENTER, scaleType)
    }

    @Test
    fun toScaleType_should_return_ScaleType_CENTER_CROP_when_contentMode_is_CENTER_CROP() {
        val scaleType = viewMapper.toScaleType(ImageContentMode.CENTER_CROP)

        assertEquals(ImageView.ScaleType.CENTER_CROP, scaleType)
    }

    @Test
    fun toScaleType_should_return_ScaleType_CENTER_when_contentMode_is_CENTER() {
        val scaleType = viewMapper.toScaleType(ImageContentMode.CENTER)

        assertEquals(ImageView.ScaleType.CENTER, scaleType)
    }
}