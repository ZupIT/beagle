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

package br.com.zup.beagle.setup

import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import br.com.zup.beagle.R

abstract class DesignSystem {
    @DrawableRes open fun image(name: String): Int? = null
    @StyleRes abstract fun theme(): Int
    @StyleRes open fun textAppearance(name: String): Int? = null
    @StyleRes open fun buttonStyle(name: String): Int? = null
    @StyleRes abstract fun toolbarStyle(name: String): Int
    @StyleRes abstract fun tabViewStyle(name: String): Int?
}