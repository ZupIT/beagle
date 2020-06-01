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

package br.com.zup.beagle.sample.micronaut.service

import br.com.zup.beagle.ext.unitPercent
import br.com.zup.beagle.sample.builder.ActionScreenBuilder
import br.com.zup.beagle.widget.core.FlexBuilder
import br.com.zup.beagle.widget.core.Size
import br.com.zup.beagle.widget.layout.Screen
import br.com.zup.beagle.widget.ui.Text
import javax.inject.Singleton

@Singleton
class SampleActionService {
    fun createAction() = ActionScreenBuilder


    fun getNavigateExample() = Screen(
        child = Text("Hello").buildAndApplyFlex(
            FlexBuilder()
                .size(Size(width = 20.unitPercent()))
                .margin()
        )
    )
}
